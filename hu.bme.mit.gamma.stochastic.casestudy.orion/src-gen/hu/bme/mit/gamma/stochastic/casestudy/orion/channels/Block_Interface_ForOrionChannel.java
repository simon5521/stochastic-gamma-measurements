package hu.bme.mit.gamma.stochastic.casestudy.orion.channels;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.Block_Interface_ForOrionInterface;
import java.util.List;
import java.util.LinkedList;

public class Block_Interface_ForOrionChannel implements Block_Interface_ForOrionChannelInterface {
	
	private Block_Interface_ForOrionInterface.Provided providedPort;
	private List<Block_Interface_ForOrionInterface.Required> requiredPorts = new LinkedList<Block_Interface_ForOrionInterface.Required>();
	
	public Block_Interface_ForOrionChannel() {}
	
	public Block_Interface_ForOrionChannel(Block_Interface_ForOrionInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(Block_Interface_ForOrionInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (Block_Interface_ForOrionInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(Block_Interface_ForOrionInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
