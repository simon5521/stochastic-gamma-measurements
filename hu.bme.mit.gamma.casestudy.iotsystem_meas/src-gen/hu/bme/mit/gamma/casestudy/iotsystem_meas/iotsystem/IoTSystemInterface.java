package hu.bme.mit.gamma.casestudy.iotsystem_meas.iotsystem;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface;

public interface IoTSystemInterface {
	
	EventStreamInterface.Provided getFailures();
	
	void reset();
	
	void start();
	
}
