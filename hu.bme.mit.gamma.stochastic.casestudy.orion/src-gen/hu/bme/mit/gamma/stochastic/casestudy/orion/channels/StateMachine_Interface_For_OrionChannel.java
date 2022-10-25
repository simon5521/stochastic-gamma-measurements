package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.StateMachine_Interface_For_OrionInterface;
import java.util.List;
import java.util.LinkedList;

public class StateMachine_Interface_For_OrionChannel implements StateMachine_Interface_For_OrionChannelInterface {
	
	private StateMachine_Interface_For_OrionInterface.Provided providedPort;
	private List<StateMachine_Interface_For_OrionInterface.Required> requiredPorts = new LinkedList<StateMachine_Interface_For_OrionInterface.Required>();
	
	public StateMachine_Interface_For_OrionChannel() {}
	
	public StateMachine_Interface_For_OrionChannel(StateMachine_Interface_For_OrionInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(StateMachine_Interface_For_OrionInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (StateMachine_Interface_For_OrionInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(StateMachine_Interface_For_OrionInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
