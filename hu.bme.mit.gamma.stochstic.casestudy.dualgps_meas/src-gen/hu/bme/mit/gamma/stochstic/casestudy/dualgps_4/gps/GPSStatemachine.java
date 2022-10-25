package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.gps;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces.*; 		

public class GPSStatemachine {
	
	enum Main {__Inactive__, operation, failstop}
	private boolean Faults_failure_In;
	private boolean Communication_failstop_Out;
	private Main main;
	
	public GPSStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main = Main.__Inactive__;
		this.Faults_failure_In = false;
		this.Communication_failstop_Out = false;
		this.main = Main.operation;
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
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _570881130 = (this.main == Main.operation);
		boolean _guard_1188098020 = ((((((_570881130) && ((this.main == Main.operation)))) && (this.Faults_failure_In))));
		if (_guard_1188098020) {
			this.Communication_failstop_Out = true;
			this.main = Main.failstop;
		}
	}
	
	private void clearOutEvents() {
		Communication_failstop_Out = false;
	}
	
	private void clearInEvents() {
		Faults_failure_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Faults_failure_In = " + Faults_failure_In + System.lineSeparator() +
			"Communication_failstop_Out = " + Communication_failstop_Out + System.lineSeparator() +
			"main = " + main
		;
	}
	
}
