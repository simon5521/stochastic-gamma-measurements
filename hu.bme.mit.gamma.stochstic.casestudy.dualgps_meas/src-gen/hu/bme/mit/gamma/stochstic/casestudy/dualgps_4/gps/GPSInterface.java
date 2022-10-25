package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gps;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;

public interface GPSInterface {

	public HardwareFailureInterface.Required getFaults();
	public SensorInterface.Provided getCommunication();
	
	void runCycle();
	void reset();

}
