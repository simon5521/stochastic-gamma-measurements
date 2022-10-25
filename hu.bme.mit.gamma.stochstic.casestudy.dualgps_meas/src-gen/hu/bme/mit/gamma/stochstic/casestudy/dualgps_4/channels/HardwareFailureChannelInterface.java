package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.HardwareFailureInterface;

public interface HardwareFailureChannelInterface {			
	
	void registerPort(HardwareFailureInterface.Provided providedPort);
	
	void registerPort(HardwareFailureInterface.Required requiredPort);

}
