package hu.bme.mit.gamma.stochstic.casestudy.dualgps.dualgps_cond;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;

public interface ParametricDualGPSInterface {
	
	SensorInterface.Provided getCommunication();
	
	void reset();
	
	void start();
	
}
