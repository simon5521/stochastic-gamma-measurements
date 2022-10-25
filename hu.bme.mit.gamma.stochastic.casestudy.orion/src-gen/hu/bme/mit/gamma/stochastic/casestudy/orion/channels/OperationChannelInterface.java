package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.OperationInterface;

public interface OperationChannelInterface {			
	
	void registerPort(OperationInterface.Provided providedPort);
	
	void registerPort(OperationInterface.Required requiredPort);

}
