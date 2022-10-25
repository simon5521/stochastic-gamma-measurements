package hu.bme.mit.gamma.casestudy.iotsystem_meas.network_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.ImageStreamInterface;

public interface NetworkAdapterInterface {
	
	ImageStreamInterface.Required getImageIn();
	ImageStreamInterface.Provided getImageOut();
	ImageStreamInterface.Required getImageLoss();
	
	void reset();
	
	void start();
	
}
