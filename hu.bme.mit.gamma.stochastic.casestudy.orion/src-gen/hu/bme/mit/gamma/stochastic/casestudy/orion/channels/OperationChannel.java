package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.OperationInterface;
import java.util.List;
import java.util.LinkedList;

public class OperationChannel implements OperationChannelInterface {
	
	private OperationInterface.Provided providedPort;
	private List<OperationInterface.Required> requiredPorts = new LinkedList<OperationInterface.Required>();
	
	public OperationChannel() {}
	
	public OperationChannel(OperationInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(OperationInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (OperationInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(OperationInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
