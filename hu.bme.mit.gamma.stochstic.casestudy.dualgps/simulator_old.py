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

DEBUG=True

print("compile java code")

os.system("""javac $(find . -name "*.java") -cp /usr/share/java/py4j0.10.8.1.jar""")

print("java code compiled")

print('initiating Python-Java connection')

startJVM(getDefaultJVMPath(), '-ea',
         '-Djava.class.path=' + str(os.path.realpath(__file__).replace("/simulator.py","")) + '/bin'
         )

DetModelEntryPoint = JClass('javaenv.DetModelEntryPoint')

detmodel = DetModelEntryPoint()


simTime=1.0
simNumber=50


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
    def __init__(self,dist,name,N=100000):
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
    def __init__(self,dist,name,N=100000):
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



        self.components.update({ "_GPS1_Failure" :
            EventSource(
                name  = "_GPS1_Failure",
                calls = {
                "Faults" : {
                "Failure" : (lambda:self.detmodel.getSystem().getGPS1().getFaults().raiseFailure())
                }
                }
                ,
                rules = {
                "Faults" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@1f9d1cb2]
                #
                "Failure" : 
                ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS1_Failure51")
                }
                }
                ,
                portevents = {
                "Faults" : [
                    "Failure"
                ]
                }
                ,
                simulator=self
            )}
        )

        self.components.update({ "_GPS2_Failure" :
            EventSource(
                name  = "_GPS2_Failure",
                calls = {
                "Faults" : {
                "Failure" : (lambda:self.detmodel.getSystem().getGPS2().getFaults().raiseFailure())
                }
                }
                ,
                rules = {
                "Faults" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@22dea7bc]
                #
                "Failure" : 
                ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(10.0)),"ContRandomVarriableGPS2_Failure52")
                }
                }
                ,
                portevents = {
                "Faults" : [
                    "Failure"
                ]
                }
                ,
                simulator=self
            )}
        )

        self.components.update({ "_Voter_Failure" :
            EventSource(
                name  = "_Voter_Failure",
                calls = {
                "Faults" : {
                "Failure" : (lambda:self.detmodel.getSystem().getVoter().getFaults().raiseFailure())
                }
                }
                ,
                rules = {
                "Faults" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@37e899c9]
                #
                "Failure" : 
                ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(20.0)),"ContRandomVarriableVoter_Failure53")
                }
                }
                ,
                portevents = {
                "Faults" : [
                    "Failure"
                ]
                }
                ,
                simulator=self
            )}
        )










    def reset(self):
        self.time=0
        self.events.clear()
        for dist in self.dists:
            dist.reset()
        i=0
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



stochmodel = StochasticEventGenerator(detmodel)


def state2Num(state):
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
            #print(detmodel.monitorOfEndConditionSystemCommunicationFailstop.state)
           # print(detmodel.getSystem().getCommunication().isRaisedFailstop())
        event.eventCall()

        #print(detmodel.getSystem().getCommunication().isRaisedFailstop())
        # evaluate end condition
        detmodel.getSystem().schedule()
        #print(detmodel.getSystem().getVoter().getCommunication().isRaisedFailstop())
        print(detmodel.monitorOfEndConditionSystemCommunicationFailstop.state)
        #print(detmodel.getSystem().getVoter().getVoter().toString())
        if detmodel.monitorOfEndConditionSystemCommunicationFailstop.state != "run":
            break
    # get the aspects and return from the simulations 
    return stochmodel.time




if DEBUG:
    detmodel.getSystem().reset()
    detmodel.getSystem().getGPS1().getFaults().raiseFailure()
    detmodel.getSystem().schedule()
    detmodel.getSystem().getGPS2().getFaults().raiseFailure()
    detmodel.getSystem().schedule()
    print("New state is:",detmodel.monitorOfAspectSystemCommunicationFailstop.state)
    #detmodel.getSystem().start()
    print("testing the simulator")
    try:
        for i in range(20):
            print(simulate())
    except Exception as err:
        print("Exception occured during testing the simulation: ")
        print(err)
        traceback.print_exc()
    finally:
        shutdownJVM()
        
