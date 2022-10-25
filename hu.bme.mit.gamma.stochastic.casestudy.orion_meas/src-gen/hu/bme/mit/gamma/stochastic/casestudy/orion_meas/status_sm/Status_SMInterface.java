package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public interface Status_SMInterface {

	public ConnectionStateInterface.Required getSlaveStatus();
	public ConnectionStateInterface.Required getMasterStatus();
	public ConnectionStateInterface.Provided getSystemStatus();
	
	void runCycle();
	void reset();

}
