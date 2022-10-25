package hu.bme.mit.gamma.stochstic.casestudy.dualgps.dualgps;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;

public interface DualGPSInterface {
	
	SensorInterface.Provided getCommunication();
	
	void reset();
	
	void start();
	
}
