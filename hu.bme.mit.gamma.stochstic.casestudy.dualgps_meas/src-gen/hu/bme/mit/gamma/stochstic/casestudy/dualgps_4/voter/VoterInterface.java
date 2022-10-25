package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;

public interface VoterInterface {

	public HardwareFailureInterface.Required getFaults();
	public SensorInterface.Required getSensor();
	public SensorInterface.Provided getCommunication();
	
	void runCycle();
	void reset();

}
