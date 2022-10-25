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
		
		
		self.components.update({ "System().GPS1_Failure" :
			EventSource(
				name  = "System().GPS1_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS1().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@659df7b9]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS1_Failure9")
				}}
				,
				portevents = {
				"Faults" : [
					"Failure"
				]
				}
				,
				simulator=self)})
		
		self.components.update({ "System().GPS2_Failure" :
			EventSource(
				name  = "System().GPS2_Failure",
				calls = {"Faults" : {
				"Failure" : (lambda:self.detmodel.getSystem().getGPS2().getFaults().raiseFailure())}}
				,
				rules = {"Faults" : {
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@10fb1719]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS2_Failure10")
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
					#[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6dd45ad]
					#
					"Failure" : 
					ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(20.0)),"ContRandomVarriableVoter_Failure11")
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
