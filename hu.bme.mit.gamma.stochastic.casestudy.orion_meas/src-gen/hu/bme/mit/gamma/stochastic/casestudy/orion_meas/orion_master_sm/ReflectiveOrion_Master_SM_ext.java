package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.Objects;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public class ReflectiveOrion_Master_SM_ext implements ReflectiveComponentInterface {
	
	private Orion_Master_SM_ext wrappedComponent;
	// Wrapped contained components
	
	
	public ReflectiveOrion_Master_SM_ext() {
		wrappedComponent = new Orion_Master_SM_ext();
	}
	
	public ReflectiveOrion_Master_SM_ext(Orion_Master_SM_ext wrappedComponent) {
		this.wrappedComponent = wrappedComponent;
	}
	
	public void reset() {
		wrappedComponent.reset();
	}
	
	public Orion_Master_SM_ext getWrappedComponent() {
		return wrappedComponent;
	}
	
	public String[] getPorts() {
		return new String[] { "Block_Port", "Connection_Port", "StateMachine_Port", "Send_StateMachine_Port", "Process_StateMachine_Port", "TimoeutKeepAliveReceiveTimeout_3", "TimeoutKapcsolodik_2", "TimeoutZarva_0", "TimeoutKeepAliveSendTimeout_1", "Status" };
	}
	
	public String[] getEvents(String port) {
		switch (port) {
			case "Block_Port":
				return new String[] { "Operation_Call_SendData", "Operation_Call_Invalid" };
			case "Connection_Port":
				return new String[] { "Operation_Call_Connect", "Operation_Call_Disconn" };
			case "StateMachine_Port":
				return new String[] { "OrionDisconn", "OrionDisconnCause", "OrionConnReq", "OrionAppData", "OrionKeepAlive", "OrionConnConf", "OrionConnResp" };
			case "Send_StateMachine_Port":
				return new String[] { "OrionDisconn", "OrionDisconnCause", "OrionConnReq", "OrionAppData", "OrionKeepAlive", "OrionConnConf", "OrionConnResp" };
			case "Process_StateMachine_Port":
				return new String[] { "OrionDisconn", "OrionDisconnCause", "OrionConnReq", "OrionAppData", "OrionKeepAlive", "OrionConnConf", "OrionConnResp" };
			case "TimoeutKeepAliveReceiveTimeout_3":
				return new String[] { "newEvent" };
			case "TimeoutKapcsolodik_2":
				return new String[] { "newEvent" };
			case "TimeoutZarva_0":
				return new String[] { "newEvent" };
			case "TimeoutKeepAliveSendTimeout_1":
				return new String[] { "newEvent" };
			case "Status":
				return new String[] { "conn", "disconn" };
			default:
				throw new IllegalArgumentException("Not known port: " + port);
		}
	}
	
	public void raiseEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Block_Port.Operation_Call_SendData":
				wrappedComponent.getBlock_Port().raiseOperation_Call_SendData();
				break;
			case "Block_Port.Operation_Call_Invalid":
				wrappedComponent.getBlock_Port().raiseOperation_Call_Invalid();
				break;
			case "Connection_Port.Operation_Call_Connect":
				wrappedComponent.getConnection_Port().raiseOperation_Call_Connect();
				break;
			case "Connection_Port.Operation_Call_Disconn":
				wrappedComponent.getConnection_Port().raiseOperation_Call_Disconn();
				break;
			case "StateMachine_Port.OrionDisconn":
				wrappedComponent.getStateMachine_Port().raiseOrionDisconn();
				break;
			case "StateMachine_Port.OrionDisconnCause":
				wrappedComponent.getStateMachine_Port().raiseOrionDisconnCause();
				break;
			case "StateMachine_Port.OrionConnReq":
				wrappedComponent.getStateMachine_Port().raiseOrionConnReq();
				break;
			case "StateMachine_Port.OrionAppData":
				wrappedComponent.getStateMachine_Port().raiseOrionAppData();
				break;
			case "StateMachine_Port.OrionKeepAlive":
				wrappedComponent.getStateMachine_Port().raiseOrionKeepAlive();
				break;
			case "StateMachine_Port.OrionConnConf":
				wrappedComponent.getStateMachine_Port().raiseOrionConnConf();
				break;
			case "StateMachine_Port.OrionConnResp":
				wrappedComponent.getStateMachine_Port().raiseOrionConnResp();
				break;
			case "TimoeutKeepAliveReceiveTimeout_3.newEvent":
				wrappedComponent.getTimoeutKeepAliveReceiveTimeout_3().raiseNewEvent();
				break;
			case "TimeoutKapcsolodik_2.newEvent":
				wrappedComponent.getTimeoutKapcsolodik_2().raiseNewEvent();
				break;
			case "TimeoutZarva_0.newEvent":
				wrappedComponent.getTimeoutZarva_0().raiseNewEvent();
				break;
			case "TimeoutKeepAliveSendTimeout_1.newEvent":
				wrappedComponent.getTimeoutKeepAliveSendTimeout_1().raiseNewEvent();
				break;
			default:
				throw new IllegalArgumentException("Not known port-in event combination: " + portEvent);
		}
	}
	
	public boolean isRaisedEvent(String port, String event, Object[] parameters) {
		String portEvent = port + "." + event;
		switch (portEvent) {
			case "Send_StateMachine_Port.OrionDisconn":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionDisconn()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionDisconnCause":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionDisconnCause()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionConnReq":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionConnReq()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionAppData":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionAppData()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionKeepAlive":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionKeepAlive()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionConnConf":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionConnConf()) {
					return true;
				}
				break;
			case "Send_StateMachine_Port.OrionConnResp":
				if (wrappedComponent.getSend_StateMachine_Port().isRaisedOrionConnResp()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionDisconn":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionDisconn()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionDisconnCause":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionDisconnCause()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionConnReq":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionConnReq()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionAppData":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionAppData()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionKeepAlive":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionKeepAlive()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionConnConf":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionConnConf()) {
					return true;
				}
				break;
			case "Process_StateMachine_Port.OrionConnResp":
				if (wrappedComponent.getProcess_StateMachine_Port().isRaisedOrionConnResp()) {
					return true;
				}
				break;
			case "Status.conn":
				if (wrappedComponent.getStatus().isRaisedConn()) {
					return true;
				}
				break;
			case "Status.disconn":
				if (wrappedComponent.getStatus().isRaisedDisconn()) {
					return true;
				}
				break;
			default:
				throw new IllegalArgumentException("Not known port-out event combination: " + portEvent);
		}
		return false;
	}
	
	public boolean isStateActive(String region, String state) {
		return wrappedComponent.isStateActive(region, state);
	}
	
	public String[] getRegions() {
		return new String[] { "region_1_in_Kapcsolodva_4", "region_2_in_Kapcsolodva_4", "main_region_of_Orion_Master_SM" };
	}
	
	public String[] getStates(String region) {
		switch (region) {
			case "region_1_in_Kapcsolodva_4":
				return new String[] { "KeepAliveSendTimeout_1" };
			case "region_2_in_Kapcsolodva_4":
				return new String[] { "KeepAliveReceiveTimeout_3" };
			case "main_region_of_Orion_Master_SM":
				return new String[] { "Zarva_0", "Kapcsolodik_2", "Kapcsolodva_4" };
		}
		throw new IllegalArgumentException("Not known region: " + region);
	}
	
		public void schedule() {
			schedule(null);
		}
	
		public void schedule(String instance) {
			wrappedComponent.runCycle();
		}
	
	public String[] getVariables() {
		return new String[] {  };
	}
	
	public Object getValue(String variable) {
		switch (variable) {
		}
		throw new IllegalArgumentException("Not known variable: " + variable);
	}
	
	public String[] getComponents() {
		return new String[] { };
	}
	
	public ReflectiveComponentInterface getComponent(String component) {
		switch (component) {
			// If the class name is given, then it will return itself
			case "Orion_Master_SM_ext":
				return this;
		}
		throw new IllegalArgumentException("Not known component: " + component);
	}
	
}