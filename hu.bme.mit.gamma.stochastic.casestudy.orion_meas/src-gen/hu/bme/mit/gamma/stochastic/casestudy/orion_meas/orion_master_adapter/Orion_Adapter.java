package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_adapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.channel.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_system.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_slave_sm.*;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_stoch_system.*;

public class Orion_Adapter implements Runnable, Orion_AdapterInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private OrionStochSystem master;
	// Control port instances
	// Wrapped port instances
	private TimoeutKeepAliveReceiveTimeout_3 timoeutKeepAliveReceiveTimeout_3;
	private TimeoutKapcsolodik_2 timeoutKapcsolodik_2;
	private TimeoutZarva_0 timeoutZarva_0;
	private TimeoutKeepAliveSendTimeout_1 timeoutKeepAliveSendTimeout_1;
	private TimeoutKeepAliveReceiveTimeout_4 timeoutKeepAliveReceiveTimeout_4;
	private TimeoutKapcsolodik_3 timeoutKapcsolodik_3;
	private TimeoutKeepAliveSendTimeout_0 timeoutKeepAliveSendTimeout_0;
	private SystemStatus systemStatus;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimoeutKeepAliveReceiveTimeout_3;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutKapcsolodik_2;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutZarva_0;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutKeepAliveSendTimeout_1;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutKeepAliveReceiveTimeout_4;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutKapcsolodik_3;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue queueOfTimeoutKeepAliveSendTimeout_0;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public Orion_Adapter() {
		master = new OrionStochSystem();
		// Wrapped port instances
		timoeutKeepAliveReceiveTimeout_3 = new TimoeutKeepAliveReceiveTimeout_3();
		timeoutKapcsolodik_2 = new TimeoutKapcsolodik_2();
		timeoutZarva_0 = new TimeoutZarva_0();
		timeoutKeepAliveSendTimeout_1 = new TimeoutKeepAliveSendTimeout_1();
		timeoutKeepAliveReceiveTimeout_4 = new TimeoutKeepAliveReceiveTimeout_4();
		timeoutKapcsolodik_3 = new TimeoutKapcsolodik_3();
		timeoutKeepAliveSendTimeout_0 = new TimeoutKeepAliveSendTimeout_0();
		systemStatus = new SystemStatus();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		master.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		master = new OrionStochSystem();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("queueOfTimoeutKeepAliveReceiveTimeout_3", -(1), (int) 1);
		queueOfTimoeutKeepAliveReceiveTimeout_3 = __asyncQueue.getSubQueue("queueOfTimoeutKeepAliveReceiveTimeout_3");
		__asyncQueue.addSubQueue("queueOfTimeoutKapcsolodik_2", -(1), (int) 1);
		queueOfTimeoutKapcsolodik_2 = __asyncQueue.getSubQueue("queueOfTimeoutKapcsolodik_2");
		__asyncQueue.addSubQueue("queueOfTimeoutZarva_0", -(1), (int) 1);
		queueOfTimeoutZarva_0 = __asyncQueue.getSubQueue("queueOfTimeoutZarva_0");
		__asyncQueue.addSubQueue("queueOfTimeoutKeepAliveSendTimeout_1", -(1), (int) 1);
		queueOfTimeoutKeepAliveSendTimeout_1 = __asyncQueue.getSubQueue("queueOfTimeoutKeepAliveSendTimeout_1");
		__asyncQueue.addSubQueue("queueOfTimeoutKeepAliveReceiveTimeout_4", -(1), (int) 1);
		queueOfTimeoutKeepAliveReceiveTimeout_4 = __asyncQueue.getSubQueue("queueOfTimeoutKeepAliveReceiveTimeout_4");
		__asyncQueue.addSubQueue("queueOfTimeoutKapcsolodik_3", -(1), (int) 1);
		queueOfTimeoutKapcsolodik_3 = __asyncQueue.getSubQueue("queueOfTimeoutKapcsolodik_3");
		__asyncQueue.addSubQueue("queueOfTimeoutKeepAliveSendTimeout_0", -(1), (int) 1);
		queueOfTimeoutKeepAliveSendTimeout_0 = __asyncQueue.getSubQueue("queueOfTimeoutKeepAliveSendTimeout_0");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class TimoeutKeepAliveReceiveTimeout_3 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimoeutKeepAliveReceiveTimeout_3.offer(new Event("TimoeutKeepAliveReceiveTimeout_3.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimoeutKeepAliveReceiveTimeout_3().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimoeutKeepAliveReceiveTimeout_3().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimoeutKeepAliveReceiveTimeout_3 getTimoeutKeepAliveReceiveTimeout_3() {
		return timoeutKeepAliveReceiveTimeout_3;
	}
	
	public class TimeoutKapcsolodik_2 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutKapcsolodik_2.offer(new Event("TimeoutKapcsolodik_2.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutKapcsolodik_2().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutKapcsolodik_2().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutKapcsolodik_2 getTimeoutKapcsolodik_2() {
		return timeoutKapcsolodik_2;
	}
	
	public class TimeoutZarva_0 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutZarva_0.offer(new Event("TimeoutZarva_0.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutZarva_0().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutZarva_0().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutZarva_0 getTimeoutZarva_0() {
		return timeoutZarva_0;
	}
	
	public class TimeoutKeepAliveSendTimeout_1 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutKeepAliveSendTimeout_1.offer(new Event("TimeoutKeepAliveSendTimeout_1.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutKeepAliveSendTimeout_1().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutKeepAliveSendTimeout_1().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutKeepAliveSendTimeout_1 getTimeoutKeepAliveSendTimeout_1() {
		return timeoutKeepAliveSendTimeout_1;
	}
	
	public class TimeoutKeepAliveReceiveTimeout_4 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutKeepAliveReceiveTimeout_4.offer(new Event("TimeoutKeepAliveReceiveTimeout_4.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutKeepAliveReceiveTimeout_4().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutKeepAliveReceiveTimeout_4().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutKeepAliveReceiveTimeout_4 getTimeoutKeepAliveReceiveTimeout_4() {
		return timeoutKeepAliveReceiveTimeout_4;
	}
	
	public class TimeoutKapcsolodik_3 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutKapcsolodik_3.offer(new Event("TimeoutKapcsolodik_3.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutKapcsolodik_3().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutKapcsolodik_3().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutKapcsolodik_3 getTimeoutKapcsolodik_3() {
		return timeoutKapcsolodik_3;
	}
	
	public class TimeoutKeepAliveSendTimeout_0 implements SoftwareTimerInterface.Required {
		
		@Override
		public void raiseNewEvent() {
			queueOfTimeoutKeepAliveSendTimeout_0.offer(new Event("TimeoutKeepAliveSendTimeout_0.newEvent"));
		}
		
		
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			master.getTimeoutKeepAliveSendTimeout_0().registerListener(listener);
		}
		
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return master.getTimeoutKeepAliveSendTimeout_0().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TimeoutKeepAliveSendTimeout_0 getTimeoutKeepAliveSendTimeout_0() {
		return timeoutKeepAliveSendTimeout_0;
	}
	
	public class SystemStatus implements ConnectionStateInterface.Provided {
		
		
		@Override
		public boolean isRaisedConn() {
			return master.getSystemStatus().isRaisedConn();
		}
		
		@Override
		public boolean isRaisedDisconn() {
			return master.getSystemStatus().isRaisedDisconn();
		}
		
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			master.getSystemStatus().registerListener(listener);
		}
		
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return master.getSystemStatus().getRegisteredListeners();
		}
		
	}
	
	@Override
	public SystemStatus getSystemStatus() {
		return systemStatus;
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
			case "TimoeutKeepAliveReceiveTimeout_3.newEvent":
				master.getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent();
			break;
			case "TimeoutKapcsolodik_2.newEvent":
				master.getTimeoutKapcsolodik_2().raiseNewEvent();
			break;
			case "TimeoutZarva_0.newEvent":
				master.getTimeoutZarva_0().raiseNewEvent();
			break;
			case "TimeoutKeepAliveSendTimeout_1.newEvent":
				master.getTimeoutKeepAliveSendTimeout_1().raiseNewEvent();
			break;
			case "TimeoutKeepAliveReceiveTimeout_4.newEvent":
				master.getTimeoutKeepAliveReceiveTimeout_4().raiseNewEvent();
			break;
			case "TimeoutKapcsolodik_3.newEvent":
				master.getTimeoutKapcsolodik_3().raiseNewEvent();
			break;
			case "TimeoutKeepAliveSendTimeout_0.newEvent":
				master.getTimeoutKeepAliveSendTimeout_0().raiseNewEvent();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimoeutKeepAliveReceiveTimeout_3")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutKapcsolodik_2")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutZarva_0")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutKeepAliveSendTimeout_1")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutKeepAliveReceiveTimeout_4")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutKapcsolodik_3")) {
			master.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TimeoutKeepAliveSendTimeout_0")) {
			master.runCycle();
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
	
	public OrionStochSystem getMaster() {
		return master;
	}
	
	
}
