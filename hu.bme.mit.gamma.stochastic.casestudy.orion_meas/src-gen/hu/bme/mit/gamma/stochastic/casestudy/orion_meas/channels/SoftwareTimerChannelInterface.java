package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.SoftwareTimerInterface;

public interface SoftwareTimerChannelInterface {			
	
	void registerPort(SoftwareTimerInterface.Provided providedPort);
	
	void registerPort(SoftwareTimerInterface.Required requiredPort);

}
