package hu.bme.mit.gamma.casestudy.iotsystem_meas.network;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;

public interface NetworkInterface {

	public ImageStreamInterface.Required getImageIn();
	public ImageStreamInterface.Provided getImageOut();
	public ImageStreamInterface.Required getImageLoss();
	
	void runCycle();
	void reset();

}
