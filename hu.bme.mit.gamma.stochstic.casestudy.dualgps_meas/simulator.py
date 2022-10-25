import pyro
import torch
from pyro.infer import SVI, Trace_ELBO
from pyro.optim import Adam
import pyro.distributions as dist
import torch.distributions.constraints as constraints
from pyro.distributions.distribution import Distribution

import math
from math import exp
import numpy as np

from py4j.java_gateway import JavaGateway, CallbackServerParameters
import matplotlib.pyplot as plt
import matplotlib

from influxdb import InfluxDBClient

import pyro.contrib.gp as gp

import time
import os
import datetime
import traceback
from jpype import JImplements, JOverride
from jpype import *
import jpype

DEBUG=False
IESC_SYNC=False

simTime=1.0
simNumber=50



#time.sleep(3)

print('initiating Python-Java connection')

def create_detmodel():
    commands = ["""javac $(find . -name "*.java") -cp /usr/share/java/py4j0.10.8.1.jar"""]
    for command in commands:
        if os.system(command) == 0:
            continue
        else:
            print( "ERROR")
            break
    startJVM(getDefaultJVMPath(), '-ea',
             '-Djava.class.path=' + str(os.path.realpath(__file__).replace(str(os.path.basename(__file__)),"")) + '/bin'
             )
    detmodel = 0
    DetModelEntryPoint = JClass('javaenv.DetModelEntryPoint')
    detmodel = DetModelEntryPoint()
    return detmodel

detmodel=create_detmodel()


print('Python-Java connection established')


class Dataset():

	def __init__(self,dbname,ip,port,query=None,script=None):
		if query is not None:
			client = InfluxDBClient(ip, int(port), database=dbname)
			result = client.query(query)
			points = result.get_points()
			self.points=points
		elif script is not None:
			exec(script)


# stochastic model classes

class ContinuousRandomVariable():
	def __init__(self,dist,name,N=1):
		self.dist=dist
		self.name=name
		self.event_cntr=N-1
		self.meta_cntr=-1
		self.N=N
	def calc(self,event=0,time=0):
		self.event_cntr=self.event_cntr+1
		if self.N>0:
			if self.event_cntr==self.N:
				self.event_cntr=0
				self.meta_cntr=self.meta_cntr+1
				self.samples=pyro.sample(self.name+"_sample_"+str(self.meta_cntr),self.dist.expand([self.N]))
			return self.samples[self.event_cntr].item()
		else:
			return pyro.sample(self.name+"_sample_"+str(self.event_cntr),self.dist).item()
	def reset(self):
		self.event_cntr=self.N-1
		self.meta_cntr=-1


class DiscreteRandomVariable():
	def __init__(self,dist,name):
		self.dist=dist
		self.name=name
		self.event_cntr=0
	def calc(self,event=0,time=0):
		self.event_cntr=self.event_cntr+1
		return pyro.sample(self.name+"_sample_"+str(self.event_cntr),self.dist).item()-1.0


class RandomVariable():
	def __init__(self,dist,name,N=1):
		self.dist=dist
		self.name=name
		self.event_cntr=N-1
		self.meta_cntr=-1
		self.N=N
	def calc(self,event=0,time=0):
		self.event_cntr=self.event_cntr+1
		if self.N>0:
			if self.event_cntr==self.N:
				self.event_cntr=0
				self.meta_cntr=self.meta_cntr+1
				
				self.samples=pyro.sample(self.name+"_sample_"+str(self.meta_cntr),self.dist.expand([self.N]))
			return self.samples[self.event_cntr].item()
		else:
			return pyro.sample(self.name+"_sample_"+str(self.event_cntr),self.dist).item()
	def reset(self):
		self.event_cntr=self.N-1
		self.meta_cntr=-1


# environment component classes


class Event():
	def __init__(self,eventSource,eventTime):
		self.eventSource=eventSource
		self.eventTime=eventTime
	def __init__(self,eventSource,eventTime,eventCall):
		self.eventSource=eventSource
		self.eventTime=eventTime
		self.eventCall=eventCall


class PeriodicEventSource():
	def __init__(self,name,calls,rules,portevents,simulator):
		self.name=name
		self.calls=calls
		self.rules=rules
		self.portevents=portevents
		self.simulator=simulator
		ports=list(self.calls.keys())
		#iterating through ports
		for port in ports:
			pevents=self.portevents[port]
			#iterating through events
			for pevent in pevents:
				rule=self.rules[port][pevent]
				self.simulator.dists.append(rule)


	def generateEvents(self):
		ports=list(self.calls.keys())
		#iterating through self.ports
		for port in ports:
			pevents=self.portevents[port]
			#iterating through events
			for pevent in pevents:
				call=self.calls[port][pevent]
				rule=self.rules[port][pevent]
				simulationtime=0.0
				while simulationtime < simTime:
					simulationtime=simulationtime+rule.calc(port+"."+pevent,simulationtime)
					#iterating through port connections
					self.simulator.events.append(Event(self,simulationtime,call))



class EventSource():
	def __init__(self,name,calls,rules,portevents,simulator):
		self.name=name
		self.calls=calls
		self.rules=rules
		self.portevents=portevents
		self.simulator=simulator
		ports=list(self.calls.keys())
		#iterating through ports
		for port in ports:
			pevents=self.portevents[port]
			#iterating through events
			for pevent in pevents:
				rule=self.rules[port][pevent]
				self.simulator.dists.append(rule)


	def generateEvents(self):
		ports=list(self.calls.keys())
		#iterating through ports
		for port in ports:
			pevents=self.portevents[port]
			#iterating through events
			for pevent in pevents:
				rule=self.rules[port][pevent]
				call=self.calls[port][pevent]
				time=rule.calc(port+"."+pevent,0.0)
				if time>=0:
					#iterating through port connections
					self.simulator.events.append(Event(self,time,call))






class StochasticEventGenerator():


	def __init__(self,detmodel):
		self.detmodel=detmodel
		self.time=0.0
		self.events=[]
		self.dists=[]
		self.components=dict()
		#0
		
		self.components.clear()
		
		
		self.components.update({ "System().GPS1__Failure" :
			EventSource(
				name  = "System().GPS1__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS1_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@424deee4]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS1__Failure153")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS2__Failure" :
			EventSource(
				name  = "System().GPS2__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS2_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@3f6a458b]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS2__Failure154")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS3__Failure" :
			EventSource(
				name  = "System().GPS3__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS3_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6bd2588b]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS3__Failure155")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS4__Failure" :
			EventSource(
				name  = "System().GPS4__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS4_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@670a2e62]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS4__Failure156")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS5__Failure" :
			EventSource(
				name  = "System().GPS5__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS5_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@27bbc0d1]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS5__Failure157")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS6__Failure" :
			EventSource(
				name  = "System().GPS6__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS6_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@22348caa]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS6__Failure158")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS7__Failure" :
			EventSource(
				name  = "System().GPS7__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS7_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@14382917]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS7__Failure159")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS8__Failure" :
			EventSource(
				name  = "System().GPS8__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS8_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@111f4f42]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS8__Failure160")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS9__Failure" :
			EventSource(
				name  = "System().GPS9__Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS9_().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@3e50ff17]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS9__Failure161")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS10_Failure" :
			EventSource(
				name  = "System().GPS10_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS10().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@5b80d285]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS10_Failure162")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS11_Failure" :
			EventSource(
				name  = "System().GPS11_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS11().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@73f89391]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS11_Failure163")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS12_Failure" :
			EventSource(
				name  = "System().GPS12_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS12().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@674d725]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS12_Failure164")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS13_Failure" :
			EventSource(
				name  = "System().GPS13_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS13().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@3f05fe44]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS13_Failure165")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS14_Failure" :
			EventSource(
				name  = "System().GPS14_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS14().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@4002429d]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS14_Failure166")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS15_Failure" :
			EventSource(
				name  = "System().GPS15_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS15().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@27d743b6]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS15_Failure167")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS16_Failure" :
			EventSource(
				name  = "System().GPS16_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS16().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@771dbacd]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS16_Failure168")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS17_Failure" :
			EventSource(
				name  = "System().GPS17_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS17().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@38506b01]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS17_Failure169")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS18_Failure" :
			EventSource(
				name  = "System().GPS18_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS18().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@756ee9bf]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS18_Failure170")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS19_Failure" :
			EventSource(
				name  = "System().GPS19_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS19().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@65c8dd7a]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS19_Failure171")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS20_Failure" :
			EventSource(
				name  = "System().GPS20_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS20().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@4117fea]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS20_Failure172")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS21_Failure" :
			EventSource(
				name  = "System().GPS21_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS21().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@777af929]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS21_Failure173")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS22_Failure" :
			EventSource(
				name  = "System().GPS22_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS22().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@7161ff14]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS22_Failure174")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS23_Failure" :
			EventSource(
				name  = "System().GPS23_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS23().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2e177346]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS23_Failure175")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS24_Failure" :
			EventSource(
				name  = "System().GPS24_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS24().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@260f371e]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS24_Failure176")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS25_Failure" :
			EventSource(
				name  = "System().GPS25_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS25().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@5fb3d657]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS25_Failure177")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS26_Failure" :
			EventSource(
				name  = "System().GPS26_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS26().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@265c8afa]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS26_Failure178")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS27_Failure" :
			EventSource(
				name  = "System().GPS27_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS27().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@19157dfe]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS27_Failure179")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS28_Failure" :
			EventSource(
				name  = "System().GPS28_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS28().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@1094d617]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS28_Failure180")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS29_Failure" :
			EventSource(
				name  = "System().GPS29_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS29().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6e93189a]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS29_Failure181")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS30_Failure" :
			EventSource(
				name  = "System().GPS30_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS30().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2ecb5d94]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS30_Failure182")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS31_Failure" :
			EventSource(
				name  = "System().GPS31_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS31().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@61c1b9ae]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS31_Failure183")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS32_Failure" :
			EventSource(
				name  = "System().GPS32_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS32().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@3cca00d2]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS32_Failure184")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().Voter_Failure" :
			EventSource(
				name  = "System().Voter_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getVoter().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@4154691d]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(20.0)),"ContRandomVarriableVoter_Failure185")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		
		
		
		
		
		
		

	def reset(self):
		self.time=0
		self.events.clear()
		for dist in self.dists:
			dist.reset()
		self.detmodel.reset()

	def generateEvents(self):
	    for component in list(self.components.values()):
	        component.generateEvents()


	def popEvent(self):
	    mintime=10000000000.0
	    min_i=0
	    for i in range (len(self.events)):
	        if self.events[i].eventTime<mintime:
	        	min_i=i
	        	mintime=self.events[min_i].eventTime
	    event=self.events[min_i]
	    self.events.remove(event)
	    return event



stochmodel=0
try:
	stochmodel = StochasticEventGenerator(detmodel)
except jpype.JException as ex:
		print("Caught base exception : ", str(ex))
		print(ex.stacktrace())
		shutdownJVM()
except Exception as ex:
		print("Caught python exception :", str(ex))
		shutdownJVM()
except Exception as err:
		print("Exception occured during testing the simulation: ")
		print(err)
		traceback.print_exc()
		shutdownJVM()

def state2num(state):
	if state=="run":
		return 0.0
	else:
		return 1.0

def simulate():
	if DEBUG:
		print("new sim ---------------------------------")
	global stochmodel, detmodel
	stochmodel.reset()
	stochmodel.generateEvents()
	while len(stochmodel.events) > 0 and stochmodel.time < simTime:
		event = stochmodel.popEvent()
		stochmodel.time = event.eventTime
		if DEBUG:
			print(event.eventSource.name + ' at time: ' + str(stochmodel.time))
		event.eventCall()
		detmodel.getSystem().schedule()
		# evaluate end condition
				
		if detmodel.monitorOfEndConditionSystemCommunicationFailstop.state != "run":
			break

	# get the aspects and return from the simulations 
	#register the result of the analysis to the Pyro
	stochmodel.time
	pyro.deterministic("AspectSystemCommunicationFailstop",torch.tensor(stochmodel.time))
	#return the result of the simulation
	return stochmodel.time
	



if DEBUG:
	print("testing the simulator")
	try:
		for i in range(10):
			print(simulate())
	except Exception as err:
		print("Exception occured during testing the simulation: ")
		print(err)
		traceback.print_exc()
	except java.lang.RuntimeException as ex:
		print("Caught runtime exception : ", str(ex))
		print(ex.stacktrace())
	except jpype.JException as ex:
		print("Caught base exception : ", str(ex))
		print(ex.stacktrace())
	except Exception as ex:
		print("Caught python exception :", str(ex))
	except Exception as err:
		print("Exception occured during testing the simulation: ")
		print(err)
		traceback.print_exc()
	finally:
		shutdownJVM()
