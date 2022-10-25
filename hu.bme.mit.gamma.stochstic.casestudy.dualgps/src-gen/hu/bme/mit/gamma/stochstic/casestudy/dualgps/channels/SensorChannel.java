package hu.bme.mit.gamma.stochstic.casestudy.dualgps.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.SensorInterface;
import java.util.List;
import java.util.LinkedList;

public class SensorChannel implements SensorChannelInterface {
	
	private SensorInterface.Provided providedPort;
	private List<SensorInterface.Required> requiredPorts = new LinkedList<SensorInterface.Required>();
	
	public SensorChannel() {}
	
	public SensorChannel(SensorInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(SensorInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (SensorInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(SensorInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
