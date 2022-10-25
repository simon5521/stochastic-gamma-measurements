package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_benchmark_system;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channel.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_env.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_system.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_adapter.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_slave_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels.*;


public class OrionBenchMarkSystem implements OrionBenchMarkSystemInterface {
	// Component instances
	private Orion_Environment subSystem1_;
	private Orion_Environment subSystem2_;
	private Orion_Environment subSystem3_;
	private Orion_Environment subSystem4_;
	private Orion_Environment subSystem5_;
	private Orion_Environment subSystem6_;
	private Orion_Environment subSystem7_;
	private Orion_Environment subSystem8_;
	private Orion_Environment subSystem9_;
	private Orion_Environment subSystem10;
	private Orion_Environment subSystem11;
	private Orion_Environment subSystem12;
	private Orion_Environment subSystem13;
	private Orion_Environment subSystem14;
	private Orion_Environment subSystem15;
	private Orion_Environment subSystem16;
	// Environmental Component instances
	// Port instances
	private SystemConnStatus systemConnStatus = new SystemConnStatus();
	// Channel instances
	
	public boolean isEmpty(){
		return  subSystem1_.isEmpty()  &&  subSystem2_.isEmpty()  &&  subSystem3_.isEmpty()  &&  subSystem4_.isEmpty()  &&  subSystem5_.isEmpty()  &&  subSystem6_.isEmpty()  &&  subSystem7_.isEmpty()  &&  subSystem8_.isEmpty()  &&  subSystem9_.isEmpty()  &&  subSystem10.isEmpty()  &&  subSystem11.isEmpty()  &&  subSystem12.isEmpty()  &&  subSystem13.isEmpty()  &&  subSystem14.isEmpty()  &&  subSystem15.isEmpty()  &&  subSystem16.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			subSystem1_.schedule();
			subSystem2_.schedule();
			subSystem3_.schedule();
			subSystem4_.schedule();
			subSystem5_.schedule();
			subSystem6_.schedule();
			subSystem7_.schedule();
			subSystem8_.schedule();
			subSystem9_.schedule();
			subSystem10.schedule();
			subSystem11.schedule();
			subSystem12.schedule();
			subSystem13.schedule();
			subSystem14.schedule();
			subSystem15.schedule();
			subSystem16.schedule();
		}
	}
	
	public OrionBenchMarkSystem() {
		subSystem1_ = new Orion_Environment();
		subSystem2_ = new Orion_Environment();
		subSystem3_ = new Orion_Environment();
		subSystem4_ = new Orion_Environment();
		subSystem5_ = new Orion_Environment();
		subSystem6_ = new Orion_Environment();
		subSystem7_ = new Orion_Environment();
		subSystem8_ = new Orion_Environment();
		subSystem9_ = new Orion_Environment();
		subSystem10 = new Orion_Environment();
		subSystem11 = new Orion_Environment();
		subSystem12 = new Orion_Environment();
		subSystem13 = new Orion_Environment();
		subSystem14 = new Orion_Environment();
		subSystem15 = new Orion_Environment();
		subSystem16 = new Orion_Environment();
		systemConnStatus = new SystemConnStatus();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		subSystem1_.reset();
		subSystem2_.reset();
		subSystem3_.reset();
		subSystem4_.reset();
		subSystem5_.reset();
		subSystem6_.reset();
		subSystem7_.reset();
		subSystem8_.reset();
		subSystem9_.reset();
		subSystem10.reset();
		subSystem11.reset();
		subSystem12.reset();
		subSystem13.reset();
		subSystem14.reset();
		subSystem15.reset();
		subSystem16.reset();
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
			return subSystem1_.getSystemConnStatus().isRaisedConn();
		}
		@Override
		public boolean isRaisedDisconn() {
			return subSystem1_.getSystemConnStatus().isRaisedDisconn();
		}
		
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			subSystem1_.getSystemConnStatus().registerListener(listener);
		}
		
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return subSystem1_.getSystemConnStatus().getRegisteredListeners();
		}
		
	}
	
	@Override
	public SystemConnStatus getSystemConnStatus() {
		return systemConnStatus;
	}
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		subSystem1_.start();
		subSystem2_.start();
		subSystem3_.start();
		subSystem4_.start();
		subSystem5_.start();
		subSystem6_.start();
		subSystem7_.start();
		subSystem8_.start();
		subSystem9_.start();
		subSystem10.start();
		subSystem11.start();
		subSystem12.start();
		subSystem13.start();
		subSystem14.start();
		subSystem15.start();
		subSystem16.start();
	}
	
	public boolean isWaiting() {
		return subSystem1_.isWaiting() && subSystem2_.isWaiting() && subSystem3_.isWaiting() && subSystem4_.isWaiting() && subSystem5_.isWaiting() && subSystem6_.isWaiting() && subSystem7_.isWaiting() && subSystem8_.isWaiting() && subSystem9_.isWaiting() && subSystem10.isWaiting() && subSystem11.isWaiting() && subSystem12.isWaiting() && subSystem13.isWaiting() && subSystem14.isWaiting() && subSystem15.isWaiting() && subSystem16.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public Orion_Environment getSubSystem1_() {
		return subSystem1_;
	}
	
	public Orion_Environment getSubSystem2_() {
		return subSystem2_;
	}
	
	public Orion_Environment getSubSystem3_() {
		return subSystem3_;
	}
	
	public Orion_Environment getSubSystem4_() {
		return subSystem4_;
	}
	
	public Orion_Environment getSubSystem5_() {
		return subSystem5_;
	}
	
	public Orion_Environment getSubSystem6_() {
		return subSystem6_;
	}
	
	public Orion_Environment getSubSystem7_() {
		return subSystem7_;
	}
	
	public Orion_Environment getSubSystem8_() {
		return subSystem8_;
	}
	
	public Orion_Environment getSubSystem9_() {
		return subSystem9_;
	}
	
	public Orion_Environment getSubSystem10() {
		return subSystem10;
	}
	
	public Orion_Environment getSubSystem11() {
		return subSystem11;
	}
	
	public Orion_Environment getSubSystem12() {
		return subSystem12;
	}
	
	public Orion_Environment getSubSystem13() {
		return subSystem13;
	}
	
	public Orion_Environment getSubSystem14() {
		return subSystem14;
	}
	
	public Orion_Environment getSubSystem15() {
		return subSystem15;
	}
	
	public Orion_Environment getSubSystem16() {
		return subSystem16;
	}
	
}
