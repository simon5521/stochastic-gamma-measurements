package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channel;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public class ReflectiveChannelStatechart implements ReflectiveComponentInterface {
	
	private ChannelStatechart wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveChannelStatechart() {
		wrappedComponent = new ChannelStatechart();
	}
	
	public ReflectiveChannelStatechart(ChannelStatechart wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public ChannelStatechart getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Output", "Input" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Output":
				return new String[] { "OrionDisconn", "OrionDisconnCause", "OrionConnReq", "OrionAppData", "OrionKeepAlive", "OrionConnConf", "OrionConnResp" };
			case "Input":
				return new String[] { "OrionDisconn", "OrionDisconnCause", "OrionConnReq", "OrionAppData", "OrionKeepAlive", "OrionConnConf", "OrionConnResp" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Input.OrionDisconn":
				wrappedComponent.getInput().raiseOrionDisconn();
				break;
			case "Input.OrionDisconnCause":
				wrappedComponent.getInput().raiseOrionDisconnCause();
				break;
			case "Input.OrionConnReq":
				wrappedComponent.getInput().raiseOrionConnReq();
				break;
			case "Input.OrionAppData":
				wrappedComponent.getInput().raiseOrionAppData();
				break;
			case "Input.OrionKeepAlive":
				wrappedComponent.getInput().raiseOrionKeepAlive();
				break;
			case "Input.OrionConnConf":
				wrappedComponent.getInput().raiseOrionConnConf();
				break;
			case "Input.OrionConnResp":
				wrappedComponent.getInput().raiseOrionConnResp();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Output.OrionDisconn":
				if (wrappedComponent.getOutput().isRaisedOrionDisconn()) {
					return true;
				}
				break;
			case "Output.OrionDisconnCause":
				if (wrappedComponent.getOutput().isRaisedOrionDisconnCause()) {
					return true;
				}
				break;
			case "Output.OrionConnReq":
				if (wrappedComponent.getOutput().isRaisedOrionConnReq()) {
					return true;
				}
				break;
			case "Output.OrionAppData":
				if (wrappedComponent.getOutput().isRaisedOrionAppData()) {
					return true;
				}
				break;
			case "Output.OrionKeepAlive":
				if (wrappedComponent.getOutput().isRaisedOrionKeepAlive()) {
					return true;
				}
				break;
			case "Output.OrionConnConf":
				if (wrappedComponent.getOutput().isRaisedOrionConnConf()) {
					return true;
				}
				break;
			case "Output.OrionConnResp":
				if (wrappedComponent.getOutput().isRaisedOrionConnResp()) {
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
		return new String[] { "OrionDisconnRegion" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "OrionDisconnRegion":
				return new String[] { "IdleOrionDisconn" };
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
			case "ChannelStatechart":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
