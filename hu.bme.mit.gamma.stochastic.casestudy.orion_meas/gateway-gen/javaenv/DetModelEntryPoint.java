package javaenv;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_benchmark_system.OrionBenchMarkSystem;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;
//import py4j.GatewayServer; great old times...


		
		
		
		public class DetModelEntryPoint  {
			
			public OrionBenchMarkSystem detModel=new OrionBenchMarkSystem();
			
			
			public MonitorOfAspectOrionSystemConnStatusConn monitorOfAspectOrionSystemConnStatusConn=new MonitorOfAspectOrionSystemConnStatusConn();
			public MonitorOfEndConditionOrionSystemConnStatusConn monitorOfEndConditionOrionSystemConnStatusConn= new MonitorOfEndConditionOrionSystemConnStatusConn();
			
			
			public DetModelEntryPoint(){
				detModel.getSystemConnStatus().registerListener(monitorOfAspectOrionSystemConnStatusConn);
				detModel.getSystemConnStatus().registerListener(monitorOfEndConditionOrionSystemConnStatusConn);
			}
			
			public void reset() {
				/*
				detModel=null;
				System.gc();
				detModel=new OrionBenchMarkSystem();
				detModel.getSystemConnStatus().registerListener(monitorOfAspectOrionSystemConnStatusConn);
				detModel.getSystemConnStatus().registerListener(monitorOfEndConditionOrionSystemConnStatusConn);
				*/
				monitorOfAspectOrionSystemConnStatusConn.reset();
				monitorOfEndConditionOrionSystemConnStatusConn.reset();
				detModel.reset();
			}
			
			
			public OrionBenchMarkSystem getDetModel(){
				return detModel;
			}
			
			public OrionBenchMarkSystem getOrion(){
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




