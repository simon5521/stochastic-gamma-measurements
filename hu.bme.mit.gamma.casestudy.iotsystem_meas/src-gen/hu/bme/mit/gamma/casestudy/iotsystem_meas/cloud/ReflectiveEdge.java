package hu.bme.mit.gamma.casestudy.iotsystem_meas.cloud;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;

public class ReflectiveEdge implements ReflectiveComponentInterface {
	
	private Edge wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveEdge() {
		wrappedComponent = new Edge();
	}
	
	public ReflectiveEdge(Edge wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Edge getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Camera", "TrafficStream", "LostImage" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Camera":
				return new String[] { "newData" };
			case "TrafficStream":
				return new String[] { "carArrives", "carLeaves" };
			case "LostImage":
				return new String[] { "newEvent" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Camera.newData":
				wrappedComponent.getCamera().raiseNewData((double) parameters[0], (boolean) parameters[1]);
				break;
			case "TrafficStream.carArrives":
				wrappedComponent.getTrafficStream().raiseCarArrives();
				break;
			case "TrafficStream.carLeaves":
				wrappedComponent.getTrafficStream().raiseCarLeaves();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "LostImage.newEvent":
				if (wrappedComponent.getLostImage().isRaisedNewEvent()) {
					return true;
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
				return new String[] { "WaitingForCars", "NotRecognized", "RecognizedByCamera" };
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
		return new String[] { "isblurred" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "isblurred":
				return wrappedComponent.getIsblurred();
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			// If the class name is given, then it will return itself
			case "Edge":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
