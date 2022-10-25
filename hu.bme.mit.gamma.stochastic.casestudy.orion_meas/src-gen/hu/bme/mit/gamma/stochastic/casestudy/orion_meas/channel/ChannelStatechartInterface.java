package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channel;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public interface ChannelStatechartInterface {

	public StateMachine_Interface_For_OrionInterface.Provided getOutput();
	public StateMachine_Interface_For_OrionInterface.Required getInput();
	
	void runCycle();
	void reset();

}
