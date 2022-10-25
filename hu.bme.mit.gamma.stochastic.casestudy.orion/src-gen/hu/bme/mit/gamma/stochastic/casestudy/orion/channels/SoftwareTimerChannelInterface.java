package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.SoftwareTimerInterface;

public interface SoftwareTimerChannelInterface {			
	
	void registerPort(SoftwareTimerInterface.Provided providedPort);
	
	void registerPort(SoftwareTimerInterface.Required requiredPort);

}
