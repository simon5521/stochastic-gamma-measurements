package javaenv;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.dualgps.DualGPS;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.SensorInterface;
//import py4j.GatewayServer; great old times...


		
		
		
		public class DetModelEntryPoint  {
			
			public DualGPS detModel=new DualGPS();
			
			
			public MonitorOfAspectSystemCommunicationFailstop monitorOfAspectSystemCommunicationFailstop=new MonitorOfAspectSystemCommunicationFailstop();
			public MonitorOfEndConditionSystemCommunicationFailstop monitorOfEndConditionSystemCommunicationFailstop= new MonitorOfEndConditionSystemCommunicationFailstop();
			
			
			public DetModelEntryPoint(){
				detModel.getCommunication().registerListener(monitorOfAspectSystemCommunicationFailstop);
				detModel.getCommunication().registerListener(monitorOfEndConditionSystemCommunicationFailstop);
			}
			
			public void reset() {
				/*
				detModel=null;
				System.gc();
				detModel=new DualGPS();
				detModel.getCommunication().registerListener(monitorOfAspectSystemCommunicationFailstop);
				detModel.getCommunication().registerListener(monitorOfEndConditionSystemCommunicationFailstop);
				*/
				monitorOfAspectSystemCommunicationFailstop.reset();
				monitorOfEndConditionSystemCommunicationFailstop.reset();
				detModel.reset();
			}
			
			
			public DualGPS getDetModel(){
				return detModel;
			}
			
			public DualGPS getSystem(){
				return detModel;
			}   
								
			
				public class MonitorOfAspectSystemCommunicationFailstop implements SensorInterface.Listener.Provided {
							
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
							public void raiseFailstop(){
								state="stop";
								freq++;
							}
							
				}
			
			
			
				public class MonitorOfEndConditionSystemCommunicationFailstop implements SensorInterface.Listener.Provided {
							
							public String state="run";
							
									
							public void reset(){
								state="run";
							}
													
							@Override
							public void raiseFailstop(){
								state="stop";
							}
							
				}
			
			
			
		}




