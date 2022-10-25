package hu.bme.mit.gamma.casestudy.iotsystem.channels;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.ImageStreamInterface;
import java.util.List;
import java.util.LinkedList;

public class ImageStreamChannel implements ImageStreamChannelInterface {
	
	private ImageStreamInterface.Provided providedPort;
	private List<ImageStreamInterface.Required> requiredPorts = new LinkedList<ImageStreamInterface.Required>();
	
	public ImageStreamChannel() {}
	
	public ImageStreamChannel(ImageStreamInterface.Provided providedPort) {
		this.providedPort = providedPort;
	}
	
	public void registerPort(ImageStreamInterface.Provided providedPort) {
		// Former port is forgotten
		this.providedPort = providedPort;
		// Registering the listeners
		for (ImageStreamInterface.Required requiredPort : requiredPorts) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}
	
	public void registerPort(ImageStreamInterface.Required requiredPort) {
		requiredPorts.add(requiredPort);
		// Checking whether a provided port is already given
		if (providedPort != null) {
			providedPort.registerListener(requiredPort);
			requiredPort.registerListener(providedPort);
		}
	}

}
