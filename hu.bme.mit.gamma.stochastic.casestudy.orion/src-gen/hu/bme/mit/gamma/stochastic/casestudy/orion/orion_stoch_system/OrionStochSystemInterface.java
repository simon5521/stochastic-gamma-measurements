package hu.bme.mit.gamma.stochastic.casestudy.orion.orion_stoch_system;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.SoftwareTimerInterface;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.ConnectionStateInterface;

public interface OrionStochSystemInterface {
	
	SoftwareTimerInterface.Required getTimeoutKapcsolodik_2();
	SoftwareTimerInterface.Required getTimeoutZarva_0();
	ConnectionStateInterface.Provided getSystemStatus();
	SoftwareTimerInterface.Required getTimoeutKeepAliveReceiveTimeout_3();
	SoftwareTimerInterface.Required getTimeoutKeepAliveReceiveTimeout_4();
	SoftwareTimerInterface.Required getTimeoutKapcsolodik_3();
	SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_0();
	SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_1();
	
	void reset();
	
	void runCycle();
	void runFullCycle();
	
}
