package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.SoftwareTimerInterface;
import java.util.List;
import java.util.LinkedList;

public class SoftwareTimerChannel implements SoftwareTimerChannelInterface {
	
	private SoftwareTimerInterface.Provided providedPort;
	private List<SoftwareTimerInterface.Required> requiredPorts = new LinkedList<SoftwareTimerInterface.Required>();
	
	public SoftwareTimerChannel() {}
	
	public SoftwareTimerChannel(SoftwareTimerInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(SoftwareTimerInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (SoftwareTimerInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(SoftwareTimerInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
