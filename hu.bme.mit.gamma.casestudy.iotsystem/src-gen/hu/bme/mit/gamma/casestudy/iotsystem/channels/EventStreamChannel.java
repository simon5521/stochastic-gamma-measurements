package hu.bme.mit.gamma.casestudy.iotsystem.channels;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.EventStreamInterface;
import java.util.List;
import java.util.LinkedList;

public class EventStreamChannel implements EventStreamChannelInterface {
	
	private EventStreamInterface.Provided providedPort;
	private List<EventStreamInterface.Required> requiredPorts = new LinkedList<EventStreamInterface.Required>();
	
	public EventStreamChannel() {}
	
	public EventStreamChannel(EventStreamInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(EventStreamInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (EventStreamInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(EventStreamInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
