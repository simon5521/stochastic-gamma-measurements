package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gpsadapter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.HardwareFailureInterface;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.SensorInterface;

public interface GPSasyncInterface {
	
	SensorInterface.Provided getCommunication();
	HardwareFailureInterface.Required getFaults();
	
	void reset();
	
	void start();
	
}
