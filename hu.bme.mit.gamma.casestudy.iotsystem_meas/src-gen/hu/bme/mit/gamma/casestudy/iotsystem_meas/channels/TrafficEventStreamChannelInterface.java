package hu.bme.mit.gamma.casestudy.iotsystem_meas.channels;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;

public interface TrafficEventStreamChannelInterface {			
	
	void registerPort(TrafficEventStreamInterface.Provided providedPort);
	
	void registerPort(TrafficEventStreamInterface.Required requiredPort);

}
