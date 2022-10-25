package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.Block_Interface_ForOrionInterface;

public interface Block_Interface_ForOrionChannelInterface {			
	
	void registerPort(Block_Interface_ForOrionInterface.Provided providedPort);
	
	void registerPort(Block_Interface_ForOrionInterface.Required requiredPort);

}
