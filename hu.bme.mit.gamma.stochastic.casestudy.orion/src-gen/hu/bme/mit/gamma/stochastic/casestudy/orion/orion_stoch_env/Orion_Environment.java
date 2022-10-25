package hu.bme.mit.gamma.stochastic.casestudy.orion.orion_stoch_env;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_stoch_system.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_master_adapter.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_master_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_slave_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.channel.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.status_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.channels.*;


public class Orion_Environment implements Orion_EnvironmentInterface {
	// Component instances
	private Orion_Adapter orionSystem;
	// Environmental Component instances
	// Port instances
	private SystemConnStatus systemConnStatus = new SystemConnStatus();
	// Channel instances
	private SoftwareTimerChannelInterface channelTimeoutKeepAliveReceiveTimeout_4OfTimerKeepAliveReceiveTimeout_4;
	private SoftwareTimerChannelInterface channelTimeoutKapcsolodik_2OfTimerKapcsolodik_2;
	private SoftwareTimerChannelInterface channelTimeoutKeepAliveSendTimeout_1OfTimerKeepAliveSendTimeout_1;
	private SoftwareTimerChannelInterface channelTimeoutZarva_0OfTimerZarva_0;
	private SoftwareTimerChannelInterface channelTimoeutKeepAliveReceiveTimeout_3OfTimerKeepAliveReceiveTimeout_3;
	private SoftwareTimerChannelInterface channelTimeoutKapcsolodik_3OfTimerKapcsolodik_3;
	private SoftwareTimerChannelInterface channelTimeoutKeepAliveSendTimeout_0OfTimerKeepAliveSendTimeout_0;
	
	public boolean isEmpty(){
		return  orionSystem.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			orionSystem.schedule();
		}
	}
	
	public Orion_Environment() {
		orionSystem = new Orion_Adapter();
		systemConnStatus = new SystemConnStatus();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		orionSystem.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class SystemConnStatus implements ConnectionStateInterface.Provided {
	
		
		@Override
		public boolean isRaisedConn() {
			return orionSystem.getSystemStatus().isRaisedConn();
		}
		@Override
		public boolean isRaisedDisconn() {
			return orionSystem.getSystemStatus().isRaisedDisconn();
		}
		
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			orionSystem.getSystemStatus().registerListener(listener);
		}
		
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return orionSystem.getSystemStatus().getRegisteredListeners();
		}
		
	}
	
	@Override
	public SystemConnStatus getSystemConnStatus() {
		return systemConnStatus;
	}
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		orionSystem.start();
	}
	
	public boolean isWaiting() {
		return orionSystem.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public Orion_Adapter getOrionSystem() {
		return orionSystem;
	}
	
}
