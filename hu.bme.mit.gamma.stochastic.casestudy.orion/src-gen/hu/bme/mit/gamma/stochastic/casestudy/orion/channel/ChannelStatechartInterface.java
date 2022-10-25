package hu.bme.mit.gamma.stochastic.casestudy.orion.channel;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*;

public interface ChannelStatechartInterface {

	public StateMachine_Interface_For_OrionInterface.Provided getOutput();
	public StateMachine_Interface_For_OrionInterface.Required getInput();
	
	void runCycle();
	void reset();

}
