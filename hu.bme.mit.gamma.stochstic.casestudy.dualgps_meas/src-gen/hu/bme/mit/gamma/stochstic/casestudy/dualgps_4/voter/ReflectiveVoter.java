package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;

public class ReflectiveVoter implements ReflectiveComponentInterface {
	
	private Voter wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveVoter() {
		wrappedComponent = new Voter();
	}
	
	public ReflectiveVoter(Voter wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Voter getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Faults", "Sensor", "Communication" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Faults":
				return new String[] { "failure" };
			case "Sensor":
				return new String[] { "failstop" };
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
			case "Sensor.failstop":
				wrappedComponent.getSensor().raiseFailstop();
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
		return wrappedComponent.isStateActive(region, state);
	}
	
	public String[] getRegions() {
		return new String[] { "main" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "main":
				return new String[] { "operation", "failstop" };
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
		return new String[] { "sensorfailure" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "sensorfailure":
				return wrappedComponent.getSensorfailure();
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			// If the class name is given, then it will return itself
			case "Voter":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
