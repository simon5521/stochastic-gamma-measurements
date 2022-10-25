package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_control;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;

public class ReflectiveCameraControl implements ReflectiveComponentInterface {
	
	private CameraControl wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveCameraControl() {
		wrappedComponent = new CameraControl();
	}
	
	public ReflectiveCameraControl(CameraControl wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public CameraControl getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Requests", "DriverImages", "NetworkImages" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Requests":
				return new String[] { "newEvent" };
			case "DriverImages":
				return new String[] { "newData" };
			case "NetworkImages":
				return new String[] { "newData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "DriverImages.newData":
				wrappedComponent.getDriverImages().raiseNewData((double) parameters[0], (boolean) parameters[1]);
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Requests.newEvent":
				if (wrappedComponent.getRequests().isRaisedNewEvent()) {
					return true;
				}
				break;
			case "NetworkImages.newData":
				if (wrappedComponent.getNetworkImages().isRaisedNewData()) {
return 					Objects.deepEquals(parameters[0], wrappedComponent.getNetworkImages().getBlurred()) && 
					Objects.deepEquals(parameters[1], wrappedComponent.getNetworkImages().getCar())
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
				return new String[] { "MainOperation" };
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
			case "CameraControl":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
