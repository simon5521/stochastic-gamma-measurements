package hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*; 		

public class TrafficStatechartStatemachine {
	
	enum Main {__Inactive__, car, nocar}
	private boolean TrafficStream_carArrives_Out;
	private boolean CarArrivesOut_newEvent_Out;
	private boolean TrafficStream_carLeaves_Out;
	private boolean CarLeaves_newEvent_In;
	private boolean CarArrives_newEvent_In;
	private Main main;
	
	public TrafficStatechartStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main = Main.__Inactive__;
		this.CarLeaves_newEvent_In = false;
		this.CarArrives_newEvent_In = false;
		this.TrafficStream_carArrives_Out = false;
		this.CarArrivesOut_newEvent_Out = false;
		this.TrafficStream_carLeaves_Out = false;
		this.main = Main.nocar;
	}
	
	public void setTrafficStream_carArrives_Out(boolean TrafficStream_carArrives_Out) {
		this.TrafficStream_carArrives_Out = TrafficStream_carArrives_Out;
	}
	
	public boolean getTrafficStream_carArrives_Out() {
		return TrafficStream_carArrives_Out;
	}
	
	public void setCarArrivesOut_newEvent_Out(boolean CarArrivesOut_newEvent_Out) {
		this.CarArrivesOut_newEvent_Out = CarArrivesOut_newEvent_Out;
	}
	
	public boolean getCarArrivesOut_newEvent_Out() {
		return CarArrivesOut_newEvent_Out;
	}
	
	public void setTrafficStream_carLeaves_Out(boolean TrafficStream_carLeaves_Out) {
		this.TrafficStream_carLeaves_Out = TrafficStream_carLeaves_Out;
	}
	
	public boolean getTrafficStream_carLeaves_Out() {
		return TrafficStream_carLeaves_Out;
	}
	
	public void setCarLeaves_newEvent_In(boolean CarLeaves_newEvent_In) {
		this.CarLeaves_newEvent_In = CarLeaves_newEvent_In;
	}
	
	public boolean getCarLeaves_newEvent_In() {
		return CarLeaves_newEvent_In;
	}
	
	public void setCarArrives_newEvent_In(boolean CarArrives_newEvent_In) {
		this.CarArrives_newEvent_In = CarArrives_newEvent_In;
	}
	
	public boolean getCarArrives_newEvent_In() {
		return CarArrives_newEvent_In;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public Main getMain() {
		return main;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _1854946081 = (this.main == Main.car);
		boolean _210819997 = (this.main == Main.nocar);
		boolean _guard_537010105 = ((((((_1854946081) && ((this.main == Main.car)))) && (this.CarLeaves_newEvent_In))));
		if (_guard_537010105) {
			this.TrafficStream_carLeaves_Out = true;
			this.main = Main.nocar;
		}
		boolean _guard_1256328073 = ((((((_210819997) && ((this.main == Main.nocar)))) && (this.CarArrives_newEvent_In))));
		if (_guard_1256328073) {
			this.TrafficStream_carArrives_Out = true;
			this.CarArrivesOut_newEvent_Out = true;
			this.main = Main.car;
		}
	}
	
	private void clearOutEvents() {
		TrafficStream_carArrives_Out = false;
		CarArrivesOut_newEvent_Out = false;
		TrafficStream_carLeaves_Out = false;
	}
	
	private void clearInEvents() {
		CarLeaves_newEvent_In = false;
		CarArrives_newEvent_In = false;
	}
	
	@Override
	public String toString() {
		return
			"TrafficStream_carArrives_Out = " + TrafficStream_carArrives_Out + System.lineSeparator() +
			"CarArrivesOut_newEvent_Out = " + CarArrivesOut_newEvent_Out + System.lineSeparator() +
			"TrafficStream_carLeaves_Out = " + TrafficStream_carLeaves_Out + System.lineSeparator() +
			"CarLeaves_newEvent_In = " + CarLeaves_newEvent_In + System.lineSeparator() +
			"CarArrives_newEvent_In = " + CarArrives_newEvent_In + System.lineSeparator() +
			"main = " + main
		;
	}
	
}
