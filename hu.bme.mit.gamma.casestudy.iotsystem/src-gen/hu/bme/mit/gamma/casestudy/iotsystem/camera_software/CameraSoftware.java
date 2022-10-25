package hu.bme.mit.gamma.casestudy.iotsystem.camera_software;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_control.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_driver.*;


public class CameraSoftware implements CameraSoftwareInterface {
	// Component instances
	private CameraDriver cameraDriver;
	private CameraControl cameraControl;
	// Port instances
	private TrafficSensing trafficSensing;
	private Images images;
	
	
	
	public CameraSoftware() {
		cameraDriver = new CameraDriver();
		cameraControl = new CameraControl();
		images = new Images();
		trafficSensing = new TrafficSensing();
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		cameraDriver.reset();
		cameraControl.reset();
		// Setting only a single queue for cascade statecharts
		cameraDriver.changeInsertQueue();
		cameraControl.changeInsertQueue();
		clearPorts();
		// Initializing chain of listeners and events 
		notifyAllListeners();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {
		// Registration of simple channels
		cameraControl.getRequests().registerListener(cameraDriver.getRequests());
		cameraDriver.getRequests().registerListener(cameraControl.getRequests());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class TrafficSensing implements TrafficEventStreamInterface.Required,TrafficEventStreamInterface.Listener.Required {
		private List<TrafficEventStreamInterface.Listener.Required> listeners = new LinkedList<TrafficEventStreamInterface.Listener.Required>();
		
		public TrafficSensing() {
			// Registering the listener to the contained component
			cameraDriver.getTraffic().registerListener(new TrafficSensingUtil());
		}
		
		@Override
		public void raiseCarArrives() {
			cameraDriver.getTraffic().raiseCarArrives();
		}
		
		@Override
		public void raiseCarLeaves() {
			cameraDriver.getTraffic().raiseCarLeaves();
		}
		
		
		
		// Class for the setting of the boolean fields (events)
		private class TrafficSensingUtil implements TrafficEventStreamInterface.Listener.Required {
		}
		
		@Override
		public void registerListener(TrafficEventStreamInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<TrafficEventStreamInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
		}
		
	}
	
	@Override
	public TrafficSensing getTrafficSensing() {
		return trafficSensing;
	}
	
	public class Images implements ImageStreamInterface.Provided,ImageStreamInterface.Listener.Provided {
		private List<ImageStreamInterface.Listener.Provided> listeners = new LinkedList<ImageStreamInterface.Listener.Provided>();
		boolean isRaisedNewData;
		double blurred;
		boolean car;
		
		public Images() {
			// Registering the listener to the contained component
			cameraControl.getNetworkImages().registerListener(new ImagesUtil());
		}
		
		
		@Override
		public boolean isRaisedNewData() {
			return isRaisedNewData;
		}
		@Override
		public double getBlurred() {
			return blurred;
		}
		@Override
		public boolean getCar() {
			return car;
		}
		@Override
		public void raiseNewData(double blurred, boolean car) {
			isRaisedNewData = true;
			Images.this.blurred = blurred;
			Images.this.car = car;
		}
		
		
		// Class for the setting of the boolean fields (events)
		private class ImagesUtil implements ImageStreamInterface.Listener.Provided {
			@Override
			public void raiseNewData(double blurred, boolean car) {
				isRaisedNewData = true;
				Images.this.blurred = blurred;
				Images.this.car = car;
			}
		}
		
		@Override
		public void registerListener(ImageStreamInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		
		@Override
		public List<ImageStreamInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
		
		/** Resetting the boolean event flags to false. */
		public void clear() {
			isRaisedNewData = false;
		}
		
		/** Notifying the registered listeners. */
		public void notifyListeners() {
			if (isRaisedNewData) {
				for (ImageStreamInterface.Listener.Provided listener : listeners) {
					listener.raiseNewData(blurred, car);
				}
			}
		}
		
	}
	
	@Override
	public Images getImages() {
		return images;
	}
	
	/** Clears the the boolean flags of all out-events in each contained port. */
	private void clearPorts() {
		getImages().clear();
		getTrafficSensing().clear();
	}
	
	/** Notifies all registered listeners in each contained port. */
	public void notifyAllListeners() {
		cameraDriver.notifyAllListeners();
		cameraControl.notifyAllListeners();

		notifyListeners();
	}
	
	public void notifyListeners() {
		getImages().notifyListeners();
		getTrafficSensing().notifyListeners();
	}
	
	
	/** Returns whether all event queues of the contained component instances are empty. 
	Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return cameraDriver.isEventQueueEmpty() && cameraControl.isEventQueueEmpty();
	}
	
	/** Initiates cycle runs until all event queues of component instances are empty. */
	@Override
	public void runFullCycle() {
		do {
			runCycle();
		}
		while (!isEventQueueEmpty());
	}
	
	/** Changes event queues and initiates a cycle run.
	 * This should be the execution point from an asynchronous component. */
	@Override
	public void runCycle() {
		// Composite type-dependent behavior
		runComponent();
	}
	
	/** Initiates a cycle run without changing the event queues.
	 * Should be used only be the container (composite system) class. */
	public void runComponent() {
		// Running contained components
		cameraControl.runComponent();
		cameraControl.runComponent();
		cameraControl.runComponent();
		cameraDriver.runComponent();
		cameraDriver.runComponent();
		cameraDriver.runComponent();
		cameraControl.runComponent();
		cameraControl.runComponent();
		cameraControl.runComponent();
		// Notifying registered listeners
		notifyListeners();
		
		// Ends with the clearing of the out-event flags
		clearPorts();
	}

	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public CameraDriver getCameraDriver() {
		return cameraDriver;
	}
	
	public CameraControl getCameraControl() {
		return cameraControl;
	}
	
	
	
}
