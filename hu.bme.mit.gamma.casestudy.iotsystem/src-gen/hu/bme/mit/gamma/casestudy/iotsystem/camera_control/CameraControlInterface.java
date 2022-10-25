package hu.bme.mit.gamma.casestudy.iotsystem.camera_control;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public interface CameraControlInterface {

	public EventStreamInterface.Provided getRequests();
	public ImageStreamInterface.Required getDriverImages();
	public ImageStreamInterface.Provided getNetworkImages();
	
	void runCycle();
	void reset();

}
