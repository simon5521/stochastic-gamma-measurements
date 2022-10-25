package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_env;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;

public interface Orion_EnvironmentInterface {
	
	ConnectionStateInterface.Provided getSystemConnStatus();
	
	void reset();
	
	void start();
	
}
