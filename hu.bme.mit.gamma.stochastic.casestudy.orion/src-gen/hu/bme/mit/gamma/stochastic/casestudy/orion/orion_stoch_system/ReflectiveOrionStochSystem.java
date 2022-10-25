package hu.bme.mit.gamma.stochastic.casestudy.orion.orion_stoch_system;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_master_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.status_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.orion_slave_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion.channel.*;

public class ReflectiveOrionStochSystem implements ReflectiveComponentInterface {
	
	private OrionStochSystem wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface master = null;
	private ReflectiveComponentInterface slave = null;
	private ReflectiveComponentInterface connStatus = null;
	
	
	public ReflectiveOrionStochSystem() {
		wrappedComponent = new OrionStochSystem();
	}
	
	public ReflectiveOrionStochSystem(OrionStochSystem wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public OrionStochSystem getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "TimoeutKeepAliveReceiveTimeout_3", "TimeoutKapcsolodik_2", "TimeoutZarva_0", "TimeoutKeepAliveSendTimeout_1", "TimeoutKeepAliveReceiveTimeout_4", "TimeoutKapcsolodik_3", "TimeoutKeepAliveSendTimeout_0", "SystemStatus" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "TimoeutKeepAliveReceiveTimeout_3":
				return new String[] { "newEvent" };
			case "TimeoutKapcsolodik_2":
				return new String[] { "newEvent" };
			case "TimeoutZarva_0":
				return new String[] { "newEvent" };
			case "TimeoutKeepAliveSendTimeout_1":
				return new String[] { "newEvent" };
			case "TimeoutKeepAliveReceiveTimeout_4":
				return new String[] { "newEvent" };
			case "TimeoutKapcsolodik_3":
				return new String[] { "newEvent" };
			case "TimeoutKeepAliveSendTimeout_0":
				return new String[] { "newEvent" };
			case "SystemStatus":
				return new String[] { "conn", "disconn" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "TimoeutKeepAliveReceiveTimeout_3.newEvent":
				wrappedComponent.getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent();
				break;
			case "TimeoutKapcsolodik_2.newEvent":
				wrappedComponent.getTimeoutKapcsolodik_2().raiseNewEvent();
				break;
			case "TimeoutZarva_0.newEvent":
				wrappedComponent.getTimeoutZarva_0().raiseNewEvent();
				break;
			case "TimeoutKeepAliveSendTimeout_1.newEvent":
				wrappedComponent.getTimeoutKeepAliveSendTimeout_1().raiseNewEvent();
				break;
			case "TimeoutKeepAliveReceiveTimeout_4.newEvent":
				wrappedComponent.getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent();
				break;
			case "TimeoutKapcsolodik_3.newEvent":
				wrappedComponent.getTimeoutKapcsolodik_3().raiseNewEvent();
				break;
			case "TimeoutKeepAliveSendTimeout_0.newEvent":
				wrappedComponent.getTimeoutKeepAliveSendTimeout_0().raiseNewEvent();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "SystemStatus.conn":
				if (wrappedComponent.getSystemStatus().isRaisedConn()) {
					return true;
				}
				break;
			case "SystemStatus.disconn":
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
		return new String[] { "master", "slave", "connStatus"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "master":
				if (master == null) {
					master = new ReflectiveOrion_Master_SM_ext(wrappedComponent.getMaster());
				}
				return master;
			case "slave":
				if (slave == null) {
					slave = new ReflectiveOrion_Slave_SM_ext(wrappedComponent.getSlave());
				}
				return slave;
			case "connStatus":
				if (connStatus == null) {
					connStatus = new ReflectiveStatus_SM(wrappedComponent.getConnStatus());
				}
				return connStatus;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
