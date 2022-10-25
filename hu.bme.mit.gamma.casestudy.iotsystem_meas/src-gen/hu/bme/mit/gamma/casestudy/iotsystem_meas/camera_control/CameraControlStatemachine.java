package hu.bme.mit.gamma.casestudy.iotsystem_meas.camera_control;

import hu.bme.mit.gamma.casestudy.iotsystem_meas.interfaces.*; 		

public class CameraControlStatemachine {
	
	enum Main {__Inactive__, MainOperation}
	private boolean DriverImages_newData_In;
	private double DriverImages_newData_In_blurred;
	private boolean DriverImages_newData_In_car;
	private boolean NetworkImages_newData_Out;
	private boolean Requests_newEvent_Out;
	private double NetworkImages_newData_Out_blurred;
	private boolean NetworkImages_newData_Out_car;
	private Main main;
	
	public CameraControlStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main = Main.__Inactive__;
		this.DriverImages_newData_In = false;
		this.NetworkImages_newData_Out = false;
		this.Requests_newEvent_Out = false;
		this.Requests_newEvent_Out = true;
		this.main = Main.MainOperation;
	}
	
	public void setDriverImages_newData_In(boolean DriverImages_newData_In) {
		this.DriverImages_newData_In = DriverImages_newData_In;
	}
	
	public boolean getDriverImages_newData_In() {
		return DriverImages_newData_In;
	}
	
	public void setDriverImages_newData_In_blurred(double DriverImages_newData_In_blurred) {
		this.DriverImages_newData_In_blurred = DriverImages_newData_In_blurred;
	}
	
	public double getDriverImages_newData_In_blurred() {
		return DriverImages_newData_In_blurred;
	}
	
	public void setDriverImages_newData_In_car(boolean DriverImages_newData_In_car) {
		this.DriverImages_newData_In_car = DriverImages_newData_In_car;
	}
	
	public boolean getDriverImages_newData_In_car() {
		return DriverImages_newData_In_car;
	}
	
	public void setNetworkImages_newData_Out(boolean NetworkImages_newData_Out) {
		this.NetworkImages_newData_Out = NetworkImages_newData_Out;
	}
	
	public boolean getNetworkImages_newData_Out() {
		return NetworkImages_newData_Out;
	}
	
	public void setRequests_newEvent_Out(boolean Requests_newEvent_Out) {
		this.Requests_newEvent_Out = Requests_newEvent_Out;
	}
	
	public boolean getRequests_newEvent_Out() {
		return Requests_newEvent_Out;
	}
	
	public void setNetworkImages_newData_Out_blurred(double NetworkImages_newData_Out_blurred) {
		this.NetworkImages_newData_Out_blurred = NetworkImages_newData_Out_blurred;
	}
	
	public double getNetworkImages_newData_Out_blurred() {
		return NetworkImages_newData_Out_blurred;
	}
	
	public void setNetworkImages_newData_Out_car(boolean NetworkImages_newData_Out_car) {
		this.NetworkImages_newData_Out_car = NetworkImages_newData_Out_car;
	}
	
	public boolean getNetworkImages_newData_Out_car() {
		return NetworkImages_newData_Out_car;
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
		boolean _119459046 = (this.main == Main.MainOperation);
		boolean _guard_227710619 = ((((((_119459046) && ((this.main == Main.MainOperation)))))));
		if (_guard_227710619) {
			this.Requests_newEvent_Out = true;
			this.main = Main.MainOperation;
		}
		boolean _guard_303483536 = ((((((_119459046) && ((this.main == Main.MainOperation)))) && (this.DriverImages_newData_In))));
		if (_guard_303483536) {
			this.NetworkImages_newData_Out_blurred = this.DriverImages_newData_In_blurred;
			this.NetworkImages_newData_Out_car = this.DriverImages_newData_In_car;
			this.NetworkImages_newData_Out = true;
			this.main = Main.MainOperation;
		}
	}
	
	private void clearOutEvents() {
		NetworkImages_newData_Out = false;
		Requests_newEvent_Out = false;
		NetworkImages_newData_Out_blurred = 0;
		NetworkImages_newData_Out_car = false;
	}
	
	private void clearInEvents() {
		DriverImages_newData_In = false;
		DriverImages_newData_In_blurred = 0;
		DriverImages_newData_In_car = false;
	}
	
	@Override
	public String toString() {
		return
			"DriverImages_newData_In = " + DriverImages_newData_In + System.lineSeparator() +
			"DriverImages_newData_In_blurred = " + DriverImages_newData_In_blurred + System.lineSeparator() +
			"DriverImages_newData_In_car = " + DriverImages_newData_In_car + System.lineSeparator() +
			"NetworkImages_newData_Out = " + NetworkImages_newData_Out + System.lineSeparator() +
			"Requests_newEvent_Out = " + Requests_newEvent_Out + System.lineSeparator() +
			"NetworkImages_newData_Out_blurred = " + NetworkImages_newData_Out_blurred + System.lineSeparator() +
			"NetworkImages_newData_Out_car = " + NetworkImages_newData_Out_car + System.lineSeparator() +
			"main = " + main
		;
	}
	
}
