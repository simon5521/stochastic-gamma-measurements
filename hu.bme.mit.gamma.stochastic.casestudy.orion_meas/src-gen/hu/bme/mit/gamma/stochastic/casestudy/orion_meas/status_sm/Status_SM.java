package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.TimerInterface.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm.Status_SMStatemachine.*;

public class Status_SM implements Status_SMInterface {
	// Port instances
	private SlaveStatus slaveStatus = new SlaveStatus();
	private MasterStatus masterStatus = new MasterStatus();
	private SystemStatus systemStatus = new SystemStatus();
	// Wrapped statemachine
	private Status_SMStatemachine status_SM;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Status_SM() {
		status_SM = new Status_SMStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		status_SM.reset();
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
	
	public class SlaveStatus implements ConnectionStateInterface.Required {
		private List<ConnectionStateInterface.Listener.Required> listeners = new LinkedList<ConnectionStateInterface.Listener.Required>();
		@Override
		public void raiseConn() {
			getInsertQueue().add(new Event("slaveStatus.conn"));
		}
		@Override
		public void raiseDisconn() {
			getInsertQueue().add(new Event("slaveStatus.disconn"));
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
	
	public SlaveStatus getSlaveStatus() {
		return slaveStatus;
	}
	
	public class MasterStatus implements ConnectionStateInterface.Required {
		private List<ConnectionStateInterface.Listener.Required> listeners = new LinkedList<ConnectionStateInterface.Listener.Required>();
		@Override
		public void raiseConn() {
			getInsertQueue().add(new Event("masterStatus.conn"));
		}
		@Override
		public void raiseDisconn() {
			getInsertQueue().add(new Event("masterStatus.disconn"));
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
	
	public MasterStatus getMasterStatus() {
		return masterStatus;
	}
	
	public class SystemStatus implements ConnectionStateInterface.Provided {
		private List<ConnectionStateInterface.Listener.Provided> listeners = new LinkedList<ConnectionStateInterface.Listener.Provided>();
		@Override
		public boolean isRaisedConn() {
			return status_SM.getSystemStatus_conn_Out();
		}
		@Override
		public boolean isRaisedDisconn() {
			return status_SM.getSystemStatus_disconn_Out();
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
	
	public SystemStatus getSystemStatus() {
		return systemStatus;
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
				case "slaveStatus.conn": 
					status_SM.setSlaveStatus_conn_In(true);
				break;
				case "slaveStatus.disconn": 
					status_SM.setSlaveStatus_disconn_In(true);
				break;
				case "masterStatus.conn": 
					status_SM.setMasterStatus_conn_In(true);
				break;
				case "masterStatus.disconn": 
					status_SM.setMasterStatus_disconn_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		status_SM.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (systemStatus.isRaisedConn()) {
			for (ConnectionStateInterface.Listener.Provided listener : systemStatus.getRegisteredListeners()) {
				listener.raiseConn();
			}
		}
		if (systemStatus.isRaisedDisconn()) {
			for (ConnectionStateInterface.Listener.Provided listener : systemStatus.getRegisteredListeners()) {
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
				return status_SM.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return status_SM.toString();
	}
}
