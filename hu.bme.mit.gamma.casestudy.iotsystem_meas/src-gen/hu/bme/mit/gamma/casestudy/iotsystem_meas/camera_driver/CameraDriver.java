package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_driver;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.TimerInterface.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_driver.CameraDriverStatemachine.*;

public class CameraDriver implements CameraDriverInterface {
	// Port instances
	private Traffic traffic = new Traffic();
	private Requests requests = new Requests();
	private Images images = new Images();
	// Wrapped statemachine
	private CameraDriverStatemachine cameraDriver;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public CameraDriver() {
		cameraDriver = new CameraDriverStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		cameraDriver.reset();
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
	
	public class Traffic implements TrafficEventStreamInterface.Required {
		private List<TrafficEventStreamInterface.Listener.Required> listeners = new LinkedList<TrafficEventStreamInterface.Listener.Required>();
		@Override
		public void raiseCarArrives() {
			getInsertQueue().add(new Event("Traffic.carArrives"));
		}
		@Override
		public void raiseCarLeaves() {
			getInsertQueue().add(new Event("Traffic.carLeaves"));
		}
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<TrafficEventStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Traffic getTraffic() {
		return traffic;
	}
	
	public class Requests implements EventStreamInterface.Required {
		private List<EventStreamInterface.Listener.Required> listeners = new LinkedList<EventStreamInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("Requests.newEvent"));
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
	
	public Requests getRequests() {
		return requests;
	}
	
	public class Images implements ImageStreamInterface.Provided {
		private List<ImageStreamInterface.Listener.Provided> listeners = new LinkedList<ImageStreamInterface.Listener.Provided>();
		@Override
		public boolean isRaisedNewData() {
			return cameraDriver.getImages_newData_Out();
		}
		@Override
		public double getBlurred() {
			return cameraDriver.getImages_newData_Out_blurred();
		}
		@Override
		public boolean getCar() {
			return cameraDriver.getImages_newData_Out_car();
		}
		@Override
		public void registerListener(ImageStreamInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<ImageStreamInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Images getImages() {
		return images;
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
				case "Traffic.carArrives": 
					cameraDriver.setTraffic_carArrives_In(true);
				break;
				case "Traffic.carLeaves": 
					cameraDriver.setTraffic_carLeaves_In(true);
				break;
				case "Requests.newEvent": 
					cameraDriver.setRequests_newEvent_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		cameraDriver.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (images.isRaisedNewData()) {
			for (ImageStreamInterface.Listener.Provided listener : images.getRegisteredListeners()) {
				listener.raiseNewData(cameraDriver.getImages_newData_Out_blurred(), cameraDriver.getImages_newData_Out_car());
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main":
				return cameraDriver.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return cameraDriver.toString();
	}
}
