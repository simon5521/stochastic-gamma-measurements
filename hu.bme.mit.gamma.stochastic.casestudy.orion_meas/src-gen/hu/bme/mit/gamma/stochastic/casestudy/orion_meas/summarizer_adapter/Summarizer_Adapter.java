package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer_adapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer.*;

public class Summarizer_Adapter implements Runnable, Summarizer_AdapterInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private Summarizer summarizerComp;
	// Control port instances
	// Wrapped port instances
	private InPort inPort;
	private OutPort outPort;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue inQueue;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public Summarizer_Adapter() {
		summarizerComp = new Summarizer();
		// Wrapped port instances
		inPort = new InPort();
		outPort = new OutPort();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		summarizerComp.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		summarizerComp = new Summarizer();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("inQueue", -(1), (int) 1);
		inQueue = __asyncQueue.getSubQueue("inQueue");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class InPort implements ConnectionStateInterface.Required {
		
		@Override
		public void raiseConn() {
			inQueue.offer(new Event("inPort.conn"));
		}
		@Override
		public void raiseDisconn() {
			inQueue.offer(new Event("inPort.disconn"));
		}
		
		
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Required listener) {
			summarizerComp.getInPort().registerListener(listener);
		}
		
		@Override
		public List<ConnectionStateInterface.Listener.Required> getRegisteredListeners() {
			return summarizerComp.getInPort().getRegisteredListeners();
		}
		
	}
	
	@Override
	public InPort getInPort() {
		return inPort;
	}
	
	public class OutPort implements ConnectionStateInterface.Provided {
		
		
		@Override
		public boolean isRaisedConn() {
			return summarizerComp.getOutPort().isRaisedConn();
		}
		
		@Override
		public boolean isRaisedDisconn() {
			return summarizerComp.getOutPort().isRaisedDisconn();
		}
		
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			summarizerComp.getOutPort().registerListener(listener);
		}
		
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return summarizerComp.getOutPort().getRegisteredListeners();
		}
		
	}
	
	@Override
	public OutPort getOutPort() {
		return outPort;
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
			case "inPort.conn":
				summarizerComp.getInPort().raiseConn();
			break;
			case "inPort.disconn":
				summarizerComp.getInPort().raiseDisconn();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("inPort")) {
			summarizerComp.runCycle();
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
	
	public Summarizer getSummarizerComp() {
		return summarizerComp;
	}
	
	
}
