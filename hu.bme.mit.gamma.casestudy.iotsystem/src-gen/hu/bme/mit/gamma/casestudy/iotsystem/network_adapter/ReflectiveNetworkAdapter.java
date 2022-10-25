package hu.bme.mit.gamma.casestudy.iotsystem.network_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem.network.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public class ReflectiveNetworkAdapter implements ReflectiveComponentInterface {
	
	private NetworkAdapter wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface network = null;
	
	
	public ReflectiveNetworkAdapter() {
		wrappedComponent = new NetworkAdapter();
	}
	
	public ReflectiveNetworkAdapter(NetworkAdapter wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public NetworkAdapter getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "ImageIn", "ImageOut", "ImageLoss" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "ImageIn":
				return new String[] { "newData" };
			case "ImageOut":
				return new String[] { "newData" };
			case "ImageLoss":
				return new String[] { "newData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "ImageIn.newData":
				wrappedComponent.getImageIn().raiseNewData((double) parameters[0], (boolean) parameters[1]);
				break;
			case "ImageLoss.newData":
				wrappedComponent.getImageLoss().raiseNewData((double) parameters[0], (boolean) parameters[1]);
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "ImageOut.newData":
				if (wrappedComponent.getImageOut().isRaisedNewData()) {
return 					Objects.deepEquals(parameters[0], wrappedComponent.getImageOut().getBlurred()) && 
					Objects.deepEquals(parameters[1], wrappedComponent.getImageOut().getCar())
;					
				}
				break;
			default:
				throw new IllegalArgumentException("Not known port-out event combination: " + portEvent);
		}
		return false;
	}
	
	public boolean isStateActive(String region, String state) {
		return false;
	}
	
	public String[] getRegions() {
		return new String[] {  };
	}
	
	public String[] getStates(String region) {
		switch (region) {
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
		public void schedule() {
			schedule(null);
		}
	
		public void schedule(String instance) {
			wrappedComponent.schedule();
		}
	
	public String[] getVariables() {
		return new String[] {  };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { "network"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "network":
				if (network == null) {
					network = new ReflectiveNetwork(wrappedComponent.getNetwork());
				}
				return network;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
