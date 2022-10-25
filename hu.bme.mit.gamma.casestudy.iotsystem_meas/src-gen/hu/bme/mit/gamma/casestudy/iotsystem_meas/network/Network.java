package hu.bme.mit.gamma.casestudy.iotsystem_meas.network;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.TimerInterface.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.network.NetworkStatemachine.*;

public class Network implements NetworkInterface {
	// Port instances
	private ImageIn imageIn = new ImageIn();
	private ImageOut imageOut = new ImageOut();
	private ImageLoss imageLoss = new ImageLoss();
	// Wrapped statemachine
	private NetworkStatemachine network;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Network() {
		network = new NetworkStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		network.reset();
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
	
	public class ImageIn implements ImageStreamInterface.Required {
		private List<ImageStreamInterface.Listener.Required> listeners = new LinkedList<ImageStreamInterface.Listener.Required>();
		@Override
		public void raiseNewData(double blurred, boolean car) {
			getInsertQueue().add(new Event("ImageIn.newData", blurred, car));
		}
		@Override
		public void registerListener(ImageStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<ImageStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public ImageIn getImageIn() {
		return imageIn;
	}
	
	public class ImageOut implements ImageStreamInterface.Provided {
		private List<ImageStreamInterface.Listener.Provided> listeners = new LinkedList<ImageStreamInterface.Listener.Provided>();
		@Override
		public boolean isRaisedNewData() {
			return network.getImageOut_newData_Out();
		}
		@Override
		public double getBlurred() {
			return network.getImageOut_newData_Out_blurred();
		}
		@Override
		public boolean getCar() {
			return network.getImageOut_newData_Out_car();
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
	
	public ImageOut getImageOut() {
		return imageOut;
	}
	
	public class ImageLoss implements ImageStreamInterface.Required {
		private List<ImageStreamInterface.Listener.Required> listeners = new LinkedList<ImageStreamInterface.Listener.Required>();
		@Override
		public void raiseNewData(double blurred, boolean car) {
			getInsertQueue().add(new Event("ImageLoss.newData", blurred, car));
		}
		@Override
		public void registerListener(ImageStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<ImageStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public ImageLoss getImageLoss() {
		return imageLoss;
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
				case "ImageIn.newData": 
					network.setImageIn_newData_In(true);
					network.setImageIn_newData_In_blurred(((double) event.getValue()[0]));
					network.setImageIn_newData_In_car(((boolean) event.getValue()[1]));
				break;
				case "ImageLoss.newData": 
					network.setImageLoss_newData_In(true);
					network.setImageLoss_newData_In_blurred(((double) event.getValue()[0]));
					network.setImageLoss_newData_In_car(((boolean) event.getValue()[1]));
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		network.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (imageOut.isRaisedNewData()) {
			for (ImageStreamInterface.Listener.Provided listener : imageOut.getRegisteredListeners()) {
				listener.raiseNewData(network.getImageOut_newData_Out_blurred(), network.getImageOut_newData_Out_car());
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "main":
				return network.getMain() == Main.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return network.toString();
	}
}
