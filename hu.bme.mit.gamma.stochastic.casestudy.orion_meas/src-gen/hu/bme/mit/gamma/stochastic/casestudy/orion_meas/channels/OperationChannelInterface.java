package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.OperationInterface;

public interface OperationChannelInterface {			
	
	void registerPort(OperationInterface.Provided providedPort);
	
	void registerPort(OperationInterface.Required requiredPort);

}
