package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_software;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface;

public interface CameraSoftwareInterface {
	
	ImageStreamInterface.Provided getImages();
	TrafficEventStreamInterface.Required getTrafficSensing();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
