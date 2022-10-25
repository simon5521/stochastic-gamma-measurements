package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.StateMachine_Interface_For_OrionInterface;

public interface StateMachine_Interface_For_OrionChannelInterface {			
	
	void registerPort(StateMachine_Interface_For_OrionInterface.Provided providedPort);
	
	void registerPort(StateMachine_Interface_For_OrionInterface.Required requiredPort);

}
