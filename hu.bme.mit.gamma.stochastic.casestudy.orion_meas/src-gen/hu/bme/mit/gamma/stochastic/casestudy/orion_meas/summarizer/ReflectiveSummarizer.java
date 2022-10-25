package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public class ReflectiveSummarizer implements ReflectiveComponentInterface {
	
	private Summarizer wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveSummarizer() {
		wrappedComponent = new Summarizer();
	}
	
	public ReflectiveSummarizer(Summarizer wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Summarizer getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "inPort", "outPort" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "inPort":
				return new String[] { "conn", "disconn" };
			case "outPort":
				return new String[] { "conn", "disconn" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "inPort.conn":
				wrappedComponent.getInPort().raiseConn();
				break;
			case "inPort.disconn":
				wrappedComponent.getInPort().raiseDisconn();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "outPort.conn":
				if (wrappedComponent.getOutPort().isRaisedConn()) {
					return true;
				}
				break;
			case "outPort.disconn":
				if (wrappedComponent.getOutPort().isRaisedDisconn()) {
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
				return new String[] { "main" };
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
			case "Summarizer":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
