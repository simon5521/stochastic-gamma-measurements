package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_driver;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;

public interface CameraDriverInterface {

	public TrafficEventStreamInterface.Required getTraffic();
	public EventStreamInterface.Required getRequests();
	public ImageStreamInterface.Provided getImages();
	
	void runCycle();
	void reset();

}
