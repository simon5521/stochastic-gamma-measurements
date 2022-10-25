package hu.bme.mit.gamma.stochstic.casestudy.dualgps.voteradapter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.HardwareFailureInterface;

public interface VoterasyncInterface {
	
	SensorInterface.Provided getCommunication();
	SensorInterface.Required getSensor1();
	SensorInterface.Required getSensor2();
	HardwareFailureInterface.Required getFaults();
	
	void reset();
	
	void start();
	
}
