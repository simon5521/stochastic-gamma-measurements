package hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;

public interface TrafficAdapterInterface {
	
	TrafficEventStreamInterface.Provided getTrafficStream();
	EventStreamInterface.Required getCarArrives();
	EventStreamInterface.Required getCarLeaves();
	EventStreamInterface.Provided getCarArrivesOut();
	
	void reset();
	
	void start();
	
}
