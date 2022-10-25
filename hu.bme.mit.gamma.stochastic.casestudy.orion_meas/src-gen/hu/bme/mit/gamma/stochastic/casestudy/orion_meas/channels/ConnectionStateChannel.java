package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;
import java.util.List;
import java.util.LinkedList;

public class ConnectionStateChannel implements ConnectionStateChannelInterface {
	
	private ConnectionStateInterface.Provided providedPort;
	private List<ConnectionStateInterface.Required> requiredPorts = new LinkedList<ConnectionStateInterface.Required>();
	
	public ConnectionStateChannel() {}
	
	public ConnectionStateChannel(ConnectionStateInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(ConnectionStateInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (ConnectionStateInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(ConnectionStateInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
