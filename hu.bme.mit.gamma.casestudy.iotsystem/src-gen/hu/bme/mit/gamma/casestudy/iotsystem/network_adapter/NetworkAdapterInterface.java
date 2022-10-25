package hu.bme.mit.gamma.casestudy.iotsystem.network_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface;

public interface NetworkAdapterInterface {
	
	ImageStreamInterface.Required getImageIn();
	ImageStreamInterface.Provided getImageOut();
	ImageStreamInterface.Required getImageLoss();
	
	void reset();
	
	void start();
	
}
