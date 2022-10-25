package hu.bme.mit.gamma.stochstic.casestudy.dualgps.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;

public interface SensorChannelInterface {			
	
	void registerPort(SensorInterface.Provided providedPort);
	
	void registerPort(SensorInterface.Required requiredPort);

}
