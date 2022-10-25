package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.SensorInterface;

public interface SensorChannelInterface {			
	
	void registerPort(SensorInterface.Provided providedPort);
	
	void registerPort(SensorInterface.Required requiredPort);

}
