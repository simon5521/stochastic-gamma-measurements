package hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;

public interface RoadTrafficInterface {
	
	TrafficEventStreamInterface.Provided getCars();
	
	void reset();
	
	void start();
	
}
