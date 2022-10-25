package hu.bme.mit.gamma.casestudy.iotsystem.channels;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface;

public interface ImageStreamChannelInterface {			
	
	void registerPort(ImageStreamInterface.Provided providedPort);
	
	void registerPort(ImageStreamInterface.Required requiredPort);

}
