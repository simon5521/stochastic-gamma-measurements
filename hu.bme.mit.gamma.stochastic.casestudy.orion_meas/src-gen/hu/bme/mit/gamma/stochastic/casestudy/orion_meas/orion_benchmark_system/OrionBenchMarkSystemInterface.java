package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_benchmark_system;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;

public interface OrionBenchMarkSystemInterface {
	
	ConnectionStateInterface.Provided getSystemConnStatus();
	
	void reset();
	
	void start();
	
}
