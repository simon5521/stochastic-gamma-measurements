package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voteradapter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.HardwareFailureInterface;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.SensorInterface;

public interface VoterasyncInterface {
	
	SensorInterface.Provided getCommunication();
	SensorInterface.Required getSensor();
	HardwareFailureInterface.Required getFaults();
	
	void reset();
	
	void start();
	
}
