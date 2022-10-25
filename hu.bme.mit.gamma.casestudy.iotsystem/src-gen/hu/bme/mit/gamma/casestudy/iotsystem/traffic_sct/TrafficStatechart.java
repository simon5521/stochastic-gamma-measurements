package hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.TimerInterface.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct.TrafficStatechartStatemachine.*;

public class TrafficStatechart implements TrafficStatechartInterface {
	// Port instances
	private CarArrives carArrives = new CarArrives();
	private CarLeaves carLeaves = new CarLeaves();
	private CarArrivesOut carArrivesOut = new CarArrivesOut();
	private TrafficStream trafficStream = new TrafficStream();
	// Wrapped statemachine
	private TrafficStatechartStatemachine trafficStatechart;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public TrafficStatechart() {
		trafficStatechart = new TrafficStatechartStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		trafficStatechart.reset();
		timer.saveTime(this);
		notifyListeners();
	}

	/** Changes the event queues of the component instance. Should be used only be the container (composite system) class. */
	public void changeEventQueues() {
		insertQueue = !insertQueue;
		processQueue = !processQueue;
	}
	
	/** Changes the event queues to which the events are put. Should be used only be a cascade container (composite system) class. */
	public void changeInsertQueue() {
		insertQueue = !insertQueue;
	}
	
	/** Returns whether the eventQueue containing incoming messages is empty. Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return getInsertQueue().isEmpty();
	}
	
	/** Returns the event queue into which events should be put in the particular cycle. */
	private Queue<Event> getInsertQueue() {
		if (insertQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Returns the event queue from which events should be inspected in the particular cycle. */
	private Queue<Event> getProcessQueue() {
		if (processQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	public class CarArrives implements EventStreamInterface.Required {
		private List<EventStreamInterface.Listener.Required> listeners = new LinkedList<EventStreamInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("CarArrives.newEvent"));
		}
		@Override
		public void registerListener(EventStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<EventStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public CarArrives getCarArrives() {
		return carArrives;
	}
	
	public class CarLeaves implements EventStreamInterface.Required {
		private List<EventStreamInterface.Listener.Required> listeners = new LinkedList<EventStreamInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("CarLeaves.newEvent"));
		}
		@Override
		public void registerListener(EventStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<EventStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public CarLeaves getCarLeaves() {
		return carLeaves;
	}
	
	public class CarArrivesOut implements EventStreamInterface.Provided {
		private List<EventStreamInterface.Listener.Provided> listeners = new LinkedList<EventStreamInterface.Listener.Provided>();
		@Override
		public boolean isRaisedNewEvent() {
			return trafficStatechart.getCarArrivesOut_newEvent_Out();
		}
		@Override
		public void registerListener(EventStreamInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<EventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public CarArrivesOut getCarArrivesOut() {
		return carArrivesOut;
	}
	
	public class TrafficStream implements TrafficEventStreamInterface.Provided {
		private List<TrafficEventStreamInterface.Listener.Provided> listeners = new LinkedList<TrafficEventStreamInterface.Listener.Provided>();
		@Override
		public boolean isRaisedCarArrives() {
			return trafficStatechart.getTrafficStream_carArrives_Out();
		}
		@Override
		public boolean isRaisedCarLeaves() {
			return trafficStatechart.getTrafficStream_carLeaves_Out();
		}
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<TrafficEventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public TrafficStream getTrafficStream() {
		return trafficStream;
	}
	
	public void runCycle() {
		changeEventQueues();
		runComponent();
	}
	
	public void runComponent() {
		Queue<Event> eventQueue = getProcessQueue();
		while (!eventQueue.isEmpty()) {
			Event event = eventQueue.remove();
			switch (event.getEvent()) {
				case "CarArrives.newEvent": 
					trafficStatechart.setCarArrives_newEvent_In(true);
				break;
				case "CarLeaves.newEvent": 
					trafficStatechart.setCarLeaves_newEvent_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		trafficStatechart.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (carArrivesOut.isRaisedNewEvent()) {
			for (EventStreamInterface.Listener.Provided listener : carArrivesOut.getRegisteredListeners()) {
				listener.raiseNewEvent();
			}
		}
		if (trafficStream.isRaisedCarArrives()) {
			for (TrafficEventStreamInterface.Listener.Provided listener : trafficStream.getRegisteredListeners()) {
				listener.raiseCarArrives();
			}
		}
		if (trafficStream.isRaisedCarLeaves()) {
			for (TrafficEventStreamInterface.Listener.Provided listener : trafficStream.getRegisteredListeners()) {
				listener.raiseCarLeaves();
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main":
				return trafficStatechart.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return trafficStatechart.toString();
	}
}
