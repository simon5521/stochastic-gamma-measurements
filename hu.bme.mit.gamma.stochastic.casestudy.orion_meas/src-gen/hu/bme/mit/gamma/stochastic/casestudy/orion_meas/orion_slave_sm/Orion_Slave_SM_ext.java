package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_slave_sm;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.TimerInterface.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_slave_sm.Orion_Slave_SM_extStatemachine.*;

public class Orion_Slave_SM_ext implements Orion_Slave_SM_extInterface {
	// Port instances
	private Block_Port block_Port = new Block_Port();
	private Connection_Port connection_Port = new Connection_Port();
	private StateMachine_Port stateMachine_Port = new StateMachine_Port();
	private Send_StateMachine_Port send_StateMachine_Port = new Send_StateMachine_Port();
	private Process_StateMachine_Port process_StateMachine_Port = new Process_StateMachine_Port();
	private Handle_StateMachine_Port handle_StateMachine_Port = new Handle_StateMachine_Port();
	private TimeoutKeepAliveReceiveTimeout_4 timeoutKeepAliveReceiveTimeout_4 = new TimeoutKeepAliveReceiveTimeout_4();
	private TimeoutKapcsolodik_3 timeoutKapcsolodik_3 = new TimeoutKapcsolodik_3();
	private TimeoutKeepAliveSendTimeout_0 timeoutKeepAliveSendTimeout_0 = new TimeoutKeepAliveSendTimeout_0();
	private Status status = new Status();
	// Wrapped statemachine
	private Orion_Slave_SM_extStatemachine orion_Slave_SM_ext;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Orion_Slave_SM_ext() {
		orion_Slave_SM_ext = new Orion_Slave_SM_extStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		orion_Slave_SM_ext.reset();
		timer.saveTime(this);
		notifyListeners();
	}

	/** Changes the event queues of the component instance. Should be used only be the container (composite system) class. */
	public void changeEventQueues() {
		insertQueue = !insertQueue;
		processQueue = !processQueue;
	}
	
	/** Changes the event queues to which the events are put. Should be used only be a cascade container (composite system) class. */
	public void changeInsertQueue() {
		insertQueue = !insertQueue;
	}
	
	/** Returns whether the eventQueue containing incoming messages is empty. Should be used only be the container (composite system) class. */
	public boolean isEventQueueEmpty() {
		return getInsertQueue().isEmpty();
	}
	
	/** Returns the event queue into which events should be put in the particular cycle. */
	private Queue<Event> getInsertQueue() {
		if (insertQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	/** Returns the event queue from which events should be inspected in the particular cycle. */
	private Queue<Event> getProcessQueue() {
		if (processQueue) {
			return eventQueue1;
		}
		return eventQueue2;
	}
	
	public class Block_Port implements Block_Interface_ForOrionInterface.Required {
		private List<Block_Interface_ForOrionInterface.Listener.Required> listeners = new LinkedList<Block_Interface_ForOrionInterface.Listener.Required>();
		@Override
		public void raiseOperation_Call_Invalid() {
			getInsertQueue().add(new Event("Block_Port.Operation_Call_Invalid"));
		}
		@Override
		public void raiseOperation_Call_SendData() {
			getInsertQueue().add(new Event("Block_Port.Operation_Call_SendData"));
		}
		@Override
		public void registerListener(Block_Interface_ForOrionInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<Block_Interface_ForOrionInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Block_Port getBlock_Port() {
		return block_Port;
	}
	
	public class Connection_Port implements Connection_Interface_For_OrionInterface.Required {
		private List<Connection_Interface_For_OrionInterface.Listener.Required> listeners = new LinkedList<Connection_Interface_For_OrionInterface.Listener.Required>();
		@Override
		public void raiseOperation_Call_Disconn() {
			getInsertQueue().add(new Event("Connection_Port.Operation_Call_Disconn"));
		}
		@Override
		public void raiseOperation_Call_Connect() {
			getInsertQueue().add(new Event("Connection_Port.Operation_Call_Connect"));
		}
		@Override
		public void registerListener(Connection_Interface_For_OrionInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<Connection_Interface_For_OrionInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Connection_Port getConnection_Port() {
		return connection_Port;
	}
	
	public class StateMachine_Port implements StateMachine_Interface_For_OrionInterface.Required {
		private List<StateMachine_Interface_For_OrionInterface.Listener.Required> listeners = new LinkedList<StateMachine_Interface_For_OrionInterface.Listener.Required>();
		@Override
		public void raiseOrionKeepAlive() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionKeepAlive"));
		}
		@Override
		public void raiseOrionConnReq() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionConnReq"));
		}
		@Override
		public void raiseOrionConnConf() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionConnConf"));
		}
		@Override
		public void raiseOrionConnResp() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionConnResp"));
		}
		@Override
		public void raiseOrionDisconn() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionDisconn"));
		}
		@Override
		public void raiseOrionAppData() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionAppData"));
		}
		@Override
		public void raiseOrionDisconnCause() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionDisconnCause"));
		}
		@Override
		public void registerListener(StateMachine_Interface_For_OrionInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<StateMachine_Interface_For_OrionInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public StateMachine_Port getStateMachine_Port() {
		return stateMachine_Port;
	}
	
	public class Send_StateMachine_Port implements StateMachine_Interface_For_OrionInterface.Provided {
		private List<StateMachine_Interface_For_OrionInterface.Listener.Provided> listeners = new LinkedList<StateMachine_Interface_For_OrionInterface.Listener.Provided>();
		@Override
		public boolean isRaisedOrionKeepAlive() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionKeepAlive_Out();
		}
		@Override
		public boolean isRaisedOrionConnReq() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionConnReq_Out();
		}
		@Override
		public boolean isRaisedOrionConnConf() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionConnConf_Out();
		}
		@Override
		public boolean isRaisedOrionConnResp() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionConnResp_Out();
		}
		@Override
		public boolean isRaisedOrionDisconn() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionDisconn_Out();
		}
		@Override
		public boolean isRaisedOrionAppData() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionAppData_Out();
		}
		@Override
		public boolean isRaisedOrionDisconnCause() {
			return orion_Slave_SM_ext.getSend_StateMachine_Port_OrionDisconnCause_Out();
		}
		@Override
		public void registerListener(StateMachine_Interface_For_OrionInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<StateMachine_Interface_For_OrionInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Send_StateMachine_Port getSend_StateMachine_Port() {
		return send_StateMachine_Port;
	}
	
	public class Process_StateMachine_Port implements StateMachine_Interface_For_OrionInterface.Provided {
		private List<StateMachine_Interface_For_OrionInterface.Listener.Provided> listeners = new LinkedList<StateMachine_Interface_For_OrionInterface.Listener.Provided>();
		@Override
		public boolean isRaisedOrionKeepAlive() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionKeepAlive_Out();
		}
		@Override
		public boolean isRaisedOrionConnReq() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionConnReq_Out();
		}
		@Override
		public boolean isRaisedOrionConnConf() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionConnConf_Out();
		}
		@Override
		public boolean isRaisedOrionConnResp() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionConnResp_Out();
		}
		@Override
		public boolean isRaisedOrionDisconn() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionDisconn_Out();
		}
		@Override
		public boolean isRaisedOrionAppData() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionAppData_Out();
		}
		@Override
		public boolean isRaisedOrionDisconnCause() {
			return orion_Slave_SM_ext.getProcess_StateMachine_Port_OrionDisconnCause_Out();
		}
		@Override
		public void registerListener(StateMachine_Interface_For_OrionInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<StateMachine_Interface_For_OrionInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Process_StateMachine_Port getProcess_StateMachine_Port() {
		return process_StateMachine_Port;
	}
	
	public class Handle_StateMachine_Port implements StateMachine_Interface_For_OrionInterface.Provided {
		private List<StateMachine_Interface_For_OrionInterface.Listener.Provided> listeners = new LinkedList<StateMachine_Interface_For_OrionInterface.Listener.Provided>();
		@Override
		public boolean isRaisedOrionKeepAlive() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionKeepAlive_Out();
		}
		@Override
		public boolean isRaisedOrionConnReq() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionConnReq_Out();
		}
		@Override
		public boolean isRaisedOrionConnConf() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionConnConf_Out();
		}
		@Override
		public boolean isRaisedOrionConnResp() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionConnResp_Out();
		}
		@Override
		public boolean isRaisedOrionDisconn() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionDisconn_Out();
		}
		@Override
		public boolean isRaisedOrionAppData() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionAppData_Out();
		}
		@Override
		public boolean isRaisedOrionDisconnCause() {
			return orion_Slave_SM_ext.getHandle_StateMachine_Port_OrionDisconnCause_Out();
		}
		@Override
		public void registerListener(StateMachine_Interface_For_OrionInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<StateMachine_Interface_For_OrionInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Handle_StateMachine_Port getHandle_StateMachine_Port() {
		return handle_StateMachine_Port;
	}
	
	public class TimeoutKeepAliveReceiveTimeout_4 implements SoftwareTimerInterface.Required {
		private List<SoftwareTimerInterface.Listener.Required> listeners = new LinkedList<SoftwareTimerInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("TimeoutKeepAliveReceiveTimeout_4.newEvent"));
		}
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public TimeoutKeepAliveReceiveTimeout_4 getTimeoutKeepAliveReceiveTimeout_4() {
		return timeoutKeepAliveReceiveTimeout_4;
	}
	
	public class TimeoutKapcsolodik_3 implements SoftwareTimerInterface.Required {
		private List<SoftwareTimerInterface.Listener.Required> listeners = new LinkedList<SoftwareTimerInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("TimeoutKapcsolodik_3.newEvent"));
		}
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public TimeoutKapcsolodik_3 getTimeoutKapcsolodik_3() {
		return timeoutKapcsolodik_3;
	}
	
	public class TimeoutKeepAliveSendTimeout_0 implements SoftwareTimerInterface.Required {
		private List<SoftwareTimerInterface.Listener.Required> listeners = new LinkedList<SoftwareTimerInterface.Listener.Required>();
		@Override
		public void raiseNewEvent() {
			getInsertQueue().add(new Event("TimeoutKeepAliveSendTimeout_0.newEvent"));
		}
		@Override
		public void registerListener(SoftwareTimerInterface.Listener.Required listener) {
			listeners.add(listener);
		}
		@Override
		public List<SoftwareTimerInterface.Listener.Required> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public TimeoutKeepAliveSendTimeout_0 getTimeoutKeepAliveSendTimeout_0() {
		return timeoutKeepAliveSendTimeout_0;
	}
	
	public class Status implements ConnectionStateInterface.Provided {
		private List<ConnectionStateInterface.Listener.Provided> listeners = new LinkedList<ConnectionStateInterface.Listener.Provided>();
		@Override
		public boolean isRaisedConn() {
			return orion_Slave_SM_ext.getStatus_conn_Out();
		}
		@Override
		public boolean isRaisedDisconn() {
			return orion_Slave_SM_ext.getStatus_disconn_Out();
		}
		@Override
		public void registerListener(ConnectionStateInterface.Listener.Provided listener) {
			listeners.add(listener);
		}
		@Override
		public List<ConnectionStateInterface.Listener.Provided> getRegisteredListeners() {
			return listeners;
		}
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void runCycle() {
		changeEventQueues();
		runComponent();
	}
	
	public void runComponent() {
		Queue<Event> eventQueue = getProcessQueue();
		while (!eventQueue.isEmpty()) {
			Event event = eventQueue.remove();
			switch (event.getEvent()) {
				case "Block_Port.Operation_Call_Invalid": 
					orion_Slave_SM_ext.setBlock_Port_Operation_Call_Invalid_In(true);
				break;
				case "Block_Port.Operation_Call_SendData": 
					orion_Slave_SM_ext.setBlock_Port_Operation_Call_SendData_In(true);
				break;
				case "Connection_Port.Operation_Call_Disconn": 
					orion_Slave_SM_ext.setConnection_Port_Operation_Call_Disconn_In(true);
				break;
				case "Connection_Port.Operation_Call_Connect": 
					orion_Slave_SM_ext.setConnection_Port_Operation_Call_Connect_In(true);
				break;
				case "StateMachine_Port.OrionKeepAlive": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionKeepAlive_In(true);
				break;
				case "StateMachine_Port.OrionConnReq": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionConnReq_In(true);
				break;
				case "StateMachine_Port.OrionConnConf": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionConnConf_In(true);
				break;
				case "StateMachine_Port.OrionConnResp": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionConnResp_In(true);
				break;
				case "StateMachine_Port.OrionDisconn": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionDisconn_In(true);
				break;
				case "StateMachine_Port.OrionAppData": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionAppData_In(true);
				break;
				case "StateMachine_Port.OrionDisconnCause": 
					orion_Slave_SM_ext.setStateMachine_Port_OrionDisconnCause_In(true);
				break;
				case "TimeoutKeepAliveReceiveTimeout_4.newEvent": 
					orion_Slave_SM_ext.setTimeoutKeepAliveReceiveTimeout_4_newEvent_In(true);
				break;
				case "TimeoutKapcsolodik_3.newEvent": 
					orion_Slave_SM_ext.setTimeoutKapcsolodik_3_newEvent_In(true);
				break;
				case "TimeoutKeepAliveSendTimeout_0.newEvent": 
					orion_Slave_SM_ext.setTimeoutKeepAliveSendTimeout_0_newEvent_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		orion_Slave_SM_ext.runCycle();
		notifyListeners();
	}
	
	/** Interface method, needed for composite component initialization chain. */
	public void notifyAllListeners() {
		notifyListeners();
	}
	
	public void notifyListeners() {
		if (send_StateMachine_Port.isRaisedOrionKeepAlive()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionKeepAlive();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionConnReq()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnReq();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionConnConf()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnConf();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionConnResp()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnResp();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionDisconn()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconn();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionAppData()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionAppData();
			}
		}
		if (send_StateMachine_Port.isRaisedOrionDisconnCause()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconnCause();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionKeepAlive()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionKeepAlive();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionConnReq()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnReq();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionConnConf()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnConf();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionConnResp()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnResp();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionDisconn()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconn();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionAppData()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionAppData();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionDisconnCause()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconnCause();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionKeepAlive()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionKeepAlive();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionConnReq()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnReq();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionConnConf()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnConf();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionConnResp()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionConnResp();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionDisconn()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconn();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionAppData()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionAppData();
			}
		}
		if (handle_StateMachine_Port.isRaisedOrionDisconnCause()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : handle_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconnCause();
			}
		}
		if (status.isRaisedConn()) {
			for (ConnectionStateInterface.Listener.Provided listener : status.getRegisteredListeners()) {
				listener.raiseConn();
			}
		}
		if (status.isRaisedDisconn()) {
			for (ConnectionStateInterface.Listener.Provided listener : status.getRegisteredListeners()) {
				listener.raiseDisconn();
			}
		}
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "region_1_in_Kapcsolodva_1":
				return orion_Slave_SM_ext.getRegion_1_in_Kapcsolodva_1() == Region_1_in_Kapcsolodva_1.valueOf(state);
			case "region_2_in_Kapcsolodva_1":
				return orion_Slave_SM_ext.getRegion_2_in_Kapcsolodva_1() == Region_2_in_Kapcsolodva_1.valueOf(state);
			case "main_region_of_Orion_Slave_SM":
				return orion_Slave_SM_ext.getMain_region_of_Orion_Slave_SM() == Main_region_of_Orion_Slave_SM.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return orion_Slave_SM_ext.toString();
	}
}
