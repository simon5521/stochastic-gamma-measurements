package hu.bme.mit.gamma.casestudy.iotsystem.traffic_param;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.TrafficEventStreamInterface;

public interface RoadTrafficParametricInterface {
	
	TrafficEventStreamInterface.Provided getCars();
	
	void reset();
	
	void start();
	
}
