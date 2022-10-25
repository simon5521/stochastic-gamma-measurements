package hu.bme.mit.gamma.stochstic.casestudy.dualgps.channels;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.HardwareFailureInterface;
import java.util.List;
import java.util.LinkedList;

public class HardwareFailureChannel implements HardwareFailureChannelInterface {
	
	private HardwareFailureInterface.Provided providedPort;
	private List<HardwareFailureInterface.Required> requiredPorts = new LinkedList<HardwareFailureInterface.Required>();
	
	public HardwareFailureChannel() {}
	
	public HardwareFailureChannel(HardwareFailureInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(HardwareFailureInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (HardwareFailureInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(HardwareFailureInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
