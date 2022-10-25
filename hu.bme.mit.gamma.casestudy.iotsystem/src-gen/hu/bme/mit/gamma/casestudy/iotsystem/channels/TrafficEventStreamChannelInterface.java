package hu.bme.mit.gamma.casestudy.iotsystem.channels;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.TrafficEventStreamInterface;

public interface TrafficEventStreamChannelInterface {			
	
	void registerPort(TrafficEventStreamInterface.Provided providedPort);
	
	void registerPort(TrafficEventStreamInterface.Required requiredPort);

}
