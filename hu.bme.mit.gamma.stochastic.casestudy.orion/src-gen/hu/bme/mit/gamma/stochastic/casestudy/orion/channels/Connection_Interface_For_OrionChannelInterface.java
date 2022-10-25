package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.Connection_Interface_For_OrionInterface;

public interface Connection_Interface_For_OrionChannelInterface {			
	
	void registerPort(Connection_Interface_For_OrionInterface.Provided providedPort);
	
	void registerPort(Connection_Interface_For_OrionInterface.Required requiredPort);

}
