package hu.bme.mit.gamma.casestudy.iotsystem.cloud;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public interface EdgeInterface {

	public ImageStreamInterface.Required getCamera();
	public TrafficEventStreamInterface.Required getTrafficStream();
	public EventStreamInterface.Provided getLostImage();
	public EventStreamInterface.Provided getCarLeave();
	
	void runCycle();
	void reset();

}
