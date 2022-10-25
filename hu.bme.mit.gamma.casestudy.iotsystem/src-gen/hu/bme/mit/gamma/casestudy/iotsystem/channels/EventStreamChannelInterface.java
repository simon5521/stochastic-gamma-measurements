package hu.bme.mit.gamma.casestudy.iotsystem.channels;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface;

public interface EventStreamChannelInterface {			
	
	void registerPort(EventStreamInterface.Provided providedPort);
	
	void registerPort(EventStreamInterface.Required requiredPort);

}
