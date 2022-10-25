package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.EventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface;

public interface CameraAdapterInterface {
	
	ImageStreamInterface.Provided getImages();
	EventStreamInterface.Required getSoftwareTimer();
	TrafficEventStreamInterface.Required getTrafficSensing();
	
	void reset();
	
	void start();
	
}
