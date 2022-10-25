package hu.bme.mit.gamma.casestudy.iotsystem.edge_adapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.casestudy.iotsystem.*;

import hu.bme.mit.gamma.casestudy.iotsystem.cloud.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

import hu.bme.mit.gamma.casestudy.iotsystem.cloud.*;

public class EdgeAdapter implements Runnable, EdgeAdapterInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private Edge edge;
	// Control port instances
	// Wrapped port instances
	private Camera camera;
	private TrafficStream trafficStream;
	private LostImage lostImage;
	private CarLeave carLeave;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue trafficQueue;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue cameraQueue;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public EdgeAdapter() {
		edge = new Edge();
		// Wrapped port instances
		camera = new Camera();
		trafficStream = new TrafficStream();
		lostImage = new LostImage();
		carLeave = new CarLeave();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		edge.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		edge = new Edge();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("cameraQueue", -(2), (int) 1);
		cameraQueue = __asyncQueue.getSubQueue("cameraQueue");
		__asyncQueue.addSubQueue("trafficQueue", -(1), (int) 1);
		trafficQueue = __asyncQueue.getSubQueue("trafficQueue");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class Camera implements ImageStreamInterface.Required {
		
		@Override
		public void raiseNewData(double blurred, boolean car) {
			cameraQueue.offer(new Event("Camera.newData", blurred, car));
		}
		
		
		@Override
		public void registerListener(ImageStreamInterface.Listener.Required listener) {
			edge.getCamera().registerListener(listener);
		}
		
		@Override
		public List<ImageStreamInterface.Listener.Required> getRegisteredListeners() {
			return edge.getCamera().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Camera getCamera() {
		return camera;
	}
	
	public class TrafficStream implements TrafficEventStreamInterface.Required {
		
		@Override
		public void raiseCarArrives() {
			trafficQueue.offer(new Event("TrafficStream.carArrives"));
		}
		@Override
		public void raiseCarLeaves() {
			trafficQueue.offer(new Event("TrafficStream.carLeaves"));
		}
		
		
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Required listener) {
			edge.getTrafficStream().registerListener(listener);
		}
		
		@Override
		public List<TrafficEventStreamInterface.Listener.Required> getRegisteredListeners() {
			return edge.getTrafficStream().getRegisteredListeners();
		}
		
	}
	
	@Override
	public TrafficStream getTrafficStream() {
		return trafficStream;
	}
	
	public class LostImage implements EventStreamInterface.Provided {
		
		
		@Override
		public boolean isRaisedNewEvent() {
			return edge.getLostImage().isRaisedNewEvent();
		}
		
		@Override
		public void registerListener(EventStreamInterface.Listener.Provided listener) {
			edge.getLostImage().registerListener(listener);
		}
		
		@Override
		public List<EventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return edge.getLostImage().getRegisteredListeners();
		}
		
	}
	
	@Override
	public LostImage getLostImage() {
		return lostImage;
	}
	
	public class CarLeave implements EventStreamInterface.Provided {
		
		
		@Override
		public boolean isRaisedNewEvent() {
			return edge.getCarLeave().isRaisedNewEvent();
		}
		
		@Override
		public void registerListener(EventStreamInterface.Listener.Provided listener) {
			edge.getCarLeave().registerListener(listener);
		}
		
		@Override
		public List<EventStreamInterface.Listener.Provided> getRegisteredListeners() {
			return edge.getCarLeave().getRegisteredListeners();
		}
		
	}
	
	@Override
	public CarLeave getCarLeave() {
		return carLeave;
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
			case "Camera.newData":
				edge.getCamera().raiseNewData( (double) event.getValue()[0],  (boolean) event.getValue()[1]);
			break;
			case "TrafficStream.carArrives":
				edge.getTrafficStream().raiseCarArrives();
			break;
			case "TrafficStream.carLeaves":
				edge.getTrafficStream().raiseCarLeaves();
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("TrafficStream")) {
			edge.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("Camera")) {
			edge.runCycle();
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
	
	public Edge getEdge() {
		return edge;
	}
	
	
}
