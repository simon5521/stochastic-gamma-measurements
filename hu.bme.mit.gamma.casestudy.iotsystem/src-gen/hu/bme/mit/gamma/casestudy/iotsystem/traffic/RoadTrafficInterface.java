package hu.bme.mit.gamma.casestudy.iotsystem.traffic;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.TrafficEventStreamInterface;

public interface RoadTrafficInterface {
	
	TrafficEventStreamInterface.Provided getCars();
	
	void reset();
	
	void start();
	
}
