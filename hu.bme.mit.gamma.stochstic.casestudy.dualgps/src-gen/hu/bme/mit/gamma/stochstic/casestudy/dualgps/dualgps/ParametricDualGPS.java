package hu.bme.mit.gamma.stochstic.casestudy.dualgps.dualgps;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.gpsadapter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.gps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.voteradapter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.channels.*;


public class ParametricDualGPS implements ParametricDualGPSInterface {
	// Component instances
	private GPSasync GPS1;
	private GPSasync GPS2;
	private Voterasync Voter;
	// Environmental Component instances
	// Port instances
	private Communication communication = new Communication();
	// Channel instances
	private SensorChannelInterface channelCommunicationOfGPS1;
	private HardwareFailureChannelInterface channelFaultsOfGPS1_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS2_Failure;
	private SensorChannelInterface channelCommunicationOfGPS2;
	private HardwareFailureChannelInterface channelFaultsOfVoter_Failure;
	// Fields representing parameters
	private final double voterFailureRate;
	
	public boolean isEmpty(){
		return  GPS1.isEmpty()  &&  GPS2.isEmpty()  &&  Voter.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			GPS1.schedule();
			GPS2.schedule();
			Voter.schedule();
		}
	}
	
	public ParametricDualGPS(double voterFailureRate) {
		this.voterFailureRate = voterFailureRate;
		GPS1 = new GPSasync();
		GPS2 = new GPSasync();
		Voter = new Voterasync();
		communication = new Communication();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		GPS1.reset();
		GPS2.reset();
		Voter.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		channelCommunicationOfGPS2 = new SensorChannel(GPS2.getCommunication());
		channelCommunicationOfGPS2.registerPort(Voter.getSensor2());
		channelCommunicationOfGPS1 = new SensorChannel(GPS1.getCommunication());
		channelCommunicationOfGPS1.registerPort(Voter.getSensor1());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class Communication implements SensorInterface.Provided {
	
		
		@Override
		public boolean isRaisedFailstop() {
			return Voter.getCommunication().isRaisedFailstop();
		}
		
		@Override
		public void registerListener(SensorInterface.Listener.Provided listener) {
			Voter.getCommunication().registerListener(listener);
		}
		
		@Override
		public List<SensorInterface.Listener.Provided> getRegisteredListeners() {
			return Voter.getCommunication().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Communication getCommunication() {
		return communication;
	}
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		GPS1.start();
		GPS2.start();
		Voter.start();
	}
	
	public boolean isWaiting() {
		return GPS1.isWaiting() && GPS2.isWaiting() && Voter.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public GPSasync getGPS1() {
		return GPS1;
	}
	
	public GPSasync getGPS2() {
		return GPS2;
	}
	
	public Voterasync getVoter() {
		return Voter;
	}
	
}
