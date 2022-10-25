package hu.bme.mit.gamma.stochstic.casestudy.dualgps.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.HardwareFailureInterface;

public interface HardwareFailureChannelInterface {			
	
	void registerPort(HardwareFailureInterface.Provided providedPort);
	
	void registerPort(HardwareFailureInterface.Required requiredPort);

}
