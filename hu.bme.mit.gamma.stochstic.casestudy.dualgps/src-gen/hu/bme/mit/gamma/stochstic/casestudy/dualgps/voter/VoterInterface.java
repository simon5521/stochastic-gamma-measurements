package hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*;

public interface VoterInterface {

	public HardwareFailureInterface.Required getFaults();
	public SensorInterface.Required getSensor1();
	public SensorInterface.Required getSensor2();
	public SensorInterface.Provided getCommunication();
	
	void runCycle();
	void reset();

}
