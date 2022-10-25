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


os.system("""javac $(find . -name "*.java") -cp /usr/share/java/py4j0.10.8.1.jar""")

#time.sleep(3)

print('initiating Python-Java connection')

startJVM(getDefaultJVMPath(), '-ea',
         '-Djava.class.path=' + str(os.path.realpath(__file__).replace("/cond_simulator.py","")) + '/bin'
         )



detmodel = 0
try:
    DetModelEntryPoint = JClass('javaenv.DetModelEntryPoint')
    detmodel = DetModelEntryPoint()
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



if3 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface$Listener$Provided'
           )


@JImplements(if3)
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
#    class Java:
#        implements = ["hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface$Listener$Provided"]


if4 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface$Listener$Provided'
           )


@JImplements(if4)
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
#    class Java:
#        implements = ["hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface$Listener$Provided"]

if5 = \
    JClass('hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface$Listener$Provided'
           )


@JImplements(if5)
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
        #hu.bme.mit.gamma.expression.model.impl.DecimalTypeDefinitionImpl@143b0272
        self.event_cntr=self.event_cntr+1
        for call in self.calls:
            if IESC_SYNC:
                callEvent=lambda:call.raiseNewData(blurred, car);
                self.simulator.events.append(Event(self,self.simulator.time,callEvent))
            else:
                callEvent=call.raiseNewData(blurred, car)

#
#    class Java:
#        implements = ["hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface$Listener$Provided"]

class StochasticEventGenerator():


    def __init__(self,detmodel):
        self.detmodel=detmodel
        self.time=0.0
        self.events=[]
        self.dists=[]
        self.components=dict()

    def reset(self):
        self.time=0
        self.events.clear()
        for dist in self.dists:
            dist.reset()
        stochmodel.delayMean=pyro.sample("param_0",pyro.distributions.Uniform(
                low=torch.tensor(0.0),high=torch.tensor(1.0))).item()
        #0
        self.detmodel.reset(self.delayMean)
        
        self.components.clear()
        
        self.components.update({ "System()Camera1().softwareTimer" :
            PeriodicEventSource(
                name  = "System()Camera1().softwareTimer",
                calls = {
                "Events" : {
                "NewEvent" : (lambda:self.detmodel.getSystem().getCamera1().getSoftware().getSoftwareTimer().raiseNewEvent())
                }
                }
                ,
                rules = {
                "Events" : {
                #[]
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2bcd5abf]
                "NewEvent" : 
                ContinuousRandomVariable(pyro.distributions.Normal(
                        torch.tensor(0.2
                        ),
                        torch.tensor(0.05
                        )
                )
                ,"ContRandomVarriablesoftwareTimer8")
                }
                }
                ,
                portevents = {
                "Events" : [
                    "NewEvent"
                ]
                }
                ,
                simulator=self
            )}
        )
        
        self.components.update({ "System()Camera2().softwareTimer" :
            PeriodicEventSource(
                name  = "System()Camera2().softwareTimer",
                calls = {
                "Events" : {
                "NewEvent" : (lambda:self.detmodel.getSystem().getCamera2().getSoftware().getSoftwareTimer().raiseNewEvent())
                }
                }
                ,
                rules = {
                "Events" : {
                #[]
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@2bcd5abf]
                "NewEvent" : 
                ContinuousRandomVariable(pyro.distributions.Normal(
                        torch.tensor(0.2
                        ),
                        torch.tensor(0.05
                        )
                )
                ,"ContRandomVarriablesoftwareTimer9")
                }
                }
                ,
                portevents = {
                "Events" : [
                    "NewEvent"
                ]
                }
                ,
                simulator=self
            )}
        )
        
        self.components.update({ "System()Traffic().carArrival" :
            PeriodicEventSource(
                name  = "System()Traffic().carArrival",
                calls = {
                "Cars" : {
                "NewEvent" : (lambda:self.detmodel.getSystem().getTraffic().getTrafficSct().getCarArrives().raiseNewEvent())
                }
                }
                ,
                rules = {
                "Cars" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6e57680b]
                #
                "NewEvent" : 
                ContinuousRandomVariable(pyro.distributions.Exponential(torch.tensor(2.5
                )),"ContRandomVarriablecarArrival10")
                }
                }
                ,
                portevents = {
                "Cars" : [
                    "NewEvent"
                ]
                }
                ,
                simulator=self
            )}
        )
        
        
        
        
        
        
        self.components.update({ "System()Traffic().carDelay" :
            DelayEventStream(
                name  = "System()Traffic().carDelay",
                inport=self.detmodel.getSystem().getTraffic().getTrafficSct().getCarArrivesOut(),
                calls = {
                "CarOut" : [
                self.detmodel.getSystem().getTraffic().getTrafficSct().getCarLeaves()
                ]
                }
                ,
                rules = {
                "CarOut" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@3b74303c]
                #
                "NewEvent" : 
                ContinuousRandomVariable(pyro.distributions.Normal(
                        torch.tensor(stochmodel.delayMean
                        ),
                        torch.tensor(0.1
                        )
                )
                ,"ContRandomVarriablecarDelay11")
                }
                }
                ,
                simulator=self
            )}
        )
        
        
        
        self.components.update({ "System()Camera1().networkLoss" :
            SwitchImageStream(
                name  = "System()Camera1().networkLoss",
                inport=self.detmodel.getSystem().getCamera1().getSoftware().getImages(),
                calls={
                "ImageOut" : [
                self.detmodel.getSystem().getCamera1().getNetworkQueue().getImageLoss()
                ], 
                "LostImages" : [
                self.detmodel.getSystem().getCamera1().getNetworkQueue().getImageLoss()
                ]
                }
                ,
                portarray=["ImageOut", "LostImages"]
                ,
                categorical=RandomVariable(
                    dist=pyro.distributions.Categorical(
                        torch.tensor([
                        0.9, 
                        0.1
                        ])),
                    name="NetworkLoss12")
                ,
                simulator=self
            )}
        )
        
        self.components.update({ "System()Camera2().networkLoss" :
            SwitchImageStream(
                name  = "System()Camera2().networkLoss",
                inport=self.detmodel.getSystem().getCamera2().getSoftware().getImages(),
                calls={
                "ImageOut" : [
                self.detmodel.getSystem().getCamera2().getNetworkQueue().getImageLoss()
                ], 
                "LostImages" : [
                self.detmodel.getSystem().getCamera2().getNetworkQueue().getImageLoss()
                ]
                }
                ,
                portarray=["ImageOut", "LostImages"]
                ,
                categorical=RandomVariable(
                    dist=pyro.distributions.Categorical(
                        torch.tensor([
                        0.9, 
                        0.1
                        ])),
                    name="NetworkLoss13")
                ,
                simulator=self
            )}
        )
        
        
        
        self.components.update({ "System()Camera1()Software()CameraSoftware().imageBlur" :
            SampleImageStream(
                name  = "System()Camera1()Software()CameraSoftware().imageBlur",
                inport=self.detmodel.getSystem().getCamera1().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
                calls = {
                "ImageOut" : [
                self.detmodel.getSystem().getCamera1().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()
                ]
                }
                ,
                rules = {
                "ImageOut" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6b98cd2]
                #
                "NewData" : 
                ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1
                )),"DiscRandomVarriableimageBlur14")
                }
                }
                ,
                simulator=self
            )}
        )
        
        self.components.update({ "System()Camera2()Software()CameraSoftware().imageBlur" :
            SampleImageStream(
                name  = "System()Camera2()Software()CameraSoftware().imageBlur",
                inport=self.detmodel.getSystem().getCamera2().getSoftware().getCameraSoftware().getCameraDriver().getImages(),
                calls = {
                "ImageOut" : [
                self.detmodel.getSystem().getCamera2().getSoftware().getCameraSoftware().getCameraControl().getDriverImages()
                ]
                }
                ,
                rules = {
                "ImageOut" : {
                #[hu.bme.mit.gamma.environment.model.impl.StochasticRuleImpl@6b98cd2]
                #
                "NewData" : 
                ContinuousRandomVariable(pyro.distributions.Bernoulli(torch.tensor(0.1
                )),"DiscRandomVarriableimageBlur15")
                }
                }
                ,
                simulator=self
            )}
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
