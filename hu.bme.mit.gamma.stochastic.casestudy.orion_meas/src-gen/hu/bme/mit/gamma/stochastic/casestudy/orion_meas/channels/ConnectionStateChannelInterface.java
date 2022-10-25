package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;

public interface ConnectionStateChannelInterface {			
	
	void registerPort(ConnectionStateInterface.Provided providedPort);
	
	void registerPort(ConnectionStateInterface.Required requiredPort);

}
