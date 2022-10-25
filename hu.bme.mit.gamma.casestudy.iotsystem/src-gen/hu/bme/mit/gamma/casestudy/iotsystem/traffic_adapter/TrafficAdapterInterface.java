package hu.bme.mit.gamma.casestudy.iotsystem.traffic_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.TrafficEventStreamInterface;

public interface TrafficAdapterInterface {
	
	EventStreamInterface.Provided getCarArrivesOut();
	TrafficEventStreamInterface.Provided getTrafficStream();
	EventStreamInterface.Required getCarArrives();
	EventStreamInterface.Required getCarLeaves();
	
	void reset();
	
	void start();
	
}
