package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.dualgps;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.SensorInterface;

public interface DualGPSInterface {
	
	SensorInterface.Provided getCommunication();
	
	void reset();
	
	void start();
	
}
