package hu.bme.mit.gamma.casestudy.iotsystem.network;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public class ReflectiveNetwork implements ReflectiveComponentInterface {
	
	private Network wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveNetwork() {
		wrappedComponent = new Network();
	}
	
	public ReflectiveNetwork(Network wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Network getWrappedComponent() {
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
		return wrappedComponent.isStateActive(region, state);
	}
	
	public String[] getRegions() {
		return new String[] { "main" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "main":
				return new String[] { "mainstate" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
		public void schedule() {
			schedule(null);
		}
	
		public void schedule(String instance) {
			wrappedComponent.runCycle();
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
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			// If the class name is given, then it will return itself
			case "Network":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
