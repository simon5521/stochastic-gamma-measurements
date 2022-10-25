package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.TimerInterface.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer.SummarizerStatemachine.*;

public class Summarizer implements SummarizerInterface {
	// Port instances
	private InPort inPort = new InPort();
	private OutPort outPort = new OutPort();
	// Wrapped statemachine
	private SummarizerStatemachine summarizer;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Summarizer() {
		summarizer = new SummarizerStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		summarizer.reset();
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
	
	public class InPort implements ConnectionStateInterface.Required {
		private List<ConnectionStateInterface.Listener.Required> listeners = new LinkedList<ConnectionStateInterface.Listener.Required>();
		@Override
		public void raiseConn() {
			getInsertQueue().add(new Event("inPort.conn"));
		}
		@Override
		public void raiseDisconn() {
			getInsertQueue().add(new Event("inPort.disconn"));
		}
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<ConnectionStateInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public InPort getInPort() {
		return inPort;
	}
	
	public class OutPort implements ConnectionStateInterface.Provided {
		private List<ConnectionStateInterface.Listener.Provided> listeners = new LinkedList<ConnectionStateInterface.Listener.Provided>();
		@Override
		public boolean isRaisedConn() {
			return summarizer.getOutPort_conn_Out();
		}
		@Override
		public boolean isRaisedDisconn() {
			return summarizer.getOutPort_disconn_Out();
		}
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public OutPort getOutPort() {
		return outPort;
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
				case "inPort.conn": 
					summarizer.setInPort_conn_In(true);
				break;
				case "inPort.disconn": 
					summarizer.setInPort_disconn_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		summarizer.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (outPort.isRaisedConn()) {
			for (ConnectionStateInterface.Listener.Provided listener : outPort.getRegisteredListeners()) {
				listener.raiseConn();
			}
		}
		if (outPort.isRaisedDisconn()) {
			for (ConnectionStateInterface.Listener.Provided listener : outPort.getRegisteredListeners()) {
				listener.raiseDisconn();
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main":
				return summarizer.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return summarizer.toString();
	}
}
