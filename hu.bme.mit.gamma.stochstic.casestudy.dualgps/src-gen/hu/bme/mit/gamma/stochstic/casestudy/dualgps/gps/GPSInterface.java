package hu.bme.mit.gamma.stochstic.casestudy.dualgps.gps;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*;

public interface GPSInterface {

	public HardwareFailureInterface.Required getFaults();
	public SensorInterface.Provided getCommunication();
	
	void runCycle();
	void reset();

}
