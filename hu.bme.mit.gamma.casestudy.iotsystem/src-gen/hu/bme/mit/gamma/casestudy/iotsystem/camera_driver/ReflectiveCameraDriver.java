package hu.bme.mit.gamma.casestudy.iotsystem.camera_driver;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public class ReflectiveCameraDriver implements ReflectiveComponentInterface {
	
	private CameraDriver wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveCameraDriver() {
		wrappedComponent = new CameraDriver();
	}
	
	public ReflectiveCameraDriver(CameraDriver wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public CameraDriver getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Traffic", "Requests", "Images" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Traffic":
				return new String[] { "carArrives", "carLeaves" };
			case "Requests":
				return new String[] { "newEvent" };
			case "Images":
				return new String[] { "newData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Traffic.carArrives":
				wrappedComponent.getTraffic().raiseCarArrives();
				break;
			case "Traffic.carLeaves":
				wrappedComponent.getTraffic().raiseCarLeaves();
				break;
			case "Requests.newEvent":
				wrappedComponent.getRequests().raiseNewEvent();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Images.newData":
				if (wrappedComponent.getImages().isRaisedNewData()) {
return 					Objects.deepEquals(parameters[0], wrappedComponent.getImages().getBlurred()) && 
					Objects.deepEquals(parameters[1], wrappedComponent.getImages().getCar())
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
				return new String[] { "CarIsVisible", "CarIsNotVisible" };
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
			case "CameraDriver":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
