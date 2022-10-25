package hu.bme.mit.gamma.casestudy.iotsystem_meas.cloud;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;

public interface EdgeInterface {

	public ImageStreamInterface.Required getCamera();
	public TrafficEventStreamInterface.Required getTrafficStream();
	public EventStreamInterface.Provided getLostImage();
	
	void runCycle();
	void reset();

}
