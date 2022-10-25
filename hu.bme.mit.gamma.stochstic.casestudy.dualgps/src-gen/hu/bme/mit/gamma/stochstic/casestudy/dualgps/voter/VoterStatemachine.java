package hu.bme.mit.gamma.stochstic.casestudy.dualgps.voter;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces.*; 		

public class VoterStatemachine {
	
	enum Main {__Inactive__, operation, onlyGPS1, onlyGPS2, failstop}
	private boolean Sensor1_failstop_In;
	private boolean Faults_failure_In;
	private boolean Sensor2_failstop_In;
	private boolean Communication_failstop_Out;
	private Main main;
	private boolean sensor1Failure;
	private boolean sensor2Failure;
	
	public VoterStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.sensor1Failure = false;
		this.sensor2Failure = false;
		this.main = Main.__Inactive__;
		this.Sensor1_failstop_In = false;
		this.Faults_failure_In = false;
		this.Sensor2_failstop_In = false;
		this.Communication_failstop_Out = false;
		this.main = Main.operation;
	}
	
	public void setSensor1_failstop_In(boolean Sensor1_failstop_In) {
		this.Sensor1_failstop_In = Sensor1_failstop_In;
	}
	
	public boolean getSensor1_failstop_In() {
		return Sensor1_failstop_In;
	}
	
	public void setFaults_failure_In(boolean Faults_failure_In) {
		this.Faults_failure_In = Faults_failure_In;
	}
	
	public boolean getFaults_failure_In() {
		return Faults_failure_In;
	}
	
	public void setSensor2_failstop_In(boolean Sensor2_failstop_In) {
		this.Sensor2_failstop_In = Sensor2_failstop_In;
	}
	
	public boolean getSensor2_failstop_In() {
		return Sensor2_failstop_In;
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
	
	public void setSensor1Failure(boolean sensor1Failure) {
		this.sensor1Failure = sensor1Failure;
	}
	
	public boolean getSensor1Failure() {
		return sensor1Failure;
	}
	
	public void setSensor2Failure(boolean sensor2Failure) {
		this.sensor2Failure = sensor2Failure;
	}
	
	public boolean getSensor2Failure() {
		return sensor2Failure;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _342463380 = (this.main == Main.operation);
		boolean _1628228046 = (this.main == Main.onlyGPS2);
		boolean _2139393668 = (this.main == Main.onlyGPS1);
		boolean _guard_1618505464 = ((((((_342463380) && ((this.main == Main.operation)))) && (this.Sensor1_failstop_In))));
		if (_guard_1618505464) {
			this.main = Main.onlyGPS2;
		}
		boolean _guard_1077219957 = ((((((_342463380) && ((this.main == Main.operation)))) && (this.Sensor2_failstop_In))));
		if (_guard_1077219957) {
			this.main = Main.onlyGPS1;
		}
		boolean _guard_1543087756 = ((((((_342463380) && ((this.main == Main.operation)))) && (this.Faults_failure_In))));
		if (_guard_1543087756) {
			this.Communication_failstop_Out = true;
			this.main = Main.failstop;
		}
		boolean _guard_1701655311 = ((((((_2139393668) && ((this.main == Main.onlyGPS1)))) && ((this.Sensor1_failstop_In || this.Faults_failure_In)))));
		if (_guard_1701655311) {
			this.Communication_failstop_Out = true;
			this.main = Main.failstop;
		}
		boolean _guard_799177143 = ((((((_1628228046) && ((this.main == Main.onlyGPS2)))) && ((this.Sensor2_failstop_In || this.Faults_failure_In)))));
		if (_guard_799177143) {
			this.Communication_failstop_Out = true;
			this.main = Main.failstop;
		}
	}
	
	private void clearOutEvents() {
		Communication_failstop_Out = false;
	}
	
	private void clearInEvents() {
		Sensor1_failstop_In = false;
		Faults_failure_In = false;
		Sensor2_failstop_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Sensor1_failstop_In = " + Sensor1_failstop_In + System.lineSeparator() +
			"Faults_failure_In = " + Faults_failure_In + System.lineSeparator() +
			"Sensor2_failstop_In = " + Sensor2_failstop_In + System.lineSeparator() +
			"Communication_failstop_Out = " + Communication_failstop_Out + System.lineSeparator() +
			"main = " + main + System.lineSeparator() +
			"sensor1Failure = " + sensor1Failure + System.lineSeparator() +
			"sensor2Failure = " + sensor2Failure
		;
	}
	
}
