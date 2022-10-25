package hu.bme.mit.gamma.casestudy.iotsystem.iotsystem;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic.*;
import hu.bme.mit.gamma.casestudy.iotsystem.network_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem.network.*;
import hu.bme.mit.gamma.casestudy.iotsystem.cloud.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_software.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_driver.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_component.*;
import hu.bme.mit.gamma.casestudy.iotsystem.traffic_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem.edge_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem.camera_control.*;
import hu.bme.mit.gamma.casestudy.iotsystem.channels.*;


public class IoTSystem implements IoTSystemInterface {
	// Component instances
	private CameraComponent camera1;
	private CameraComponent camera2;
	private EdgeAdapter edge;
	private RoadTraffic traffic;
	// Environmental Component instances
	// Port instances
	private Failures failures = new Failures();
	private CarLeave carLeave = new CarLeave();
	// Channel instances
	private ImageStreamChannelInterface channelImagesOfCamera1;
	private ImageStreamChannelInterface channelImagesOfCamera2;
	private TrafficEventStreamChannelInterface channelCarsOfTraffic;
	
	public boolean isEmpty(){
		return  camera1.isEmpty()  &&  camera2.isEmpty()  &&  edge.isEmpty()  &&  traffic.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			camera1.schedule();
			camera2.schedule();
			edge.schedule();
			traffic.schedule();
		}
	}
	
	public IoTSystem() {
		camera1 = new CameraComponent();
		camera2 = new CameraComponent();
		edge = new EdgeAdapter();
		traffic = new RoadTraffic();
		failures = new Failures();
		carLeave = new CarLeave();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		camera1.reset();
		camera2.reset();
		edge.reset();
		traffic.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		channelImagesOfCamera1 = new ImageStreamChannel(camera1.getImages());
		channelImagesOfCamera1.registerPort(edge.getCamera());
		channelImagesOfCamera2 = new ImageStreamChannel(camera2.getImages());
		channelImagesOfCamera2.registerPort(edge.getCamera());
		// Registration of broadcast channels
		channelCarsOfTraffic = new TrafficEventStreamChannel(traffic.getCars());
			channelCarsOfTraffic.registerPort(camera1.getTrafficSensing());
			channelCarsOfTraffic.registerPort(edge.getTrafficStream());
			channelCarsOfTraffic.registerPort(camera2.getTrafficSensing());
	}
	
	// Inner classes representing Ports
	public class Failures implements EventStreamInterface.Provided {
	
		
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
	public Failures getFailures() {
		return failures;
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
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		camera1.start();
		camera2.start();
		edge.start();
		traffic.start();
	}
	
	public boolean isWaiting() {
		return camera1.isWaiting() && camera2.isWaiting() && edge.isWaiting() && traffic.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public CameraComponent getCamera1() {
		return camera1;
	}
	
	public CameraComponent getCamera2() {
		return camera2;
	}
	
	public EdgeAdapter getEdge() {
		return edge;
	}
	
	public RoadTraffic getTraffic() {
		return traffic;
	}
	
}
