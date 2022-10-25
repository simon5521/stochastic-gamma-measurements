package hu.bme.mit.gamma.casestudy.iotsystem_meas.channels;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface;

public interface ImageStreamChannelInterface {			
	
	void registerPort(ImageStreamInterface.Provided providedPort);
	
	void registerPort(ImageStreamInterface.Required requiredPort);

}
