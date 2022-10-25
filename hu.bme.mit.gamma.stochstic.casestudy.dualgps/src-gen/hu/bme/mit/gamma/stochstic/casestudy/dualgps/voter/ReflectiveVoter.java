package hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*;

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
		return new String[] { "Faults", "Sensor1", "Sensor2", "Communication" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Faults":
				return new String[] { "failure" };
			case "Sensor1":
				return new String[] { "failstop" };
			case "Sensor2":
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
			case "Sensor1.failstop":
				wrappedComponent.getSensor1().raiseFailstop();
				break;
			case "Sensor2.failstop":
				wrappedComponent.getSensor2().raiseFailstop();
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
				return new String[] { "operation", "onlyGPS1", "onlyGPS2", "failstop" };
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
		return new String[] { "sensor1Failure", "sensor2Failure" };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
			case "sensor1Failure":
				return wrappedComponent.getSensor1Failure();
			case "sensor2Failure":
				return wrappedComponent.getSensor2Failure();
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
