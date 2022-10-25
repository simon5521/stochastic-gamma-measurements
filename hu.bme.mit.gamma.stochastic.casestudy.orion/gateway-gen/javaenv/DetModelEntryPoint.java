package javaenv;

import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_stoch_env_param.Orion_Environment_Param;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.ConnectionStateInterface;
//import py4j.GatewayServer; great old times...


		
		
		
		public class DetModelEntryPoint  {
			
			public Orion_Environment_Param detModel=new Orion_Environment_Param(0.0
			);
			
			
			public MonitorOfAspectOrionSystemConnStatusConn monitorOfAspectOrionSystemConnStatusConn=new MonitorOfAspectOrionSystemConnStatusConn();
			public MonitorOfEndConditionOrionSystemConnStatusConn monitorOfEndConditionOrionSystemConnStatusConn= new MonitorOfEndConditionOrionSystemConnStatusConn();
			
			
			public DetModelEntryPoint(){
				detModel.getSystemConnStatus().registerListener(monitorOfAspectOrionSystemConnStatusConn);
				detModel.getSystemConnStatus().registerListener(monitorOfEndConditionOrionSystemConnStatusConn);
			}
			
			public void reset(double timerMean
			) {
				/*
				detModel=null;
				System.gc();
				detModel=new Orion_Environment_Param(timerMean
				);
				detModel.getSystemConnStatus().registerListener(monitorOfAspectOrionSystemConnStatusConn);
				detModel.getSystemConnStatus().registerListener(monitorOfEndConditionOrionSystemConnStatusConn);
				*/
				monitorOfAspectOrionSystemConnStatusConn.reset();
				monitorOfEndConditionOrionSystemConnStatusConn.reset();
				detModel.reset();
			}
			
			
			public Orion_Environment_Param getDetModel(){
				return detModel;
			}
			
			public Orion_Environment_Param getOrion(){
				return detModel;
			}   
								
			
				public class MonitorOfAspectOrionSystemConnStatusConn implements ConnectionStateInterface.Listener.Provided {
							
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
							public void raiseConn(){
								state="stop";
								freq++;
							}
							
							@Override
							public void raiseDisconn(){
							}
							
				}
			
			
			
				public class MonitorOfEndConditionOrionSystemConnStatusConn implements ConnectionStateInterface.Listener.Provided {
							
							public String state="run";
							
									
							public void reset(){
								state="run";
							}
													
							@Override
							public void raiseConn(){
								state="stop";
							}
							
							@Override
							public void raiseDisconn(){
							}
							
				}
			
			
			
		}




