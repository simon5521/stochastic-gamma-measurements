package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.TimerInterface.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter.VoterStatemachine.*;

public class Voter implements VoterInterface {
	// Port instances
	private Faults faults = new Faults();
	private Sensor sensor = new Sensor();
	private Communication communication = new Communication();
	// Wrapped statemachine
	private VoterStatemachine voter;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Voter() {
		voter = new VoterStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		voter.reset();
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
	
	public class Faults implements HardwareFailureInterface.Required {
		private List<HardwareFailureInterface.Listener.Required> listeners = new LinkedList<HardwareFailureInterface.Listener.Required>();
		@Override
		public void raiseFailure() {
			getInsertQueue().add(new Event("Faults.failure"));
		}
		@Override
		public void registerListener(HardwareFailureInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<HardwareFailureInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Faults getFaults() {
		return faults;
	}
	
	public class Sensor implements SensorInterface.Required {
		private List<SensorInterface.Listener.Required> listeners = new LinkedList<SensorInterface.Listener.Required>();
		@Override
		public void raiseFailstop() {
			getInsertQueue().add(new Event("Sensor.failstop"));
		}
		@Override
		public void registerListener(SensorInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<SensorInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Sensor getSensor() {
		return sensor;
	}
	
	public class Communication implements SensorInterface.Provided {
		private List<SensorInterface.Listener.Provided> listeners = new LinkedList<SensorInterface.Listener.Provided>();
		@Override
		public boolean isRaisedFailstop() {
			return voter.getCommunication_failstop_Out();
		}
		@Override
		public void registerListener(SensorInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<SensorInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Communication getCommunication() {
		return communication;
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
				case "Faults.failure": 
					voter.setFaults_failure_In(true);
				break;
				case "Sensor.failstop": 
					voter.setSensor_failstop_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		voter.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (communication.isRaisedFailstop()) {
			for (SensorInterface.Listener.Provided listener : communication.getRegisteredListeners()) {
				listener.raiseFailstop();
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main":
				return voter.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	public long getSensorfailure() {
		return voter.getSensorfailure();
	}
	
	@Override
	public String toString() {
		return voter.toString();
	}
}
