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




if57 = \
    JClass('hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.StateMachine_Interface_For_OrionInterface$Listener$Provided'
           )


@JImplements(if57)
class SwitchStateMachine_Interface_For_Orion():
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
	def raiseOrionDisconn(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionDisconn"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionDisconn();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionDisconn()

	@JOverride
	def raiseOrionDisconnCause(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionDisconnCause"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionDisconnCause();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionDisconnCause()

	@JOverride
	def raiseOrionConnReq(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionConnReq"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionConnReq();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionConnReq()

	@JOverride
	def raiseOrionAppData(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionAppData"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionAppData();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionAppData()

	@JOverride
	def raiseOrionKeepAlive(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionKeepAlive"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionKeepAlive();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionKeepAlive()

	@JOverride
	def raiseOrionConnConf(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionConnConf"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionConnConf();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionConnConf()

	@JOverride
	def raiseOrionConnResp(self,):
		port=self.portarray[self.categorical.calc()]
		eventcalls=self.calls[port]#["OrionConnResp"]
		self.event_cntr=self.event_cntr+1
		for call in eventcalls:
			if call is not None:
				if IESC_SYNC:
					callEvent=lambda:call.raiseOrionConnResp();
					self.simulator.events.append(Event(self,self.simulator.time,callEvent))
				else:
					call.raiseOrionConnResp()
#
#	class Java:
#		implements = ["hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.StateMachine_Interface_For_OrionInterface$Listener$Provided"]


class StochasticEventGenerator():


	def __init__(self,detmodel):
		self.detmodel=detmodel
		self.time=0.0
		self.events=[]
		self.dists=[]
		self.components=dict()
		#0
		
		self.components.clear()
		
		self.components.update({ "Orion()SubSystem1_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31116")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21117")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01118")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11119")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41120")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31121")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem1_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01122")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31123")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21124")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01125")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11126")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41127")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31128")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem2_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01129")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31130")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21131")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01132")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11133")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41134")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31135")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem3_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01136")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31137")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21138")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01139")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11140")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41141")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31142")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem4_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01143")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31144")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21145")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01146")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11147")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41148")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31149")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem5_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01150")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31151")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21152")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01153")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11154")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41155")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31156")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem6_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01157")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31158")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21159")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01160")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11161")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41162")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31163")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem7_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01164")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31165")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21166")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01167")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11168")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41169")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31170")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem8_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01171")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31172")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21173")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01174")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11175")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41176")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31177")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem9_().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01178")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31179")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21180")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01181")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11182")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41183")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31184")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem10().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem10().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01185")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31186")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21187")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01188")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11189")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41190")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31191")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem11().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem11().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01192")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31193")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21194")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01195")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11196")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41197")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31198")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem12().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem12().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01199")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31200")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21201")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01202")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11203")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41204")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31205")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem13().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem13().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01206")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31207")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21208")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01209")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11210")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41211")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31212")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem14().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem14().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01213")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31214")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21215")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01216")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11217")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41218")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31219")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem15().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem15().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01220")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@408f585e]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31221")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@9e6bc48]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21222")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@12492768]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01223")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@30b6e4ba]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11224")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@515e48eb]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41225")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2d822268]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31226")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion()SubSystem16().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getSubSystem16().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@44887fc]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=torch.tensor(5.0),scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01227")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		
		
		
		
		
		self.components.update({ "Orion()SubSystem1_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem1_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1228")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem1_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem1_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem1_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1229")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem2_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1230")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem2_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem2_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem2_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1231")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem3_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1232")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem3_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem3_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem3_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1233")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem4_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1234")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem4_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem4_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem4_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1235")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem5_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1236")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem5_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem5_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem5_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1237")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem6_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1238")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem6_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem6_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem6_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1239")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem7_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1240")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem7_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem7_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem7_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1241")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem8_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1242")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem8_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem8_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem8_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1243")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem9_()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1244")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem9_()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem9_()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem9_().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1245")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem10()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem10().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem10().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1246")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem10()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem10()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem10().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem10().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1247")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem11()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem11().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem11().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1248")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem11()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem11()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem11().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem11().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1249")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem12()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem12().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem12().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1250")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem12()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem12()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem12().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem12().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1251")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem13()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem13().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem13().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1252")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem13()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem13()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem13().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem13().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1253")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem14()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem14().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem14().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1254")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem14()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem14()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem14().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem14().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1255")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem15()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem15().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem15().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1256")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem15()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem15()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem15().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem15().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1257")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem16()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getSubSystem16().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem16().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1258")
				,
				simulator=self)})
		
		self.components.update({ "Orion()SubSystem16()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()SubSystem16()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getSubSystem16().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getSubSystem16().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1259")
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
		detmodel.getOrion().schedule()
		# evaluate end condition
				
		if detmodel.monitorOfEndConditionOrionSystemConnStatusConn.state != "run":
			break

	# get the aspects and return from the simulations 
	#register the result of the analysis to the Pyro
	stochmodel.time
	pyro.deterministic("AspectOrionSystemConnStatusConn",torch.tensor(stochmodel.time))
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
