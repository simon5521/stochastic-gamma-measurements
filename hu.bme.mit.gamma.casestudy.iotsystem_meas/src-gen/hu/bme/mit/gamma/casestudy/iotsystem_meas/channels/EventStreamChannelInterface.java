package hu.bme.mit.gamma.casestudy.iotsystem_meas.channels;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface;

public interface EventStreamChannelInterface {			
	
	void registerPort(EventStreamInterface.Provided providedPort);
	
	void registerPort(EventStreamInterface.Required requiredPort);

}
