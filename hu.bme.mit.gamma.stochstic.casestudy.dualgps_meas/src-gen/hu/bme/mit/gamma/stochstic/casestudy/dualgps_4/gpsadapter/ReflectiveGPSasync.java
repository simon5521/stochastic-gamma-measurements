package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gpsadapter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;

public class ReflectiveGPSasync implements ReflectiveComponentInterface {
	
	private GPSasync wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface gps = null;
	
	
	public ReflectiveGPSasync() {
		wrappedComponent = new GPSasync();
	}
	
	public ReflectiveGPSasync(GPSasync wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public GPSasync getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Faults", "Communication" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Faults":
				return new String[] { "failure" };
			case "Communication":
				return new String[] { "failstop" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Faults.failure":
				wrappedComponent.getFaults().raiseFailure();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Communication.failstop":
				if (wrappedComponent.getCommunication().isRaisedFailstop()) {
					return true;
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
		return new String[] { "gps"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "gps":
				if (gps == null) {
					gps = new ReflectiveGPS(wrappedComponent.getGps());
				}
				return gps;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
