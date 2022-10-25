package hu.bme.mit.gamma.casestudy.iotsystem_meas.channels;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.TrafficEventStreamInterface;
import java.util.List;
import java.util.LinkedList;

public class TrafficEventStreamChannel implements TrafficEventStreamChannelInterface {
	
	private TrafficEventStreamInterface.Provided providedPort;
	private List<TrafficEventStreamInterface.Required> requiredPorts = new LinkedList<TrafficEventStreamInterface.Required>();
	
	public TrafficEventStreamChannel() {}
	
	public TrafficEventStreamChannel(TrafficEventStreamInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(TrafficEventStreamInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (TrafficEventStreamInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(TrafficEventStreamInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
