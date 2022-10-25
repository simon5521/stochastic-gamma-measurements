package hu.bme.mit.gamma.stochstic.casestudy.dualgps.voteradapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps.gps.*;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter.*;

public class Voterasync implements Runnable, VoterasyncInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private Voter voter;
	// Control port instances
	// Wrapped port instances
	private Faults faults;
	private Sensor1 sensor1;
	private Sensor2 sensor2;
	private Communication communication;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue faultQueue;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue s1Queue;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue s2Queue;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public Voterasync() {
		voter = new Voter();
		// Wrapped port instances
		faults = new Faults();
		sensor1 = new Sensor1();
		sensor2 = new Sensor2();
		communication = new Communication();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		voter.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		voter = new Voter();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("s2Queue", -(3), (int) 1);
		s2Queue = __asyncQueue.getSubQueue("s2Queue");
		__asyncQueue.addSubQueue("s1Queue", -(2), (int) 1);
		s1Queue = __asyncQueue.getSubQueue("s1Queue");
		__asyncQueue.addSubQueue("faultQueue", -(1), (int) 1);
		faultQueue = __asyncQueue.getSubQueue("faultQueue");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class Faults implements HardwareFailureInterface.Required {
		
		@Override
		public void raiseFailure() {
			faultQueue.offer(new Event("Faults.failure"));
		}
		
		
		@Override
		public void registerListener(HardwareFailureInterface.Listener.Required listener) {
			voter.getFaults().registerListener(listener);
		}
		
		@Override
		public List<HardwareFailureInterface.Listener.Required> getRegisteredListeners() {
			return voter.getFaults().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Faults getFaults() {
		return faults;
	}
	
	public class Sensor1 implements SensorInterface.Required {
		
		@Override
		public void raiseFailstop() {
			s1Queue.offer(new Event("Sensor1.failstop"));
		}
		
		
		@Override
		public void registerListener(SensorInterface.Listener.Required listener) {
			voter.getSensor1().registerListener(listener);
		}
		
		@Override
		public List<SensorInterface.Listener.Required> getRegisteredListeners() {
			return voter.getSensor1().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Sensor1 getSensor1() {
		return sensor1;
	}
	
	public class Sensor2 implements SensorInterface.Required {
		
		@Override
		public void raiseFailstop() {
			s2Queue.offer(new Event("Sensor2.failstop"));
		}
		
		
		@Override
		public void registerListener(SensorInterface.Listener.Required listener) {
			voter.getSensor2().registerListener(listener);
		}
		
		@Override
		public List<SensorInterface.Listener.Required> getRegisteredListeners() {
			return voter.getSensor2().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Sensor2 getSensor2() {
		return sensor2;
	}
	
	public class Communication implements SensorInterface.Provided {
		
		
		@Override
		public boolean isRaisedFailstop() {
			return voter.getCommunication().isRaisedFailstop();
		}
		
		@Override
		public void registerListener(SensorInterface.Listener.Provided listener) {
			voter.getCommunication().registerListener(listener);
		}
		
		@Override
		public List<SensorInterface.Listener.Provided> getRegisteredListeners() {
			return voter.getCommunication().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Communication getCommunication() {
		return communication;
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
			case "Faults.failure":
				voter.getFaults().raiseFailure();
			break;
			case "Sensor1.failstop":
				voter.getSensor1().raiseFailstop();
			break;
			case "Sensor2.failstop":
				voter.getSensor2().raiseFailstop();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("Faults")) {
			voter.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("Sensor1")) {
			voter.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("Sensor2")) {
			voter.runCycle();
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
	
	public Voter getVoter() {
		return voter;
	}
	
	
}
