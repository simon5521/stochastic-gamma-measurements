package hu.bme.mit.gamma.casestudy.iotsystem.traffic;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct.*;
import hu.bme.mit.gamma.casestudy.iotsystem.channels.*;


public class RoadTraffic implements RoadTrafficInterface {
	// Component instances
	private TrafficAdapter trafficSct;
	// Environmental Component instances
	// Port instances
	private Cars cars = new Cars();
	// Channel instances
	private EventStreamChannelInterface channelCarOutOfCarDelay;
	private EventStreamChannelInterface channelCarsOfCarArrival;
	private EventStreamChannelInterface channelCarArrivesOutOfTrafficSct;
	
	public boolean isEmpty(){
		return  trafficSct.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			trafficSct.schedule();
		}
	}
	
	public RoadTraffic() {
		trafficSct = new TrafficAdapter();
		cars = new Cars();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		trafficSct.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class Cars implements TrafficEventStreamInterface.Provided {
	
		
		@Override
		public boolean isRaisedCarArrives() {
			return trafficSct.getTrafficStream().isRaisedCarArrives();
		}
		@Override
		public boolean isRaisedCarLeaves() {
			return trafficSct.getTrafficStream().isRaisedCarLeaves();
		}
		
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Provided listener) {
			trafficSct.getTrafficStream().registerListener(listener);
		}
		
		@Override
		public List<TrafficEventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return trafficSct.getTrafficStream().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Cars getCars() {
		return cars;
	}
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		trafficSct.start();
	}
	
	public boolean isWaiting() {
		return trafficSct.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public TrafficAdapter getTrafficSct() {
		return trafficSct;
	}
	
}
