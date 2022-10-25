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
simNumber=100



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



if33 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface$Listener$Provided'
           )


@JImplements(if33)
class DelayEventStream():
	def __init__(self,name,inport,calls,rules,simulator):
		self.name=name
		callitem=list(calls.items())[0]#only one out port
		self.calls=callitem[1]
		self.port=callitem[0]
		self.rules=list(rules.items())[0][1]#only one out port
		self.event_cntr=0
		inport.registerListener(self)
		self.simulator=simulator
		#iterating through ports
		for port in list(rules.keys()):
			pevents=list(rules[port].keys())
			#iterating through events
			for pevent in pevents:
				rule=rules[port][pevent]
				simulator.dists.append(rule)



	def generateEvents(self):
		pass


			
	#definition of the interface functions
	
	@JOverride
	def raiseNewEvent(self,):
		time=self.rules["NewEvent"].calc(self.port+"."+"NewEvent",self.simulator.time)
		self.event_cntr=self.event_cntr+1
		failureTime=abs(time)+self.simulator.time
		for callitem in self.calls:
			callEvent=lambda:callitem.raiseNewEvent();
			self.simulator.events.append(Event(self,failureTime,callEvent))
#
#	class Java:
#		implements = ["hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface$Listener$Provided"]


if34 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface$Listener$Provided'
           )


@JImplements(if34)
class SwitchImageStream():
	def __init__(self,name,inport,calls,portarray,categorical,simulator):
		self.name=name
		self.calls=calls
		self.portarray=portarray
		self.categorical=categorical
		self.event_cntr=0
		inport.registerListener(self)
		self.simulator=simulator
		self.simulator.dists.append(categorical)
					
	def generateEvents(self):
		pass



	#definition of the interface functions

	@JOverride
	def raiseNewData(self,blurred, car):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["NewData"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if IESC_SYNC:
				callEvent=lambda:call.raiseNewData(blurred, car);
				self.simulator.events.append(Event(self,self.simulator.time,callEvent))
			else:
				call.raiseNewData(blurred, car)
#
#	class Java:
#		implements = ["hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface$Listener$Provided"]

if35 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface$Listener$Provided'
           )


@JImplements(if35)
class SampleImageStream():
	def __init__(self,name,inport,calls,rules,simulator):
		self.name=name
		callitem=list(calls.items())[0]#only one out port
		self.calls=callitem[1]
		self.port=callitem[0]
		self.rules=list(rules.items())[0][1]#only one out port
		self.event_cntr=0
		inport.registerListener(self)
		self.simulator=simulator
		#iterating through ports
		for port in rules.keys():
			pevents=rules[port].keys()
			#iterating through events
			for pevent in pevents:
				rule=rules[port][pevent]
				simulator.dists.append(rule)


	def generateEvents(self):
		pass
		#definition of the interface functions

	

	@JOverride
	def raiseNewData(self,blurred, car):
		blurred=self.rules["NewData"].calc(self.port+"."+"NewData",self.simulator.time)
		#hu.bme.mit.gamma.expression.model.impl.DecimalTypeDefinitionImpl@5032f24d
		self.event_cntr=self.event_cntr+1
		for call in self.calls:
			if IESC_SYNC:
				callEvent=lambda:call.raiseNewData(blurred, car);
				self.simulator.events.append(Event(self,self.simulator.time,callEvent))
			else:
				callEvent=call.raiseNewData(blurred, car)

#
#	class Java:
#		implements = ["hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface$Listener$Provided"]

class StochasticEventGenerator():


	def __init__(self,detmodel):
		self.detmodel=detmodel
		self.time=0.0
		self.events=[]
		self.dists=[]
		self.components=dict()
		#0
		
		self.components.clear()
		
		self.components.update({ "System()Camera1_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera1_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera1_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer496")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera2_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera2_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera2_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer497")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera3_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera3_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera3_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer498")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera4_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera4_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera4_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer499")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera5_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera5_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera5_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer500")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera6_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera6_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera6_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer501")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera7_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera7_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera7_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer502")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera8_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera8_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera8_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer503")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera9_().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera9_().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera9_().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer504")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera10().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera10().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera10().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer505")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera11().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera11().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera11().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer506")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera12().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera12().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera12().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer507")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera13().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera13().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera13().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer508")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera14().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera14().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera14().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer509")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera15().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera15().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera15().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer510")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera16().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera16().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera16().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer511")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera17().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera17().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera17().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer512")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera18().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera18().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera18().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer513")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera19().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera19().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera19().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer514")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera20().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera20().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera20().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer515")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera21().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera21().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera21().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer516")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera22().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera22().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera22().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer517")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera23().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera23().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera23().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer518")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera24().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera24().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera24().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer519")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera25().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera25().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera25().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer520")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera26().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera26().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera26().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer521")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera27().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera27().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera27().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer522")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera28().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera28().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera28().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer523")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera29().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera29().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera29().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer524")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera30().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera30().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera30().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer525")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera31().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera31().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera31().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer526")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera32().softwareTimer" :
			PeriodicEventSource(
				name  = "System()Camera32().softwareTimer",
				calls = {"Events" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getCamera32().getSoftware().getSoftwareTimer().raiseNewEvent())}}
				,
				rules = {"Events" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@732dc7b8]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.2),scale=torch.tensor(0.05)),"ContRandomVarriablesoftwareTimer527")
				}}
				,
				portevents = {
				"Events" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System()Traffic().carArrival" :
			PeriodicEventSource(
				name  = "System()Traffic().carArrival",
				calls = {"Cars" : {
				"NewEvent" : (lambda:self.detmodel.getSystem().getTraffic().getTrafficSct().getCarArrives().raiseNewEvent())}}
				,
				rules = {"Cars" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@25670a02]
					#
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(2.5)),"ContRandomVarriablecarArrival528")
				}}
				,
				portevents = {
				"Cars" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		
		
		
		self.components.update({ "System()Traffic().carDelay" :
			DelayEventStream(
				name  = "System()Traffic().carDelay",
				inport=self.detmodel.getSystem().getTraffic().getTrafficSct().getCarArrivesOut(),
				calls = {
				"CarOut" : [
				self.detmodel.getSystem().getTraffic().getTrafficSct().getCarLeaves()]}
				,
				rules = {"CarOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6bb3285]
					#
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(0.3),scale=torch.tensor(0.1)),"ContRandomVarriablecarDelay529")
				}}
				,
				simulator=self)})
		
		
		
		self.components.update({ "System()Camera1_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera1_().networkLoss",
				inport=self.detmodel.getSystem().getCamera1_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera1_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera1_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss530")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera2_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera2_().networkLoss",
				inport=self.detmodel.getSystem().getCamera2_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera2_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera2_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss531")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera3_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera3_().networkLoss",
				inport=self.detmodel.getSystem().getCamera3_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera3_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera3_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss532")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera4_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera4_().networkLoss",
				inport=self.detmodel.getSystem().getCamera4_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera4_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera4_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss533")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera5_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera5_().networkLoss",
				inport=self.detmodel.getSystem().getCamera5_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera5_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera5_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss534")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera6_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera6_().networkLoss",
				inport=self.detmodel.getSystem().getCamera6_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera6_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera6_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss535")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera7_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera7_().networkLoss",
				inport=self.detmodel.getSystem().getCamera7_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera7_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera7_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss536")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera8_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera8_().networkLoss",
				inport=self.detmodel.getSystem().getCamera8_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera8_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera8_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss537")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera9_().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera9_().networkLoss",
				inport=self.detmodel.getSystem().getCamera9_().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera9_().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera9_().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss538")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera10().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera10().networkLoss",
				inport=self.detmodel.getSystem().getCamera10().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera10().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera10().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss539")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera11().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera11().networkLoss",
				inport=self.detmodel.getSystem().getCamera11().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera11().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera11().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss540")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera12().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera12().networkLoss",
				inport=self.detmodel.getSystem().getCamera12().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera12().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera12().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss541")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera13().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera13().networkLoss",
				inport=self.detmodel.getSystem().getCamera13().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera13().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera13().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss542")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera14().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera14().networkLoss",
				inport=self.detmodel.getSystem().getCamera14().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera14().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera14().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss543")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera15().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera15().networkLoss",
				inport=self.detmodel.getSystem().getCamera15().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera15().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera15().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss544")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera16().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera16().networkLoss",
				inport=self.detmodel.getSystem().getCamera16().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera16().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera16().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss545")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera17().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera17().networkLoss",
				inport=self.detmodel.getSystem().getCamera17().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera17().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera17().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss546")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera18().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera18().networkLoss",
				inport=self.detmodel.getSystem().getCamera18().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera18().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera18().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss547")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera19().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera19().networkLoss",
				inport=self.detmodel.getSystem().getCamera19().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera19().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera19().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss548")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera20().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera20().networkLoss",
				inport=self.detmodel.getSystem().getCamera20().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera20().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera20().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss549")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera21().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera21().networkLoss",
				inport=self.detmodel.getSystem().getCamera21().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera21().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera21().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss550")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera22().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera22().networkLoss",
				inport=self.detmodel.getSystem().getCamera22().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera22().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera22().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss551")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera23().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera23().networkLoss",
				inport=self.detmodel.getSystem().getCamera23().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera23().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera23().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss552")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera24().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera24().networkLoss",
				inport=self.detmodel.getSystem().getCamera24().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera24().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera24().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss553")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera25().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera25().networkLoss",
				inport=self.detmodel.getSystem().getCamera25().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera25().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera25().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss554")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera26().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera26().networkLoss",
				inport=self.detmodel.getSystem().getCamera26().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera26().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera26().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss555")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera27().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera27().networkLoss",
				inport=self.detmodel.getSystem().getCamera27().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera27().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera27().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss556")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera28().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera28().networkLoss",
				inport=self.detmodel.getSystem().getCamera28().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera28().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera28().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss557")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera29().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera29().networkLoss",
				inport=self.detmodel.getSystem().getCamera29().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera29().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera29().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss558")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera30().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera30().networkLoss",
				inport=self.detmodel.getSystem().getCamera30().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera30().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera30().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss559")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera31().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera31().networkLoss",
				inport=self.detmodel.getSystem().getCamera31().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera31().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera31().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss560")
				,
				simulator=self)})
		
		self.components.update({ "System()Camera32().networkLoss" :
			SwitchImageStream(
				name  = "System()Camera32().networkLoss",
				inport=self.detmodel.getSystem().getCamera32().getSoftware().getImages(),
				calls={
				"ImageOut" : [
				self.detmodel.getSystem().getCamera32().getNetworkQueue().getImageIn()], 
				"LostImages" : [
				self.detmodel.getSystem().getCamera32().getNetworkQueue().getImageIn()]}
				,
				portarray=["ImageOut", "LostImages"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="NetworkLoss561")
				,
				simulator=self)})
		
		
		
		self.components.update({ "System()Camera1_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera1_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera1_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera1_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur562")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera2_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera2_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera2_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera2_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur563")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera3_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera3_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera3_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera3_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur564")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera4_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera4_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera4_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera4_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur565")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera5_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera5_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera5_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera5_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur566")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera6_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera6_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera6_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera6_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur567")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera7_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera7_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera7_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera7_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur568")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera8_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera8_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera8_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera8_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur569")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera9_()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera9_()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera9_().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera9_().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur570")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera10()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera10()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera10().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera10().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur571")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera11()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera11()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera11().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera11().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur572")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera12()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera12()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera12().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera12().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur573")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera13()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera13()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera13().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera13().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur574")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera14()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera14()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera14().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera14().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur575")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera15()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera15()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera15().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera15().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur576")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera16()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera16()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera16().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera16().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur577")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera17()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera17()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera17().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera17().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur578")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera18()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera18()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera18().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera18().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur579")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera19()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera19()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera19().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera19().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur580")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera20()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera20()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera20().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera20().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur581")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera21()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera21()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera21().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera21().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur582")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera22()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera22()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera22().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera22().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur583")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera23()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera23()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera23().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera23().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur584")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera24()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera24()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera24().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera24().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur585")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera25()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera25()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera25().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera25().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur586")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera26()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera26()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera26().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera26().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur587")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera27()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera27()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera27().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera27().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur588")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera28()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera28()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera28().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera28().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur589")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera29()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera29()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera29().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera29().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur590")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera30()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera30()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera30().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera30().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur591")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera31()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera31()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera31().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera31().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur592")
				}}
				,
				simulator=self)})
		
		self.components.update({ "System()Camera32()Software()CameraSoftware().imageBlur" :
			SampleImageStream(
				name  = "System()Camera32()Software()CameraSoftware().imageBlur",
				inport=self.detmodel.getSystem().getCamera32().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
				calls = {
				"ImageOut" : [
				self.detmodel.getSystem().getCamera32().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()]}
				,
				rules = {"ImageOut" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2b630a64]
					#
					"NewData" : 
					ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1)),"DiscRandomVarriableimageBlur593")
				}}
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
				
		if detmodel.monitorOfEndConditionSystemFailuresNewEvent.state != "run":
			break

	# get the aspects and return from the simulations 
	#register the result of the analysis to the Pyro
	state2num(detmodel.monitorOfAspectSystemFailuresNewEvent.state)
	pyro.deterministic("AspectSystemFailuresNewEvent",torch.tensor(state2num(detmodel.monitorOfAspectSystemFailuresNewEvent.state)))
	#return the result of the simulation
	return state2num(detmodel.monitorOfAspectSystemFailuresNewEvent.state)
	



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
