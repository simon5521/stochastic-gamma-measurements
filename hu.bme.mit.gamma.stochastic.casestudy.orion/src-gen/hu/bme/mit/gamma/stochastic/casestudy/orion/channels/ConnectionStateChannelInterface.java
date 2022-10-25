package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.ConnectionStateInterface;

public interface ConnectionStateChannelInterface {			
	
	void registerPort(ConnectionStateInterface.Provided providedPort);
	
	void registerPort(ConnectionStateInterface.Required requiredPort);

}
