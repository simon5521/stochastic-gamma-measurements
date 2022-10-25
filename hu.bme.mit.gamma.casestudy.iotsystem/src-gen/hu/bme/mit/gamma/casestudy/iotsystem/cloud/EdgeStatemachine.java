package hu.bme.mit.gamma.casestudy.iotsystem.cloud;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*; 		

public class EdgeStatemachine {
	
	enum Main {__Inactive__, WaitingForCars, NotRecognized, RecognizedByCamera}
	private boolean Camera_newData_In;
	private boolean TrafficStream_carArrives_In;
	private boolean TrafficStream_carLeaves_In;
	private double Camera_newData_In_blurred;
	private boolean Camera_newData_In_car;
	private boolean LostImage_newEvent_Out;
	private boolean CarLeave_newEvent_Out;
	private Main main;
	private double isblurred;
	
	public EdgeStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.isblurred = 0;
		this.main = Main.__Inactive__;
		this.Camera_newData_In = false;
		this.TrafficStream_carArrives_In = false;
		this.TrafficStream_carLeaves_In = false;
		this.LostImage_newEvent_Out = false;
		this.CarLeave_newEvent_Out = false;
		this.main = Main.WaitingForCars;
	}
	
	public void setCamera_newData_In(boolean Camera_newData_In) {
		this.Camera_newData_In = Camera_newData_In;
	}
	
	public boolean getCamera_newData_In() {
		return Camera_newData_In;
	}
	
	public void setTrafficStream_carArrives_In(boolean TrafficStream_carArrives_In) {
		this.TrafficStream_carArrives_In = TrafficStream_carArrives_In;
	}
	
	public boolean getTrafficStream_carArrives_In() {
		return TrafficStream_carArrives_In;
	}
	
	public void setTrafficStream_carLeaves_In(boolean TrafficStream_carLeaves_In) {
		this.TrafficStream_carLeaves_In = TrafficStream_carLeaves_In;
	}
	
	public boolean getTrafficStream_carLeaves_In() {
		return TrafficStream_carLeaves_In;
	}
	
	public void setCamera_newData_In_blurred(double Camera_newData_In_blurred) {
		this.Camera_newData_In_blurred = Camera_newData_In_blurred;
	}
	
	public double getCamera_newData_In_blurred() {
		return Camera_newData_In_blurred;
	}
	
	public void setCamera_newData_In_car(boolean Camera_newData_In_car) {
		this.Camera_newData_In_car = Camera_newData_In_car;
	}
	
	public boolean getCamera_newData_In_car() {
		return Camera_newData_In_car;
	}
	
	public void setLostImage_newEvent_Out(boolean LostImage_newEvent_Out) {
		this.LostImage_newEvent_Out = LostImage_newEvent_Out;
	}
	
	public boolean getLostImage_newEvent_Out() {
		return LostImage_newEvent_Out;
	}
	
	public void setCarLeave_newEvent_Out(boolean CarLeave_newEvent_Out) {
		this.CarLeave_newEvent_Out = CarLeave_newEvent_Out;
	}
	
	public boolean getCarLeave_newEvent_Out() {
		return CarLeave_newEvent_Out;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public Main getMain() {
		return main;
	}
	
	public void setIsblurred(double isblurred) {
		this.isblurred = isblurred;
	}
	
	public double getIsblurred() {
		return isblurred;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _2029825840 = (this.main == Main.NotRecognized);
		boolean _1831294400 = (this.main == Main.RecognizedByCamera);
		boolean _1505109458 = (this.main == Main.WaitingForCars);
		boolean _guard_1443742802 = ((((((_1505109458) && ((this.main == Main.WaitingForCars)))) && (this.TrafficStream_carArrives_In))));
		if (_guard_1443742802) {
			this.main = Main.NotRecognized;
		}
		boolean _guard_754563884 = ((((((_2029825840) && ((this.main == Main.NotRecognized)))) && (this.TrafficStream_carLeaves_In))));
		if (_guard_754563884) {
			this.LostImage_newEvent_Out = true;
			this.CarLeave_newEvent_Out = true;
			this.main = Main.WaitingForCars;
		}
		boolean _guard_496765828 = ((((((_1831294400) && ((this.main == Main.RecognizedByCamera)))) && (this.TrafficStream_carLeaves_In))));
		if (_guard_496765828) {
			this.CarLeave_newEvent_Out = true;
			this.main = Main.WaitingForCars;
		}
		boolean _guard_1363451489 = ((((((_2029825840) && ((this.main == Main.NotRecognized)))) && (this.Camera_newData_In))));
		if (_guard_1363451489) {
			this.main = Main.__Inactive__;
			this.isblurred = this.Camera_newData_In_blurred;
			boolean _148805173 = (this.Camera_newData_In_blurred == 0.0);
			if (((_148805173))) {
				this.main = Main.RecognizedByCamera;
			} else 
			if (!(_148805173)) {
				this.main = Main.NotRecognized;
			}
		}
	}
	
	private void clearOutEvents() {
		LostImage_newEvent_Out = false;
		CarLeave_newEvent_Out = false;
	}
	
	private void clearInEvents() {
		Camera_newData_In = false;
		TrafficStream_carArrives_In = false;
		TrafficStream_carLeaves_In = false;
		Camera_newData_In_blurred = 0;
		Camera_newData_In_car = false;
	}
	
	@Override
	public String toString() {
		return
			"Camera_newData_In = " + Camera_newData_In + System.lineSeparator() +
			"TrafficStream_carArrives_In = " + TrafficStream_carArrives_In + System.lineSeparator() +
			"TrafficStream_carLeaves_In = " + TrafficStream_carLeaves_In + System.lineSeparator() +
			"Camera_newData_In_blurred = " + Camera_newData_In_blurred + System.lineSeparator() +
			"Camera_newData_In_car = " + Camera_newData_In_car + System.lineSeparator() +
			"LostImage_newEvent_Out = " + LostImage_newEvent_Out + System.lineSeparator() +
			"CarLeave_newEvent_Out = " + CarLeave_newEvent_Out + System.lineSeparator() +
			"main = " + main + System.lineSeparator() +
			"isblurred = " + isblurred
		;
	}
	
}
