package hu.bme.mit.gamma.casestudy.iotsystem_meas.edge_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface;

public interface EdgeAdapterInterface {
	
	TrafficEventStreamInterface.Required getTrafficStream();
	EventStreamInterface.Provided getLostImage();
	ImageStreamInterface.Required getCamera();
	
	void reset();
	
	void start();
	
}
