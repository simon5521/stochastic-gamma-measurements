package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_slave_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public interface Orion_Slave_SM_extInterface {

	public Block_Interface_ForOrionInterface.Required getBlock_Port();
	public Connection_Interface_For_OrionInterface.Required getConnection_Port();
	public StateMachine_Interface_For_OrionInterface.Required getStateMachine_Port();
	public StateMachine_Interface_For_OrionInterface.Provided getSend_StateMachine_Port();
	public StateMachine_Interface_For_OrionInterface.Provided getProcess_StateMachine_Port();
	public StateMachine_Interface_For_OrionInterface.Provided getHandle_StateMachine_Port();
	public SoftwareTimerInterface.Required getTimeoutKeepAliveReceiveTimeout_4();
	public SoftwareTimerInterface.Required getTimeoutKapcsolodik_3();
	public SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_0();
	public ConnectionStateInterface.Provided getStatus();
	
	void runCycle();
	void reset();

}
