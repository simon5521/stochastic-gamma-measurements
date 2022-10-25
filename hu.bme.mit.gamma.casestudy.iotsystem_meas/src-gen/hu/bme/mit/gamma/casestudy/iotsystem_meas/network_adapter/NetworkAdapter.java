package hu.bme.mit.gamma.casestudy.iotsystem_meas.network_adapter;

import java.util.Collections;
import java.util.List;

import lbmq.*; 
import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.network.*;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.network.*;

public class NetworkAdapter implements Runnable, NetworkAdapterInterface {			
	// Thread running this wrapper instance
	private Thread thread;
	// Wrapped synchronous instance
	private Network network;
	// Control port instances
	// Wrapped port instances
	private ImageIn imageIn;
	private ImageOut imageOut;
	private ImageLoss imageLoss;
	// Main queue
	private LinkedBlockingMultiQueue<String, Event> __asyncQueue = new LinkedBlockingMultiQueue<String, Event>();
	// Subqueues
	private LinkedBlockingMultiQueue<String, Event>.SubQueue imageInQueue;
	private LinkedBlockingMultiQueue<String, Event>.SubQueue imageLossQueue;
	
	
	public boolean isEmpty(){
		return __asyncQueue.isEmpty();
	}
	
	public NetworkAdapter() {
		network = new Network();
		// Wrapped port instances
		imageIn = new ImageIn();
		imageOut = new ImageOut();
		imageLoss = new ImageLoss();
		init();
	}
	
	/** Resets the wrapped component. Must be called to initialize the component. */
	@Override
	public void reset() {
		network.reset();
	}
	
	/** Creates the subqueues, clocks and enters the wrapped synchronous component. */
	private void init() {
		network = new Network();
		// Creating subqueues: the negative conversion regarding priorities is needed,
		// because the lbmq marks higher priority with lower integer values
		__asyncQueue.addSubQueue("imageLossQueue", -(2), (int) 1);
		imageLossQueue = __asyncQueue.getSubQueue("imageLossQueue");
		__asyncQueue.addSubQueue("imageInQueue", -(1), (int) 1);
		imageInQueue = __asyncQueue.getSubQueue("imageInQueue");
		// The thread has to be started manually
	}
	
	
	// Inner classes representing control ports
	
	// Inner classes representing wrapped ports
	public class ImageIn implements ImageStreamInterface.Required {
		
		@Override
		public void raiseNewData(double blurred, boolean car) {
			imageInQueue.offer(new Event("ImageIn.newData", blurred, car));
		}
		
		
		@Override
		public void registerListener(ImageStreamInterface.Listener.Required listener) {
			network.getImageIn().registerListener(listener);
		}
		
		@Override
		public List<ImageStreamInterface.Listener.Required> getRegisteredListeners() {
			return network.getImageIn().getRegisteredListeners();
		}
		
	}
	
	@Override
	public ImageIn getImageIn() {
		return imageIn;
	}
	
	public class ImageOut implements ImageStreamInterface.Provided {
		
		
		@Override
		public boolean isRaisedNewData() {
			return network.getImageOut().isRaisedNewData();
		}
		@Override
		public double getBlurred() {
			return network.getImageOut().getBlurred();
		}
		@Override
		public boolean getCar() {
			return network.getImageOut().getCar();
		}
		
		@Override
		public void registerListener(ImageStreamInterface.Listener.Provided listener) {
			network.getImageOut().registerListener(listener);
		}
		
		@Override
		public List<ImageStreamInterface.Listener.Provided> getRegisteredListeners() {
			return network.getImageOut().getRegisteredListeners();
		}
		
	}
	
	@Override
	public ImageOut getImageOut() {
		return imageOut;
	}
	
	public class ImageLoss implements ImageStreamInterface.Required {
		
		@Override
		public void raiseNewData(double blurred, boolean car) {
			imageLossQueue.offer(new Event("ImageLoss.newData", blurred, car));
		}
		
		
		@Override
		public void registerListener(ImageStreamInterface.Listener.Required listener) {
			network.getImageLoss().registerListener(listener);
		}
		
		@Override
		public List<ImageStreamInterface.Listener.Required> getRegisteredListeners() {
			return network.getImageLoss().getRegisteredListeners();
		}
		
	}
	
	@Override
	public ImageLoss getImageLoss() {
		return imageLoss;
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
			case "ImageIn.newData":
				network.getImageIn().raiseNewData( (double) event.getValue()[0],  (boolean) event.getValue()[1]);
			break;
			case "ImageLoss.newData":
				network.getImageLoss().raiseNewData( (double) event.getValue()[0],  (boolean) event.getValue()[1]);
			break;
			default:
				throw new IllegalArgumentException("No such event!");
		}
	}
	
	private void performControlActions(Event event) {
		String[] eventName = event.getEvent().split("\\.");
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("ImageIn")) {
			network.runCycle();
			return;
		}
		// Port trigger
		if (eventName.length == 2 && eventName[0].equals("ImageLoss")) {
			network.runCycle();
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
	
	public Network getNetwork() {
		return network;
	}
	
	
}
