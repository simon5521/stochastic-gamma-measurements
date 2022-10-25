package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.status_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*; 		

public class Status_SMStatemachine {
	
	enum Main {__Inactive__, no_conn, master_conn, slave_conn, system_conn}
	private boolean slaveStatus_conn_In;
	private boolean masterStatus_disconn_In;
	private boolean masterStatus_conn_In;
	private boolean slaveStatus_disconn_In;
	private boolean systemStatus_conn_Out;
	private boolean systemStatus_disconn_Out;
	private Main main;
	
	public Status_SMStatemachine() {
	}
	
	public void reset() {
		this.main = Main.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main = Main.__Inactive__;
		this.slaveStatus_conn_In = false;
		this.masterStatus_disconn_In = false;
		this.masterStatus_conn_In = false;
		this.slaveStatus_disconn_In = false;
		this.systemStatus_conn_Out = false;
		this.systemStatus_disconn_Out = false;
		this.main = Main.no_conn;
	}
	
	public void setSlaveStatus_conn_In(boolean slaveStatus_conn_In) {
		this.slaveStatus_conn_In = slaveStatus_conn_In;
	}
	
	public boolean getSlaveStatus_conn_In() {
		return slaveStatus_conn_In;
	}
	
	public void setMasterStatus_disconn_In(boolean masterStatus_disconn_In) {
		this.masterStatus_disconn_In = masterStatus_disconn_In;
	}
	
	public boolean getMasterStatus_disconn_In() {
		return masterStatus_disconn_In;
	}
	
	public void setMasterStatus_conn_In(boolean masterStatus_conn_In) {
		this.masterStatus_conn_In = masterStatus_conn_In;
	}
	
	public boolean getMasterStatus_conn_In() {
		return masterStatus_conn_In;
	}
	
	public void setSlaveStatus_disconn_In(boolean slaveStatus_disconn_In) {
		this.slaveStatus_disconn_In = slaveStatus_disconn_In;
	}
	
	public boolean getSlaveStatus_disconn_In() {
		return slaveStatus_disconn_In;
	}
	
	public void setSystemStatus_conn_Out(boolean systemStatus_conn_Out) {
		this.systemStatus_conn_Out = systemStatus_conn_Out;
	}
	
	public boolean getSystemStatus_conn_Out() {
		return systemStatus_conn_Out;
	}
	
	public void setSystemStatus_disconn_Out(boolean systemStatus_disconn_Out) {
		this.systemStatus_disconn_Out = systemStatus_disconn_Out;
	}
	
	public boolean getSystemStatus_disconn_Out() {
		return systemStatus_disconn_Out;
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
		boolean _845825971 = (this.main == Main.no_conn);
		boolean _95662641 = (this.main == Main.slave_conn);
		boolean _130151816 = (this.main == Main.master_conn);
		boolean _guard_699849759 = ((((((_845825971) && ((this.main == Main.no_conn)))) && (this.masterStatus_conn_In))));
		if (_guard_699849759) {
			this.main = Main.master_conn;
		}
		boolean _guard_1528918043 = ((((((_845825971) && ((this.main == Main.no_conn)))) && (this.slaveStatus_conn_In))));
		if (_guard_1528918043) {
			this.main = Main.slave_conn;
		}
		boolean _guard_53647369 = ((((((_130151816) && ((this.main == Main.master_conn)))) && (this.masterStatus_disconn_In))));
		if (_guard_53647369) {
			this.main = Main.no_conn;
		}
		boolean _guard_1092317060 = ((((((_130151816) && ((this.main == Main.master_conn)))) && (this.slaveStatus_conn_In))));
		if (_guard_1092317060) {
			this.systemStatus_conn_Out = true;
			this.main = Main.system_conn;
		}
		boolean _guard_214077935 = ((((((_95662641) && ((this.main == Main.slave_conn)))) && (this.slaveStatus_disconn_In))));
		if (_guard_214077935) {
			this.main = Main.no_conn;
		}
		boolean _guard_1262579498 = ((((((_95662641) && ((this.main == Main.slave_conn)))) && (this.masterStatus_conn_In))));
		if (_guard_1262579498) {
			this.systemStatus_conn_Out = true;
			this.main = Main.system_conn;
		}
	}
	
	private void clearOutEvents() {
		systemStatus_conn_Out = false;
		systemStatus_disconn_Out = false;
	}
	
	private void clearInEvents() {
		slaveStatus_conn_In = false;
		masterStatus_disconn_In = false;
		masterStatus_conn_In = false;
		slaveStatus_disconn_In = false;
	}
	
	@Override
	public String toString() {
		return
			"slaveStatus_conn_In = " + slaveStatus_conn_In + System.lineSeparator() +
			"masterStatus_disconn_In = " + masterStatus_disconn_In + System.lineSeparator() +
			"masterStatus_conn_In = " + masterStatus_conn_In + System.lineSeparator() +
			"slaveStatus_disconn_In = " + slaveStatus_disconn_In + System.lineSeparator() +
			"systemStatus_conn_Out = " + systemStatus_conn_Out + System.lineSeparator() +
			"systemStatus_disconn_Out = " + systemStatus_disconn_Out + System.lineSeparator() +
			"main = " + main
		;
	}
	
}
