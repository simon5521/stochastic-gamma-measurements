package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.Connection_Interface_For_OrionInterface;
import java.util.List;
import java.util.LinkedList;

public class Connection_Interface_For_OrionChannel implements Connection_Interface_For_OrionChannelInterface {
	
	private Connection_Interface_For_OrionInterface.Provided providedPort;
	private List<Connection_Interface_For_OrionInterface.Required> requiredPorts = new LinkedList<Connection_Interface_For_OrionInterface.Required>();
	
	public Connection_Interface_For_OrionChannel() {}
	
	public Connection_Interface_For_OrionChannel(Connection_Interface_For_OrionInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(Connection_Interface_For_OrionInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (Connection_Interface_For_OrionInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(Connection_Interface_For_OrionInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
