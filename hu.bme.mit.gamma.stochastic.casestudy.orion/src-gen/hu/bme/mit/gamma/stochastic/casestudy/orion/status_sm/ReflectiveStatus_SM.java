package hu.bme.mit.gamma.stochastic.casestudy.orion.status_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*;

public class ReflectiveStatus_SM implements ReflectiveComponentInterface {
	
	private Status_SM wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveStatus_SM() {
		wrappedComponent = new Status_SM();
	}
	
	public ReflectiveStatus_SM(Status_SM wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Status_SM getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "slaveStatus", "masterStatus", "systemStatus" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "slaveStatus":
				return new String[] { "conn", "disconn" };
			case "masterStatus":
				return new String[] { "conn", "disconn" };
			case "systemStatus":
				return new String[] { "conn", "disconn" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "slaveStatus.conn":
				wrappedComponent.getSlaveStatus().raiseConn();
				break;
			case "slaveStatus.disconn":
				wrappedComponent.getSlaveStatus().raiseDisconn();
				break;
			case "masterStatus.conn":
				wrappedComponent.getMasterStatus().raiseConn();
				break;
			case "masterStatus.disconn":
				wrappedComponent.getMasterStatus().raiseDisconn();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "systemStatus.conn":
				if (wrappedComponent.getSystemStatus().isRaisedConn()) {
					return true;
				}
				break;
			case "systemStatus.disconn":
				if (wrappedComponent.getSystemStatus().isRaisedDisconn()) {
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
				return new String[] { "no_conn", "master_conn", "slave_conn", "system_conn" };
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
			case "Status_SM":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
