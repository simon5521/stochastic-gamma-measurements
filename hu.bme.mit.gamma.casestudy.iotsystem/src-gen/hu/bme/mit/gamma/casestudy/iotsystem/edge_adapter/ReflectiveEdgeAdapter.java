package hu.bme.mit.gamma.casestudy.iotsystem.edge_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem.cloud.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public class ReflectiveEdgeAdapter implements ReflectiveComponentInterface {
	
	private EdgeAdapter wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface edge = null;
	
	
	public ReflectiveEdgeAdapter() {
		wrappedComponent = new EdgeAdapter();
	}
	
	public ReflectiveEdgeAdapter(EdgeAdapter wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public EdgeAdapter getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Camera", "TrafficStream", "LostImage", "CarLeave" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Camera":
				return new String[] { "newData" };
			case "TrafficStream":
				return new String[] { "carArrives", "carLeaves" };
			case "LostImage":
				return new String[] { "newEvent" };
			case "CarLeave":
				return new String[] { "newEvent" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Camera.newData":
				wrappedComponent.getCamera().raiseNewData((double) parameters[0], (boolean) parameters[1]);
				break;
			case "TrafficStream.carArrives":
				wrappedComponent.getTrafficStream().raiseCarArrives();
				break;
			case "TrafficStream.carLeaves":
				wrappedComponent.getTrafficStream().raiseCarLeaves();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "LostImage.newEvent":
				if (wrappedComponent.getLostImage().isRaisedNewEvent()) {
					return true;
				}
				break;
			case "CarLeave.newEvent":
				if (wrappedComponent.getCarLeave().isRaisedNewEvent()) {
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
		return new String[] { "edge"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "edge":
				if (edge == null) {
					edge = new ReflectiveEdge(wrappedComponent.getEdge());
				}
				return edge;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
