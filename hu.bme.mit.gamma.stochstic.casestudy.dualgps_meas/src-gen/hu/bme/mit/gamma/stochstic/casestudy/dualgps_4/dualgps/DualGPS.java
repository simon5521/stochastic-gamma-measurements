package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.dualgps;

import java.util.List;
import java.util.LinkedList;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voteradapter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gpsadapter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gps.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.channels.*;


public class DualGPS implements DualGPSInterface {
	// Component instances
	private Voterasync Voter;
	private GPSasync GPS1_;
	private GPSasync GPS2_;
	private GPSasync GPS3_;
	private GPSasync GPS4_;
	private GPSasync GPS5_;
	private GPSasync GPS6_;
	private GPSasync GPS7_;
	private GPSasync GPS8_;
	private GPSasync GPS9_;
	private GPSasync GPS10;
	private GPSasync GPS11;
	private GPSasync GPS12;
	private GPSasync GPS13;
	private GPSasync GPS14;
	private GPSasync GPS15;
	private GPSasync GPS16;
	private GPSasync GPS17;
	private GPSasync GPS18;
	private GPSasync GPS19;
	private GPSasync GPS20;
	private GPSasync GPS21;
	private GPSasync GPS22;
	private GPSasync GPS23;
	private GPSasync GPS24;
	private GPSasync GPS25;
	private GPSasync GPS26;
	private GPSasync GPS27;
	private GPSasync GPS28;
	private GPSasync GPS29;
	private GPSasync GPS30;
	private GPSasync GPS31;
	private GPSasync GPS32;
	// Environmental Component instances
	// Port instances
	private Communication communication = new Communication();
	// Channel instances
	private SensorChannelInterface channelCommunicationOfGPS21;
	private HardwareFailureChannelInterface channelFaultsOfGPS21_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS18_Failure;
	private SensorChannelInterface channelCommunicationOfGPS29;
	private SensorChannelInterface channelCommunicationOfGPS9_;
	private HardwareFailureChannelInterface channelFaultsOfGPS9__Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS29_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS24_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS7__Failure;
	private SensorChannelInterface channelCommunicationOfGPS25;
	private HardwareFailureChannelInterface channelFaultsOfGPS27_Failure;
	private SensorChannelInterface channelCommunicationOfGPS32;
	private HardwareFailureChannelInterface channelFaultsOfGPS23_Failure;
	private SensorChannelInterface channelCommunicationOfGPS4_;
	private HardwareFailureChannelInterface channelFaultsOfGPS6__Failure;
	private HardwareFailureChannelInterface channelFaultsOfVoter_Failure;
	private SensorChannelInterface channelCommunicationOfGPS12;
	private SensorChannelInterface channelCommunicationOfGPS8_;
	private HardwareFailureChannelInterface channelFaultsOfGPS3__Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS14_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS28_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS17_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS10_Failure;
	private SensorChannelInterface channelCommunicationOfGPS6_;
	private HardwareFailureChannelInterface channelFaultsOfGPS31_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS11_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS22_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS32_Failure;
	private SensorChannelInterface channelCommunicationOfGPS27;
	private HardwareFailureChannelInterface channelFaultsOfGPS12_Failure;
	private SensorChannelInterface channelCommunicationOfGPS3_;
	private SensorChannelInterface channelCommunicationOfGPS13;
	private SensorChannelInterface channelCommunicationOfGPS31;
	private HardwareFailureChannelInterface channelFaultsOfGPS19_Failure;
	private SensorChannelInterface channelCommunicationOfGPS2_;
	private HardwareFailureChannelInterface channelFaultsOfGPS16_Failure;
	private SensorChannelInterface channelCommunicationOfGPS18;
	private HardwareFailureChannelInterface channelFaultsOfGPS13_Failure;
	private SensorChannelInterface channelCommunicationOfGPS24;
	private SensorChannelInterface channelCommunicationOfGPS26;
	private SensorChannelInterface channelCommunicationOfGPS14;
	private SensorChannelInterface channelCommunicationOfGPS30;
	private HardwareFailureChannelInterface channelFaultsOfGPS4__Failure;
	private SensorChannelInterface channelCommunicationOfGPS22;
	private SensorChannelInterface channelCommunicationOfGPS15;
	private SensorChannelInterface channelCommunicationOfGPS10;
	private SensorChannelInterface channelCommunicationOfGPS19;
	private SensorChannelInterface channelCommunicationOfGPS1_;
	private SensorChannelInterface channelCommunicationOfGPS23;
	private SensorChannelInterface channelCommunicationOfGPS16;
	private HardwareFailureChannelInterface channelFaultsOfGPS2__Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS26_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS15_Failure;
	private SensorChannelInterface channelCommunicationOfGPS11;
	private SensorChannelInterface channelCommunicationOfGPS17;
	private HardwareFailureChannelInterface channelFaultsOfGPS8__Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS5__Failure;
	private SensorChannelInterface channelCommunicationOfGPS20;
	private HardwareFailureChannelInterface channelFaultsOfGPS30_Failure;
	private SensorChannelInterface channelCommunicationOfGPS28;
	private SensorChannelInterface channelCommunicationOfGPS5_;
	private SensorChannelInterface channelCommunicationOfGPS7_;
	private HardwareFailureChannelInterface channelFaultsOfGPS25_Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS1__Failure;
	private HardwareFailureChannelInterface channelFaultsOfGPS20_Failure;
	
	public boolean isEmpty(){
		return  Voter.isEmpty()  &&  GPS1_.isEmpty()  &&  GPS2_.isEmpty()  &&  GPS3_.isEmpty()  &&  GPS4_.isEmpty()  &&  GPS5_.isEmpty()  &&  GPS6_.isEmpty()  &&  GPS7_.isEmpty()  &&  GPS8_.isEmpty()  &&  GPS9_.isEmpty()  &&  GPS10.isEmpty()  &&  GPS11.isEmpty()  &&  GPS12.isEmpty()  &&  GPS13.isEmpty()  &&  GPS14.isEmpty()  &&  GPS15.isEmpty()  &&  GPS16.isEmpty()  &&  GPS17.isEmpty()  &&  GPS18.isEmpty()  &&  GPS19.isEmpty()  &&  GPS20.isEmpty()  &&  GPS21.isEmpty()  &&  GPS22.isEmpty()  &&  GPS23.isEmpty()  &&  GPS24.isEmpty()  &&  GPS25.isEmpty()  &&  GPS26.isEmpty()  &&  GPS27.isEmpty()  &&  GPS28.isEmpty()  &&  GPS29.isEmpty()  &&  GPS30.isEmpty()  &&  GPS31.isEmpty()  &&  GPS32.isEmpty() ;
	}
	
	public void schedule(){
		while(!isEmpty()){
			Voter.schedule();
			GPS1_.schedule();
			GPS2_.schedule();
			GPS3_.schedule();
			GPS4_.schedule();
			GPS5_.schedule();
			GPS6_.schedule();
			GPS7_.schedule();
			GPS8_.schedule();
			GPS9_.schedule();
			GPS10.schedule();
			GPS11.schedule();
			GPS12.schedule();
			GPS13.schedule();
			GPS14.schedule();
			GPS15.schedule();
			GPS16.schedule();
			GPS17.schedule();
			GPS18.schedule();
			GPS19.schedule();
			GPS20.schedule();
			GPS21.schedule();
			GPS22.schedule();
			GPS23.schedule();
			GPS24.schedule();
			GPS25.schedule();
			GPS26.schedule();
			GPS27.schedule();
			GPS28.schedule();
			GPS29.schedule();
			GPS30.schedule();
			GPS31.schedule();
			GPS32.schedule();
		}
	}
	
	public DualGPS() {
		Voter = new Voterasync();
		GPS1_ = new GPSasync();
		GPS2_ = new GPSasync();
		GPS3_ = new GPSasync();
		GPS4_ = new GPSasync();
		GPS5_ = new GPSasync();
		GPS6_ = new GPSasync();
		GPS7_ = new GPSasync();
		GPS8_ = new GPSasync();
		GPS9_ = new GPSasync();
		GPS10 = new GPSasync();
		GPS11 = new GPSasync();
		GPS12 = new GPSasync();
		GPS13 = new GPSasync();
		GPS14 = new GPSasync();
		GPS15 = new GPSasync();
		GPS16 = new GPSasync();
		GPS17 = new GPSasync();
		GPS18 = new GPSasync();
		GPS19 = new GPSasync();
		GPS20 = new GPSasync();
		GPS21 = new GPSasync();
		GPS22 = new GPSasync();
		GPS23 = new GPSasync();
		GPS24 = new GPSasync();
		GPS25 = new GPSasync();
		GPS26 = new GPSasync();
		GPS27 = new GPSasync();
		GPS28 = new GPSasync();
		GPS29 = new GPSasync();
		GPS30 = new GPSasync();
		GPS31 = new GPSasync();
		GPS32 = new GPSasync();
		communication = new Communication();
		// Environmental Component instances
		init();
	}
	
	/** Resets the contained statemachines recursively. Must be called to initialize the component. */
	@Override
	public void reset() {
		Voter.reset();
		GPS1_.reset();
		GPS2_.reset();
		GPS3_.reset();
		GPS4_.reset();
		GPS5_.reset();
		GPS6_.reset();
		GPS7_.reset();
		GPS8_.reset();
		GPS9_.reset();
		GPS10.reset();
		GPS11.reset();
		GPS12.reset();
		GPS13.reset();
		GPS14.reset();
		GPS15.reset();
		GPS16.reset();
		GPS17.reset();
		GPS18.reset();
		GPS19.reset();
		GPS20.reset();
		GPS21.reset();
		GPS22.reset();
		GPS23.reset();
		GPS24.reset();
		GPS25.reset();
		GPS26.reset();
		GPS27.reset();
		GPS28.reset();
		GPS29.reset();
		GPS30.reset();
		GPS31.reset();
		GPS32.reset();
	}
	
	/** Creates the channel mappings and enters the wrapped statemachines. */
	private void init() {				
		// Registration of simple channels
		channelCommunicationOfGPS13 = new SensorChannel(GPS13.getCommunication());
		channelCommunicationOfGPS13.registerPort(Voter.getSensor());
		channelCommunicationOfGPS3_ = new SensorChannel(GPS3_.getCommunication());
		channelCommunicationOfGPS3_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS20 = new SensorChannel(GPS20.getCommunication());
		channelCommunicationOfGPS20.registerPort(Voter.getSensor());
		channelCommunicationOfGPS8_ = new SensorChannel(GPS8_.getCommunication());
		channelCommunicationOfGPS8_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS15 = new SensorChannel(GPS15.getCommunication());
		channelCommunicationOfGPS15.registerPort(Voter.getSensor());
		channelCommunicationOfGPS32 = new SensorChannel(GPS32.getCommunication());
		channelCommunicationOfGPS32.registerPort(Voter.getSensor());
		channelCommunicationOfGPS9_ = new SensorChannel(GPS9_.getCommunication());
		channelCommunicationOfGPS9_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS17 = new SensorChannel(GPS17.getCommunication());
		channelCommunicationOfGPS17.registerPort(Voter.getSensor());
		channelCommunicationOfGPS5_ = new SensorChannel(GPS5_.getCommunication());
		channelCommunicationOfGPS5_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS14 = new SensorChannel(GPS14.getCommunication());
		channelCommunicationOfGPS14.registerPort(Voter.getSensor());
		channelCommunicationOfGPS28 = new SensorChannel(GPS28.getCommunication());
		channelCommunicationOfGPS28.registerPort(Voter.getSensor());
		channelCommunicationOfGPS23 = new SensorChannel(GPS23.getCommunication());
		channelCommunicationOfGPS23.registerPort(Voter.getSensor());
		channelCommunicationOfGPS18 = new SensorChannel(GPS18.getCommunication());
		channelCommunicationOfGPS18.registerPort(Voter.getSensor());
		channelCommunicationOfGPS26 = new SensorChannel(GPS26.getCommunication());
		channelCommunicationOfGPS26.registerPort(Voter.getSensor());
		channelCommunicationOfGPS16 = new SensorChannel(GPS16.getCommunication());
		channelCommunicationOfGPS16.registerPort(Voter.getSensor());
		channelCommunicationOfGPS7_ = new SensorChannel(GPS7_.getCommunication());
		channelCommunicationOfGPS7_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS24 = new SensorChannel(GPS24.getCommunication());
		channelCommunicationOfGPS24.registerPort(Voter.getSensor());
		channelCommunicationOfGPS10 = new SensorChannel(GPS10.getCommunication());
		channelCommunicationOfGPS10.registerPort(Voter.getSensor());
		channelCommunicationOfGPS12 = new SensorChannel(GPS12.getCommunication());
		channelCommunicationOfGPS12.registerPort(Voter.getSensor());
		channelCommunicationOfGPS4_ = new SensorChannel(GPS4_.getCommunication());
		channelCommunicationOfGPS4_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS19 = new SensorChannel(GPS19.getCommunication());
		channelCommunicationOfGPS19.registerPort(Voter.getSensor());
		channelCommunicationOfGPS25 = new SensorChannel(GPS25.getCommunication());
		channelCommunicationOfGPS25.registerPort(Voter.getSensor());
		channelCommunicationOfGPS1_ = new SensorChannel(GPS1_.getCommunication());
		channelCommunicationOfGPS1_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS2_ = new SensorChannel(GPS2_.getCommunication());
		channelCommunicationOfGPS2_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS27 = new SensorChannel(GPS27.getCommunication());
		channelCommunicationOfGPS27.registerPort(Voter.getSensor());
		channelCommunicationOfGPS6_ = new SensorChannel(GPS6_.getCommunication());
		channelCommunicationOfGPS6_.registerPort(Voter.getSensor());
		channelCommunicationOfGPS11 = new SensorChannel(GPS11.getCommunication());
		channelCommunicationOfGPS11.registerPort(Voter.getSensor());
		channelCommunicationOfGPS30 = new SensorChannel(GPS30.getCommunication());
		channelCommunicationOfGPS30.registerPort(Voter.getSensor());
		channelCommunicationOfGPS21 = new SensorChannel(GPS21.getCommunication());
		channelCommunicationOfGPS21.registerPort(Voter.getSensor());
		channelCommunicationOfGPS31 = new SensorChannel(GPS31.getCommunication());
		channelCommunicationOfGPS31.registerPort(Voter.getSensor());
		channelCommunicationOfGPS22 = new SensorChannel(GPS22.getCommunication());
		channelCommunicationOfGPS22.registerPort(Voter.getSensor());
		channelCommunicationOfGPS29 = new SensorChannel(GPS29.getCommunication());
		channelCommunicationOfGPS29.registerPort(Voter.getSensor());
		// Registration of broadcast channels
	}
	
	// Inner classes representing Ports
	public class Communication implements SensorInterface.Provided {
	
		
		@Override
		public boolean isRaisedFailstop() {
			return Voter.getCommunication().isRaisedFailstop();
		}
		
		@Override
		public void registerListener(SensorInterface.Listener.Provided listener) {
			Voter.getCommunication().registerListener(listener);
		}
		
		@Override
		public List<SensorInterface.Listener.Provided> getRegisteredListeners() {
			return Voter.getCommunication().getRegisteredListeners();
		}
		
	}
	
	@Override
	public Communication getCommunication() {
		return communication;
	}
	
	/** Starts the running of the asynchronous component. */
	@Override
	public void start() {
		Voter.start();
		GPS1_.start();
		GPS2_.start();
		GPS3_.start();
		GPS4_.start();
		GPS5_.start();
		GPS6_.start();
		GPS7_.start();
		GPS8_.start();
		GPS9_.start();
		GPS10.start();
		GPS11.start();
		GPS12.start();
		GPS13.start();
		GPS14.start();
		GPS15.start();
		GPS16.start();
		GPS17.start();
		GPS18.start();
		GPS19.start();
		GPS20.start();
		GPS21.start();
		GPS22.start();
		GPS23.start();
		GPS24.start();
		GPS25.start();
		GPS26.start();
		GPS27.start();
		GPS28.start();
		GPS29.start();
		GPS30.start();
		GPS31.start();
		GPS32.start();
	}
	
	public boolean isWaiting() {
		return Voter.isWaiting() && GPS1_.isWaiting() && GPS2_.isWaiting() && GPS3_.isWaiting() && GPS4_.isWaiting() && GPS5_.isWaiting() && GPS6_.isWaiting() && GPS7_.isWaiting() && GPS8_.isWaiting() && GPS9_.isWaiting() && GPS10.isWaiting() && GPS11.isWaiting() && GPS12.isWaiting() && GPS13.isWaiting() && GPS14.isWaiting() && GPS15.isWaiting() && GPS16.isWaiting() && GPS17.isWaiting() && GPS18.isWaiting() && GPS19.isWaiting() && GPS20.isWaiting() && GPS21.isWaiting() && GPS22.isWaiting() && GPS23.isWaiting() && GPS24.isWaiting() && GPS25.isWaiting() && GPS26.isWaiting() && GPS27.isWaiting() && GPS28.isWaiting() && GPS29.isWaiting() && GPS30.isWaiting() && GPS31.isWaiting() && GPS32.isWaiting()
					;
	}
	
	
	/**  Getter for component instances, e.g., enabling to check their states. */
	public Voterasync getVoter() {
		return Voter;
	}
	
	public GPSasync getGPS1_() {
		return GPS1_;
	}
	
	public GPSasync getGPS2_() {
		return GPS2_;
	}
	
	public GPSasync getGPS3_() {
		return GPS3_;
	}
	
	public GPSasync getGPS4_() {
		return GPS4_;
	}
	
	public GPSasync getGPS5_() {
		return GPS5_;
	}
	
	public GPSasync getGPS6_() {
		return GPS6_;
	}
	
	public GPSasync getGPS7_() {
		return GPS7_;
	}
	
	public GPSasync getGPS8_() {
		return GPS8_;
	}
	
	public GPSasync getGPS9_() {
		return GPS9_;
	}
	
	public GPSasync getGPS10() {
		return GPS10;
	}
	
	public GPSasync getGPS11() {
		return GPS11;
	}
	
	public GPSasync getGPS12() {
		return GPS12;
	}
	
	public GPSasync getGPS13() {
		return GPS13;
	}
	
	public GPSasync getGPS14() {
		return GPS14;
	}
	
	public GPSasync getGPS15() {
		return GPS15;
	}
	
	public GPSasync getGPS16() {
		return GPS16;
	}
	
	public GPSasync getGPS17() {
		return GPS17;
	}
	
	public GPSasync getGPS18() {
		return GPS18;
	}
	
	public GPSasync getGPS19() {
		return GPS19;
	}
	
	public GPSasync getGPS20() {
		return GPS20;
	}
	
	public GPSasync getGPS21() {
		return GPS21;
	}
	
	public GPSasync getGPS22() {
		return GPS22;
	}
	
	public GPSasync getGPS23() {
		return GPS23;
	}
	
	public GPSasync getGPS24() {
		return GPS24;
	}
	
	public GPSasync getGPS25() {
		return GPS25;
	}
	
	public GPSasync getGPS26() {
		return GPS26;
	}
	
	public GPSasync getGPS27() {
		return GPS27;
	}
	
	public GPSasync getGPS28() {
		return GPS28;
	}
	
	public GPSasync getGPS29() {
		return GPS29;
	}
	
	public GPSasync getGPS30() {
		return GPS30;
	}
	
	public GPSasync getGPS31() {
		return GPS31;
	}
	
	public GPSasync getGPS32() {
		return GPS32;
	}
	
}
