package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*; 		

public class VoterStatemachine {
	
	enum Main {__Inactive__, operation, failstop}
	private boolean Sensor_failstop_In;
	private boolean Faults_failure_In;
	private boolean Communication_failstop_Out;
	private Main main;
	private long sensorfailure;
	
	public VoterStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.sensorfailure = 0;
		this.main = Main.__Inactive__;
		this.Sensor_failstop_In = false;
		this.Faults_failure_In = false;
		this.Communication_failstop_Out = false;
		this.main = Main.operation;
	}
	
	public void setSensor_failstop_In(boolean Sensor_failstop_In) {
		this.Sensor_failstop_In = Sensor_failstop_In;
	}
	
	public boolean getSensor_failstop_In() {
		return Sensor_failstop_In;
	}
	
	public void setFaults_failure_In(boolean Faults_failure_In) {
		this.Faults_failure_In = Faults_failure_In;
	}
	
	public boolean getFaults_failure_In() {
		return Faults_failure_In;
	}
	
	public void setCommunication_failstop_Out(boolean Communication_failstop_Out) {
		this.Communication_failstop_Out = Communication_failstop_Out;
	}
	
	public boolean getCommunication_failstop_Out() {
		return Communication_failstop_Out;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public Main getMain() {
		return main;
	}
	
	public void setSensorfailure(long sensorfailure) {
		this.sensorfailure = sensorfailure;
	}
	
	public long getSensorfailure() {
		return sensorfailure;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _1042552949 = (this.main == Main.operation);
		boolean _guard_716929624 = ((((((_1042552949) && ((this.main == Main.operation)))) && (this.Faults_failure_In))));
		if (_guard_716929624) {
			this.Communication_failstop_Out = true;
			this.main = Main.failstop;
		}
		boolean _guard_377973132 = ((((((_1042552949) && ((this.main == Main.operation)))) && (this.Sensor_failstop_In))));
		if (_guard_377973132) {
			this.main = Main.__Inactive__;
			boolean _1813691998 = (this.sensorfailure > 31);
			if (((_1813691998))) {
				this.Communication_failstop_Out = true;
				this.main = Main.failstop;
			} else 
			if (!(_1813691998)) {
				this.main = Main.operation;
			}
		}
	}
	
	private void clearOutEvents() {
		Communication_failstop_Out = false;
	}
	
	private void clearInEvents() {
		Sensor_failstop_In = false;
		Faults_failure_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Sensor_failstop_In = " + Sensor_failstop_In + System.lineSeparator() +
			"Faults_failure_In = " + Faults_failure_In + System.lineSeparator() +
			"Communication_failstop_Out = " + Communication_failstop_Out + System.lineSeparator() +
			"main = " + main + System.lineSeparator() +
			"sensorfailure = " + sensorfailure
		;
	}
	
}
