package hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_adapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_sct.*;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_sct.*;

public class TrafficAdapter implements Runnable, TrafficAdapterInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private TrafficStatechart trafficAdapter;
	// Control port instances
	// Wrapped port instances
	private CarArrives carArrives;
	private CarLeaves carLeaves;
	private CarArrivesOut carArrivesOut;
	private TrafficStream trafficStream;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue arriveQueue;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue leaveQueue;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public TrafficAdapter() {
		trafficAdapter = new TrafficStatechart();
		// Wrapped port instances
		carArrives = new CarArrives();
		carLeaves = new CarLeaves();
		carArrivesOut = new CarArrivesOut();
		trafficStream = new TrafficStream();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		trafficAdapter.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		trafficAdapter = new TrafficStatechart();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("arriveQueue", -(1), (int) 1);
		arriveQueue = __asyncQueue.getSubQueue("arriveQueue");
		__asyncQueue.addSubQueue("leaveQueue", -(1), (int) 1);
		leaveQueue = __asyncQueue.getSubQueue("leaveQueue");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class CarArrives implements EventStreamInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			arriveQueue.offer(new Event("CarArrives.newEvent"));
		}
		
		
		@Override
		public void registerListener(EventStreamInterface.Listener.Required listener) {
			trafficAdapter.getCarArrives().registerListener(listener);
		}
		
		@Override
		public List<EventStreamInterface.Listener.Required> getRegisteredListeners() {
			return trafficAdapter.getCarArrives().getRegisteredListeners();
		}
		
	}
	
	@Override
	public CarArrives getCarArrives() {
		return carArrives;
	}
	
	public class CarLeaves implements EventStreamInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			leaveQueue.offer(new Event("CarLeaves.newEvent"));
		}
		
		
		@Override
		public void registerListener(EventStreamInterface.Listener.Required listener) {
			trafficAdapter.getCarLeaves().registerListener(listener);
		}
		
		@Override
		public List<EventStreamInterface.Listener.Required> getRegisteredListeners() {
			return trafficAdapter.getCarLeaves().getRegisteredListeners();
		}
		
	}
	
	@Override
	public CarLeaves getCarLeaves() {
		return carLeaves;
	}
	
	public class CarArrivesOut implements EventStreamInterface.Provided {
		
		
		@Override
		public boolean isRaisedNewEvent() {
			return trafficAdapter.getCarArrivesOut().isRaisedNewEvent();
		}
		
		@Override
		public void registerListener(EventStreamInterface.Listener.Provided listener) {
			trafficAdapter.getCarArrivesOut().registerListener(listener);
		}
		
		@Override
		public List<EventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return trafficAdapter.getCarArrivesOut().getRegisteredListeners();
		}
		
	}
	
	@Override
	public CarArrivesOut getCarArrivesOut() {
		return carArrivesOut;
	}
	
	public class TrafficStream implements TrafficEventStreamInterface.Provided {
		
		
		@Override
		public boolean isRaisedCarArrives() {
			return trafficAdapter.getTrafficStream().isRaisedCarArrives();
		}
		
		@Override
		public boolean isRaisedCarLeaves() {
			return trafficAdapter.getTrafficStream().isRaisedCarLeaves();
		}
		
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Provided listener) {
			trafficAdapter.getTrafficStream().registerListener(listener);
		}
		
		@Override
		public List<TrafficEventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return trafficAdapter.getTrafficStream().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TrafficStream getTrafficStream() {
		return trafficStream;
	}
	
	/** Manual scheduling. */
	public void schedule() {
		Event event = __asyncQueue.poll();
		if (event == null) {
			// There was no event in the queue
			return;
		}
		processEvent(event);
	}
	
	/** Operation. */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Event event = __asyncQueue.take();		
				processEvent(event);
			} catch (InterruptedException e) {
				thread.interrupt();
			}
		}
	}
	
	private void processEvent(Event event) {
		if (!isControlEvent(event)) {
			// Event is forwarded to the wrapped component
			forwardEvent(event);
		}
		performControlActions(event);
	}
	
	private boolean isControlEvent(Event event) {
		return false;
	}
	
	private void forwardEvent(Event event) {
		switch (event.getEvent()) {
			case "CarArrives.newEvent":
				trafficAdapter.getCarArrives().raiseNewEvent();
			break;
			case "CarLeaves.newEvent":
				trafficAdapter.getCarLeaves().raiseNewEvent();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("CarArrives")) {
			trafficAdapter.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("CarLeaves")) {
			trafficAdapter.runCycle();
			return;
		}
	}
	
	/** Starts this wrapper instance on a thread. */
	@Override
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public boolean isWaiting() {
		return thread.getState() == Thread.State.WAITING;
	}
	
	/** Stops the thread running this wrapper instance. */
	public void interrupt() {
		thread.interrupt();
	}
	
	public TrafficStatechart getTrafficAdapter() {
		return trafficAdapter;
	}
	
	
}
