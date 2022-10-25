package hu.bme.mit.gamma.casestudy.iotsystem.camera_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.TrafficEventStreamInterface;

public interface CameraAdapterInterface {
	
	EventStreamInterface.Required getSoftwareTimer();
	ImageStreamInterface.Provided getImages();
	TrafficEventStreamInterface.Required getTrafficSensing();
	
	void reset();
	
	void start();
	
}
