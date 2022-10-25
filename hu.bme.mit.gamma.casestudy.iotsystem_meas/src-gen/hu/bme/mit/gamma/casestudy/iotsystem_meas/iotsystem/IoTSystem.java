package hu.bme.mit.gamma.casestudy.iotsystem_meas.iotsystem;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_control.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.cloud.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.edge_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.network_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_driver.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_component.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.network.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic_sct.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_software.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.traffic.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_adapter.*;
import hu.bme.mit.gamma.casestudy.iotsystem_meas.channels.*;


public class IoTSystem implements IoTSystemInterface {
	// Component instances
	private CameraComponent camera1_;
	private CameraComponent camera2_;
	private CameraComponent camera3_;
	private CameraComponent camera4_;
	private CameraComponent camera5_;
	private CameraComponent camera6_;
	private CameraComponent camera7_;
	private CameraComponent camera8_;
	private CameraComponent camera9_;
	private CameraComponent camera10;
	private CameraComponent camera11;
	private CameraComponent camera12;
	private CameraComponent camera13;
	private CameraComponent camera14;
	private CameraComponent camera15;
	private CameraComponent camera16;
	private CameraComponent camera17;
	private CameraComponent camera18;
	private CameraComponent camera19;
	private CameraComponent camera20;
	private CameraComponent camera21;
	private CameraComponent camera22;
	private CameraComponent camera23;
	private CameraComponent camera24;
	private CameraComponent camera25;
	private CameraComponent camera26;
	private CameraComponent camera27;
	private CameraComponent camera28;
	private CameraComponent camera29;
	private CameraComponent camera30;
	private CameraComponent camera31;
	private CameraComponent camera32;
	private EdgeAdapter edge;
	private RoadTraffic traffic;
	// Environmental Component instances
	// Port instances
	private Failures failures = new Failures();
	// Channel instances
	private ImageStreamChannelInterface channelImagesOfCamera14;
	private ImageStreamChannelInterface channelImagesOfCamera27;
	private ImageStreamChannelInterface channelImagesOfCamera17;
	private ImageStreamChannelInterface channelImagesOfCamera13;
	private ImageStreamChannelInterface channelImagesOfCamera10;
	private ImageStreamChannelInterface channelImagesOfCamera7_;
	private ImageStreamChannelInterface channelImagesOfCamera26;
	private ImageStreamChannelInterface channelImagesOfCamera16;
	private ImageStreamChannelInterface channelImagesOfCamera28;
	private ImageStreamChannelInterface channelImagesOfCamera32;
	private ImageStreamChannelInterface channelImagesOfCamera5_;
	private ImageStreamChannelInterface channelImagesOfCamera23;
	private ImageStreamChannelInterface channelImagesOfCamera8_;
	private ImageStreamChannelInterface channelImagesOfCamera15;
	private ImageStreamChannelInterface channelImagesOfCamera29;
	private ImageStreamChannelInterface channelImagesOfCamera2_;
	private ImageStreamChannelInterface channelImagesOfCamera21;
	private ImageStreamChannelInterface channelImagesOfCamera25;
	private ImageStreamChannelInterface channelImagesOfCamera9_;
	private ImageStreamChannelInterface channelImagesOfCamera31;
	private ImageStreamChannelInterface channelImagesOfCamera1_;
	private ImageStreamChannelInterface channelImagesOfCamera30;
	private ImageStreamChannelInterface channelImagesOfCamera24;
	private ImageStreamChannelInterface channelImagesOfCamera19;
	private ImageStreamChannelInterface channelImagesOfCamera4_;
	private ImageStreamChannelInterface channelImagesOfCamera18;
	private ImageStreamChannelInterface channelImagesOfCamera22;
	private ImageStreamChannelInterface channelImagesOfCamera12;
	private ImageStreamChannelInterface channelImagesOfCamera20;
	private ImageStreamChannelInterface channelImagesOfCamera11;
	private ImageStreamChannelInterface channelImagesOfCamera6_;
	private ImageStreamChannelInterface channelImagesOfCamera3_;
	private TrafficEventStreamChannelInterface channelCarsOfTraffic;
	
	public boolean isEmpty(){
		return  camera1_.isEmpty()  &&  camera2_.isEmpty()  &&  camera3_.isEmpty()  &&  camera4_.isEmpty()  &&  camera5_.isEmpty()  &&  camera6_.isEmpty()  &&  camera7_.isEmpty()  &&  camera8_.isEmpty()  &&  camera9_.isEmpty()  &&  camera10.isEmpty()  &&  camera11.isEmpty()  &&  camera12.isEmpty()  &&  camera13.isEmpty()  &&  camera14.isEmpty()  &&  camera15.isEmpty()  &&  camera16.isEmpty()  &&  camera17.isEmpty()  &&  camera18.isEmpty()  &&  camera19.isEmpty()  &&  camera20.isEmpty()  &&  camera21.isEmpty()  &&  camera22.isEmpty()  &&  camera23.isEmpty()  &&  camera24.isEmpty()  &&  camera25.isEmpty()  &&  camera26.isEmpty()  &&  camera27.isEmpty()  &&  camera28.isEmpty()  &&  camera29.isEmpty()  &&  camera30.isEmpty()  &&  camera31.isEmpty()  &&  camera32.isEmpty()  &&  edge.isEmpty()  &&  traffic.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			camera1_.schedule();
			camera2_.schedule();
			camera3_.schedule();
			camera4_.schedule();
			camera5_.schedule();
			camera6_.schedule();
			camera7_.schedule();
			camera8_.schedule();
			camera9_.schedule();
			camera10.schedule();
			camera11.schedule();
			camera12.schedule();
			camera13.schedule();
			camera14.schedule();
			camera15.schedule();
			camera16.schedule();
			camera17.schedule();
			camera18.schedule();
			camera19.schedule();
			camera20.schedule();
			camera21.schedule();
			camera22.schedule();
			camera23.schedule();
			camera24.schedule();
			camera25.schedule();
			camera26.schedule();
			camera27.schedule();
			camera28.schedule();
			camera29.schedule();
			camera30.schedule();
			camera31.schedule();
			camera32.schedule();
			edge.schedule();
			traffic.schedule();
		}
	}
	
	public IoTSystem() {
		camera1_ = new CameraComponent();
		camera2_ = new CameraComponent();
		camera3_ = new CameraComponent();
		camera4_ = new CameraComponent();
		camera5_ = new CameraComponent();
		camera6_ = new CameraComponent();
		camera7_ = new CameraComponent();
		camera8_ = new CameraComponent();
		camera9_ = new CameraComponent();
		camera10 = new CameraComponent();
		camera11 = new CameraComponent();
		camera12 = new CameraComponent();
		camera13 = new CameraComponent();
		camera14 = new CameraComponent();
		camera15 = new CameraComponent();
		camera16 = new CameraComponent();
		camera17 = new CameraComponent();
		camera18 = new CameraComponent();
		camera19 = new CameraComponent();
		camera20 = new CameraComponent();
		camera21 = new CameraComponent();
		camera22 = new CameraComponent();
		camera23 = new CameraComponent();
		camera24 = new CameraComponent();
		camera25 = new CameraComponent();
		camera26 = new CameraComponent();
		camera27 = new CameraComponent();
		camera28 = new CameraComponent();
		camera29 = new CameraComponent();
		camera30 = new CameraComponent();
		camera31 = new CameraComponent();
		camera32 = new CameraComponent();
		edge = new EdgeAdapter();
		traffic = new RoadTraffic();
		failures = new Failures();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		camera1_.reset();
		camera2_.reset();
		camera3_.reset();
		camera4_.reset();
		camera5_.reset();
		camera6_.reset();
		camera7_.reset();
		camera8_.reset();
		camera9_.reset();
		camera10.reset();
		camera11.reset();
		camera12.reset();
		camera13.reset();
		camera14.reset();
		camera15.reset();
		camera16.reset();
		camera17.reset();
		camera18.reset();
		camera19.reset();
		camera20.reset();
		camera21.reset();
		camera22.reset();
		camera23.reset();
		camera24.reset();
		camera25.reset();
		camera26.reset();
		camera27.reset();
		camera28.reset();
		camera29.reset();
		camera30.reset();
		camera31.reset();
		camera32.reset();
		edge.reset();
		traffic.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		channelImagesOfCamera19 = new ImageStreamChannel(camera19.getImages());
		channelImagesOfCamera19.registerPort(edge.getCamera());
		channelImagesOfCamera16 = new ImageStreamChannel(camera16.getImages());
		channelImagesOfCamera16.registerPort(edge.getCamera());
		channelImagesOfCamera12 = new ImageStreamChannel(camera12.getImages());
		channelImagesOfCamera12.registerPort(edge.getCamera());
		channelImagesOfCamera8_ = new ImageStreamChannel(camera8_.getImages());
		channelImagesOfCamera8_.registerPort(edge.getCamera());
		channelImagesOfCamera18 = new ImageStreamChannel(camera18.getImages());
		channelImagesOfCamera18.registerPort(edge.getCamera());
		channelImagesOfCamera2_ = new ImageStreamChannel(camera2_.getImages());
		channelImagesOfCamera2_.registerPort(edge.getCamera());
		channelImagesOfCamera3_ = new ImageStreamChannel(camera3_.getImages());
		channelImagesOfCamera3_.registerPort(edge.getCamera());
		channelImagesOfCamera21 = new ImageStreamChannel(camera21.getImages());
		channelImagesOfCamera21.registerPort(edge.getCamera());
		channelImagesOfCamera14 = new ImageStreamChannel(camera14.getImages());
		channelImagesOfCamera14.registerPort(edge.getCamera());
		channelImagesOfCamera7_ = new ImageStreamChannel(camera7_.getImages());
		channelImagesOfCamera7_.registerPort(edge.getCamera());
		channelImagesOfCamera24 = new ImageStreamChannel(camera24.getImages());
		channelImagesOfCamera24.registerPort(edge.getCamera());
		channelImagesOfCamera25 = new ImageStreamChannel(camera25.getImages());
		channelImagesOfCamera25.registerPort(edge.getCamera());
		channelImagesOfCamera17 = new ImageStreamChannel(camera17.getImages());
		channelImagesOfCamera17.registerPort(edge.getCamera());
		channelImagesOfCamera29 = new ImageStreamChannel(camera29.getImages());
		channelImagesOfCamera29.registerPort(edge.getCamera());
		channelImagesOfCamera15 = new ImageStreamChannel(camera15.getImages());
		channelImagesOfCamera15.registerPort(edge.getCamera());
		channelImagesOfCamera23 = new ImageStreamChannel(camera23.getImages());
		channelImagesOfCamera23.registerPort(edge.getCamera());
		channelImagesOfCamera32 = new ImageStreamChannel(camera32.getImages());
		channelImagesOfCamera32.registerPort(edge.getCamera());
		channelImagesOfCamera9_ = new ImageStreamChannel(camera9_.getImages());
		channelImagesOfCamera9_.registerPort(edge.getCamera());
		channelImagesOfCamera5_ = new ImageStreamChannel(camera5_.getImages());
		channelImagesOfCamera5_.registerPort(edge.getCamera());
		channelImagesOfCamera13 = new ImageStreamChannel(camera13.getImages());
		channelImagesOfCamera13.registerPort(edge.getCamera());
		channelImagesOfCamera6_ = new ImageStreamChannel(camera6_.getImages());
		channelImagesOfCamera6_.registerPort(edge.getCamera());
		channelImagesOfCamera4_ = new ImageStreamChannel(camera4_.getImages());
		channelImagesOfCamera4_.registerPort(edge.getCamera());
		channelImagesOfCamera26 = new ImageStreamChannel(camera26.getImages());
		channelImagesOfCamera26.registerPort(edge.getCamera());
		channelImagesOfCamera30 = new ImageStreamChannel(camera30.getImages());
		channelImagesOfCamera30.registerPort(edge.getCamera());
		channelImagesOfCamera11 = new ImageStreamChannel(camera11.getImages());
		channelImagesOfCamera11.registerPort(edge.getCamera());
		channelImagesOfCamera28 = new ImageStreamChannel(camera28.getImages());
		channelImagesOfCamera28.registerPort(edge.getCamera());
		channelImagesOfCamera27 = new ImageStreamChannel(camera27.getImages());
		channelImagesOfCamera27.registerPort(edge.getCamera());
		channelImagesOfCamera31 = new ImageStreamChannel(camera31.getImages());
		channelImagesOfCamera31.registerPort(edge.getCamera());
		channelImagesOfCamera22 = new ImageStreamChannel(camera22.getImages());
		channelImagesOfCamera22.registerPort(edge.getCamera());
		channelImagesOfCamera10 = new ImageStreamChannel(camera10.getImages());
		channelImagesOfCamera10.registerPort(edge.getCamera());
		channelImagesOfCamera20 = new ImageStreamChannel(camera20.getImages());
		channelImagesOfCamera20.registerPort(edge.getCamera());
		channelImagesOfCamera1_ = new ImageStreamChannel(camera1_.getImages());
		channelImagesOfCamera1_.registerPort(edge.getCamera());
		// Registration of broadcast channels
		channelCarsOfTraffic = new TrafficEventStreamChannel(traffic.getCars());
			channelCarsOfTraffic.registerPort(camera11.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera3_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera26.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera22.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera24.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera15.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera25.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera32.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera5_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera18.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera29.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera17.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera21.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera10.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera20.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera19.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera6_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera30.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera9_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera2_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera31.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera16.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera14.getTrafficSensing());
			channelCarsOfTraffic.registerPort(edge.getTrafficStream());
			channelCarsOfTraffic.registerPort(camera7_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera12.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera28.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera23.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera27.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera8_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera4_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera1_.getTrafficSensing());
			channelCarsOfTraffic.registerPort(camera13.getTrafficSensing());
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
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		camera1_.start();
		camera2_.start();
		camera3_.start();
		camera4_.start();
		camera5_.start();
		camera6_.start();
		camera7_.start();
		camera8_.start();
		camera9_.start();
		camera10.start();
		camera11.start();
		camera12.start();
		camera13.start();
		camera14.start();
		camera15.start();
		camera16.start();
		camera17.start();
		camera18.start();
		camera19.start();
		camera20.start();
		camera21.start();
		camera22.start();
		camera23.start();
		camera24.start();
		camera25.start();
		camera26.start();
		camera27.start();
		camera28.start();
		camera29.start();
		camera30.start();
		camera31.start();
		camera32.start();
		edge.start();
		traffic.start();
	}
	
	public boolean isWaiting() {
		return camera1_.isWaiting() && camera2_.isWaiting() && camera3_.isWaiting() && camera4_.isWaiting() && camera5_.isWaiting() && camera6_.isWaiting() && camera7_.isWaiting() && camera8_.isWaiting() && camera9_.isWaiting() && camera10.isWaiting() && camera11.isWaiting() && camera12.isWaiting() && camera13.isWaiting() && camera14.isWaiting() && camera15.isWaiting() && camera16.isWaiting() && camera17.isWaiting() && camera18.isWaiting() && camera19.isWaiting() && camera20.isWaiting() && camera21.isWaiting() && camera22.isWaiting() && camera23.isWaiting() && camera24.isWaiting() && camera25.isWaiting() && camera26.isWaiting() && camera27.isWaiting() && camera28.isWaiting() && camera29.isWaiting() && camera30.isWaiting() && camera31.isWaiting() && camera32.isWaiting() && edge.isWaiting() && traffic.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public CameraComponent getCamera1_() {
		return camera1_;
	}
	
	public CameraComponent getCamera2_() {
		return camera2_;
	}
	
	public CameraComponent getCamera3_() {
		return camera3_;
	}
	
	public CameraComponent getCamera4_() {
		return camera4_;
	}
	
	public CameraComponent getCamera5_() {
		return camera5_;
	}
	
	public CameraComponent getCamera6_() {
		return camera6_;
	}
	
	public CameraComponent getCamera7_() {
		return camera7_;
	}
	
	public CameraComponent getCamera8_() {
		return camera8_;
	}
	
	public CameraComponent getCamera9_() {
		return camera9_;
	}
	
	public CameraComponent getCamera10() {
		return camera10;
	}
	
	public CameraComponent getCamera11() {
		return camera11;
	}
	
	public CameraComponent getCamera12() {
		return camera12;
	}
	
	public CameraComponent getCamera13() {
		return camera13;
	}
	
	public CameraComponent getCamera14() {
		return camera14;
	}
	
	public CameraComponent getCamera15() {
		return camera15;
	}
	
	public CameraComponent getCamera16() {
		return camera16;
	}
	
	public CameraComponent getCamera17() {
		return camera17;
	}
	
	public CameraComponent getCamera18() {
		return camera18;
	}
	
	public CameraComponent getCamera19() {
		return camera19;
	}
	
	public CameraComponent getCamera20() {
		return camera20;
	}
	
	public CameraComponent getCamera21() {
		return camera21;
	}
	
	public CameraComponent getCamera22() {
		return camera22;
	}
	
	public CameraComponent getCamera23() {
		return camera23;
	}
	
	public CameraComponent getCamera24() {
		return camera24;
	}
	
	public CameraComponent getCamera25() {
		return camera25;
	}
	
	public CameraComponent getCamera26() {
		return camera26;
	}
	
	public CameraComponent getCamera27() {
		return camera27;
	}
	
	public CameraComponent getCamera28() {
		return camera28;
	}
	
	public CameraComponent getCamera29() {
		return camera29;
	}
	
	public CameraComponent getCamera30() {
		return camera30;
	}
	
	public CameraComponent getCamera31() {
		return camera31;
	}
	
	public CameraComponent getCamera32() {
		return camera32;
	}
	
	public EdgeAdapter getEdge() {
		return edge;
	}
	
	public RoadTraffic getTraffic() {
		return traffic;
	}
	
}
