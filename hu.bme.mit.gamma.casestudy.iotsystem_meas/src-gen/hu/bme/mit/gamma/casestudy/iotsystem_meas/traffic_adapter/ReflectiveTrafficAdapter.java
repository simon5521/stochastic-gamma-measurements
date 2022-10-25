package hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_adapter;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_sct.*;

public class ReflectiveTrafficAdapter implements ReflectiveComponentInterface {
	
	private TrafficAdapter wrappedComponent;
	// Wrapped contained components
	private ReflectiveComponentInterface trafficAdapter = null;
	
	
	public ReflectiveTrafficAdapter() {
		wrappedComponent = new TrafficAdapter();
	}
	
	public ReflectiveTrafficAdapter(TrafficAdapter wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public TrafficAdapter getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "CarArrives", "CarLeaves", "CarArrivesOut", "TrafficStream" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "CarArrives":
				return new String[] { "newEvent" };
			case "CarLeaves":
				return new String[] { "newEvent" };
			case "CarArrivesOut":
				return new String[] { "newEvent" };
			case "TrafficStream":
				return new String[] { "carArrives", "carLeaves" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "CarArrives.newEvent":
				wrappedComponent.getCarArrives().raiseNewEvent();
				break;
			case "CarLeaves.newEvent":
				wrappedComponent.getCarLeaves().raiseNewEvent();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "CarArrivesOut.newEvent":
				if (wrappedComponent.getCarArrivesOut().isRaisedNewEvent()) {
					return true;
				}
				break;
			case "TrafficStream.carArrives":
				if (wrappedComponent.getTrafficStream().isRaisedCarArrives()) {
					return true;
				}
				break;
			case "TrafficStream.carLeaves":
				if (wrappedComponent.getTrafficStream().isRaisedCarLeaves()) {
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
		return new String[] { "trafficAdapter"};
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			case "trafficAdapter":
				if (trafficAdapter == null) {
					trafficAdapter = new ReflectiveTrafficStatechart(wrappedComponent.getTrafficAdapter());
				}
				return trafficAdapter;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}
