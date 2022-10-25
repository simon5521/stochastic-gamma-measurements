package javaenv;

import hu.bme.mit.gamma.casestudy.iotsystem.iotsystem.IoTSystem;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface;
//import py4j.GatewayServer; great old times...


		
		
		
		public class DetModelEntryPoint  {
			
			public IoTSystem detModel=new IoTSystem();
			
			
			public MonitorOfAspectSystemFailuresNewEvent monitorOfAspectSystemFailuresNewEvent=new MonitorOfAspectSystemFailuresNewEvent();
			public MonitorOfEndConditionSystemCarLeaveNewEvent monitorOfEndConditionSystemCarLeaveNewEvent= new MonitorOfEndConditionSystemCarLeaveNewEvent();
			
			
			public DetModelEntryPoint(){
				detModel.getFailures().registerListener(monitorOfAspectSystemFailuresNewEvent);
				detModel.getCarLeave().registerListener(monitorOfEndConditionSystemCarLeaveNewEvent);
			}
			
			public void reset() {
				/*
				detModel=null;
				System.gc();
				detModel=new IoTSystem();
				detModel.getFailures().registerListener(monitorOfAspectSystemFailuresNewEvent);
				detModel.getCarLeave().registerListener(monitorOfEndConditionSystemCarLeaveNewEvent);
				*/
				monitorOfAspectSystemFailuresNewEvent.reset();
				monitorOfEndConditionSystemCarLeaveNewEvent.reset();
				detModel.reset();
			}
			
			
			public IoTSystem getDetModel(){
				return detModel;
			}
			
			public IoTSystem getSystem(){
				return detModel;
			}   
								
			
				public class MonitorOfAspectSystemFailuresNewEvent implements EventStreamInterface.Listener.Provided {
							
							public String state="run";
							
							public int freq=0;
							
							public double parameter=0.0/0.0;//NaN is the initial value intentionally 
							public double meanParameter=0.0;
							public double sumParameter=0.0;
							
							public void getValue(){}
							
									
							public void reset(){
								state="run";
								freq=0;
								parameter=0.0/0.0;
								sumParameter=0.0;
								meanParameter=0.0;
							}
													
							@Override
							public void raiseNewEvent(){
								state="stop";
								freq++;
							}
							
				}
			
			
			
				public class MonitorOfEndConditionSystemCarLeaveNewEvent implements EventStreamInterface.Listener.Provided {
							
							public String state="run";
							
									
							public void reset(){
								state="run";
							}
													
							@Override
							public void raiseNewEvent(){
								state="stop";
							}
							
				}
			
			
			
		}




