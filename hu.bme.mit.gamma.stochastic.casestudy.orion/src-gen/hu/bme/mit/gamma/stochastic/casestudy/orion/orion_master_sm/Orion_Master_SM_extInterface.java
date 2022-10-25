package hu.bme.mit.gamma.stochastic.casestudy.orion.orion_master_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*;

public interface Orion_Master_SM_extInterface {

	public Block_Interface_ForOrionInterface.Required getBlock_Port();
	public Connection_Interface_For_OrionInterface.Required getConnection_Port();
	public StateMachine_Interface_For_OrionInterface.Required getStateMachine_Port();
	public StateMachine_Interface_For_OrionInterface.Provided getSend_StateMachine_Port();
	public StateMachine_Interface_For_OrionInterface.Provided getProcess_StateMachine_Port();
	public SoftwareTimerInterface.Required getTimoeutKeepAliveReceiveTimeout_3();
	public SoftwareTimerInterface.Required getTimeoutKapcsolodik_2();
	public SoftwareTimerInterface.Required getTimeoutZarva_0();
	public SoftwareTimerInterface.Required getTimeoutKeepAliveSendTimeout_1();
	public ConnectionStateInterface.Provided getStatus();
	
	void runCycle();
	void reset();

}
