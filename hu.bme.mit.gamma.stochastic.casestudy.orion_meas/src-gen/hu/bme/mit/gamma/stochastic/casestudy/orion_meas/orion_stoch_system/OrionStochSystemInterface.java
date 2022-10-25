package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_system;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.SoftwareTimerInterface;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;

public interface OrionStochSystemInterface {
	
	SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_0();
	SoftwareTimerInterface.Required getTimeoutZarva_0();
	ConnectionStateInterface.Provided getSystemStatus();
	SoftwareTimerInterface.Required getTimeoutKeepAliveReceiveTimeout_4();
	SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_1();
	SoftwareTimerInterface.Required getTimeoutKapcsolodik_3();
	SoftwareTimerInterface.Required getTimeoutKapcsolodik_2();
	SoftwareTimerInterface.Required getTimoeutKeepAliveReceiveTimeout_3();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
