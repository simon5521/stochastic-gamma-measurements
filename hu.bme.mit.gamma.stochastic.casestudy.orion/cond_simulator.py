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




if61 = \
    JClass('hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.StateMachine_Interface_For_OrionInterface$Listener$Provided'
           )


@JImplements(if61)
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
#		implements = ["hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.StateMachine_Interface_For_OrionInterface$Listener$Provided"]


class StochasticEventGenerator():


	def __init__(self,detmodel):
		self.detmodel=detmodel
		self.time=0.0
		self.events=[]
		self.dists=[]
		self.components=dict()
		self.timerMean=torch.tensor([0.0])
		#0
		#0
		
		self.components.clear()
		
		self.components.update({ "Orion().TimerKeepAliveReceiveTimeout_3" :
			PeriodicEventSource(
				name  = "Orion().TimerKeepAliveReceiveTimeout_3",
				calls = {"TimoeutKeepAliveReceiveTimeout_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent())}}
				,
				rules = {"TimoeutKeepAliveReceiveTimeout_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@1a520c2b]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_31287")
				}}
				,
				portevents = {
				"TimoeutKeepAliveReceiveTimeout_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerKapcsolodik_2" :
			PeriodicEventSource(
				name  = "Orion().TimerKapcsolodik_2",
				calls = {"TimeoutKapcsolodik_2" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutKapcsolodik_2().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_2" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@4fa62a11]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_21288")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_2" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerZarva_0" :
			PeriodicEventSource(
				name  = "Orion().TimerZarva_0",
				calls = {"TimeoutZarva_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutZarva_0().raiseNewEvent())}}
				,
				rules = {"TimeoutZarva_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@4050703]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerZarva_01289")
				}}
				,
				portevents = {
				"TimeoutZarva_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerKeepAliveSendTimeout_1" :
			PeriodicEventSource(
				name  = "Orion().TimerKeepAliveSendTimeout_1",
				calls = {"TimeoutKeepAliveSendTimeout_1" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutKeepAliveSendTimeout_1().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_1" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@7dfb83f4]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_11290")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_1" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerKeepAliveReceiveTimeout_4" :
			PeriodicEventSource(
				name  = "Orion().TimerKeepAliveReceiveTimeout_4",
				calls = {"TimeoutKeepAliveReceiveTimeout_4" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveReceiveTimeout_4" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@79ad408f]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveReceiveTimeout_41291")
				}}
				,
				portevents = {
				"TimeoutKeepAliveReceiveTimeout_4" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerKapcsolodik_3" :
			PeriodicEventSource(
				name  = "Orion().TimerKapcsolodik_3",
				calls = {"TimeoutKapcsolodik_3" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutKapcsolodik_3().raiseNewEvent())}}
				,
				rules = {"TimeoutKapcsolodik_3" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@43459f16]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKapcsolodik_31292")
				}}
				,
				portevents = {
				"TimeoutKapcsolodik_3" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "Orion().TimerKeepAliveSendTimeout_0" :
			PeriodicEventSource(
				name  = "Orion().TimerKeepAliveSendTimeout_0",
				calls = {"TimeoutKeepAliveSendTimeout_0" : {
				"NewEvent" : (lambda:self.detmodel.getOrion().getOrionSystem().getTimeoutKeepAliveSendTimeout_0().raiseNewEvent())}}
				,
				rules = {"TimeoutKeepAliveSendTimeout_0" : {
					#[]
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@17645602]
					"NewEvent" : 
					ContinuousRandomVariable(pyro.distributions.Normal(loc=self.timerMean[0],scale=torch.tensor(0.5)),"ContRandomVarriableTimerKeepAliveSendTimeout_01293")
				}}
				,
				portevents = {
				"TimeoutKeepAliveSendTimeout_0" : [
					"NewEvent"
				]
				}
				,
				simulator=self)})
		
		
		
		
		
		
		self.components.update({ "Orion()OrionSystem()Master().masterSlaveChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()OrionSystem()Master().masterSlaveChannel",
				inport=self.detmodel.getOrion().getOrionSystem().getMaster().getMaster().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getOrionSystem().getMaster().getSlave().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="MasterSlaveChannel1294")
				,
				simulator=self)})
		
		self.components.update({ "Orion()OrionSystem()Master().slaveMasterChannel" :
			SwitchStateMachine_Interface_For_Orion(
				name  = "Orion()OrionSystem()Master().slaveMasterChannel",
				inport=self.detmodel.getOrion().getOrionSystem().getMaster().getSlave().getSend_StateMachine_Port(),
				calls={
				"Output" : [
				self.detmodel.getOrion().getOrionSystem().getMaster().getMaster().getStateMachine_Port()], 
				"Lossport" : [None]}
				,
				portarray=["Output", "Lossport"]
				,
				categorical=RandomVariable(dist=pyro.distributions.Categorical(torch.tensor([
						0.9, 
										0.1
								])),
					name="SlaveMasterChannel1295")
				,
				simulator=self)})
		
		
		
		

	def reset(self):
		self.time=0
		self.events.clear()
		for dist in self.dists:
			dist.reset()
		self.timerMean[0]=pyro.sample("param_0",pyro.distributions.Uniform(low=torch.tensor(4.0),high=torch.tensor(6.0))
		)
		#0
		self.detmodel.reset(self.timerMean#1
		)

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
