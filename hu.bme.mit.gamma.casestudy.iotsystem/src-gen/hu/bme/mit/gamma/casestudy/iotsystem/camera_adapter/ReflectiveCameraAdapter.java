package hu.bme.mit.gamma.casestudy.iotsystem.camera_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_control.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_software.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_driver.*;

public class ReflectiveCameraAdapter implements ReflectiveComponentInterface {
	
	private CameraAdapter wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface cameraSoftware = null;
	
	
	public ReflectiveCameraAdapter() {
		wrappedComponent = new CameraAdapter();
	}
	
	public ReflectiveCameraAdapter(CameraAdapter wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public CameraAdapter getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "SoftwareTimer", "TrafficSensing", "Images" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "SoftwareTimer":
				return new String[] { "newEvent" };
			case "TrafficSensing":
				return new String[] { "carArrives", "carLeaves" };
			case "Images":
				return new String[] { "newData" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "SoftwareTimer.newEvent":
				wrappedComponent.getSoftwareTimer().raiseNewEvent();
				break;
			case "TrafficSensing.carArrives":
				wrappedComponent.getTrafficSensing().raiseCarArrives();
				break;
			case "TrafficSensing.carLeaves":
				wrappedComponent.getTrafficSensing().raiseCarLeaves();
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
		return new String[] { "cameraSoftware"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "cameraSoftware":
				if (cameraSoftware == null) {
					cameraSoftware = new ReflectiveCameraSoftware(wrappedComponent.getCameraSoftware());
				}
				return cameraSoftware;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
