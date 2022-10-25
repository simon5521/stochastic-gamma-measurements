package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.Connection_Interface_For_OrionInterface;

public interface Connection_Interface_For_OrionChannelInterface {			
	
	void registerPort(Connection_Interface_For_OrionInterface.Provided providedPort);
	
	void registerPort(Connection_Interface_For_OrionInterface.Required requiredPort);

}
