package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_driver;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*; 		

public class CameraDriverStatemachine {
	
	enum Main {__Inactive__, CarIsVisible, CarIsNotVisible}
	private boolean Traffic_carArrives_In;
	private boolean Requests_newEvent_In;
	private boolean Traffic_carLeaves_In;
	private boolean Images_newData_Out;
	private double Images_newData_Out_blurred;
	private boolean Images_newData_Out_car;
	private Main main;
	
	public CameraDriverStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main = Main.__Inactive__;
		this.Traffic_carArrives_In = false;
		this.Requests_newEvent_In = false;
		this.Traffic_carLeaves_In = false;
		this.Images_newData_Out = false;
		this.main = Main.CarIsNotVisible;
	}
	
	public void setTraffic_carArrives_In(boolean Traffic_carArrives_In) {
		this.Traffic_carArrives_In = Traffic_carArrives_In;
	}
	
	public boolean getTraffic_carArrives_In() {
		return Traffic_carArrives_In;
	}
	
	public void setRequests_newEvent_In(boolean Requests_newEvent_In) {
		this.Requests_newEvent_In = Requests_newEvent_In;
	}
	
	public boolean getRequests_newEvent_In() {
		return Requests_newEvent_In;
	}
	
	public void setTraffic_carLeaves_In(boolean Traffic_carLeaves_In) {
		this.Traffic_carLeaves_In = Traffic_carLeaves_In;
	}
	
	public boolean getTraffic_carLeaves_In() {
		return Traffic_carLeaves_In;
	}
	
	public void setImages_newData_Out(boolean Images_newData_Out) {
		this.Images_newData_Out = Images_newData_Out;
	}
	
	public boolean getImages_newData_Out() {
		return Images_newData_Out;
	}
	
	public void setImages_newData_Out_blurred(double Images_newData_Out_blurred) {
		this.Images_newData_Out_blurred = Images_newData_Out_blurred;
	}
	
	public double getImages_newData_Out_blurred() {
		return Images_newData_Out_blurred;
	}
	
	public void setImages_newData_Out_car(boolean Images_newData_Out_car) {
		this.Images_newData_Out_car = Images_newData_Out_car;
	}
	
	public boolean getImages_newData_Out_car() {
		return Images_newData_Out_car;
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
		boolean _153933108 = (this.main == Main.CarIsVisible);
		boolean _1983042773 = (this.main == Main.CarIsNotVisible);
		boolean _guard_1643550034 = ((((((_153933108) && ((this.main == Main.CarIsVisible)))) && (this.Traffic_carLeaves_In))));
		if (_guard_1643550034) {
			this.main = Main.CarIsNotVisible;
		}
		boolean _guard_1728120957 = ((((((_153933108) && ((this.main == Main.CarIsVisible)))) && (this.Requests_newEvent_In))));
		if (_guard_1728120957) {
			this.Images_newData_Out_blurred = 0.0;
			this.Images_newData_Out_car = true;
			this.Images_newData_Out = true;
			this.main = Main.CarIsVisible;
		}
		boolean _guard_1487778835 = ((((((_1983042773) && ((this.main == Main.CarIsNotVisible)))) && (this.Traffic_carArrives_In))));
		if (_guard_1487778835) {
			this.main = Main.CarIsVisible;
		}
		boolean _guard_530588163 = ((((((_1983042773) && ((this.main == Main.CarIsNotVisible)))) && (this.Requests_newEvent_In))));
		if (_guard_530588163) {
			this.Images_newData_Out_blurred = 0.0;
			this.Images_newData_Out_car = false;
			this.Images_newData_Out = true;
			this.main = Main.CarIsNotVisible;
		}
	}
	
	private void clearOutEvents() {
		Images_newData_Out = false;
		Images_newData_Out_blurred = 0;
		Images_newData_Out_car = false;
	}
	
	private void clearInEvents() {
		Traffic_carArrives_In = false;
		Requests_newEvent_In = false;
		Traffic_carLeaves_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Traffic_carArrives_In = " + Traffic_carArrives_In + System.lineSeparator() +
			"Requests_newEvent_In = " + Requests_newEvent_In + System.lineSeparator() +
			"Traffic_carLeaves_In = " + Traffic_carLeaves_In + System.lineSeparator() +
			"Images_newData_Out = " + Images_newData_Out + System.lineSeparator() +
			"Images_newData_Out_blurred = " + Images_newData_Out_blurred + System.lineSeparator() +
			"Images_newData_Out_car = " + Images_newData_Out_car + System.lineSeparator() +
			"main = " + main
		;
	}
	
}
