package hu.bme.mit.gamma.stochstic.casestudy.dualgps.gpsadapter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.HardwareFailureInterface;

public interface GPSasyncInterface {
	
	HardwareFailureInterface.Required getFaults();
	SensorInterface.Provided getCommunication();
	
	void reset();
	
	void start();
	
}
