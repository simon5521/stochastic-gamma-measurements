package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.TimerInterface.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm.Orion_Master_SMStatemachine.*;

public class Orion_Master_SM implements Orion_Master_SMInterface {
	// Port instances
	private Block_Port block_Port = new Block_Port();
	private Connection_Port connection_Port = new Connection_Port();
	private StateMachine_Port stateMachine_Port = new StateMachine_Port();
	private Send_StateMachine_Port send_StateMachine_Port = new Send_StateMachine_Port();
	private Process_StateMachine_Port process_StateMachine_Port = new Process_StateMachine_Port();
	// Wrapped statemachine
	private Orion_Master_SMStatemachine orion_Master_SM;
	// Indicates which queue is active in a cycle
	private boolean insertQueue = true;
	private boolean processQueue = false;
	// Event queues for the synchronization of statecharts
	private Queue<Event> eventQueue1 = new LinkedList<Event>();
	private Queue<Event> eventQueue2 = new LinkedList<Event>();
	// Clocks
	private TimerInterface timer = new OneThreadedTimer();
	
	public Orion_Master_SM() {
		orion_Master_SM = new Orion_Master_SMStatemachine();
	}
	
	public void reset() {
		// Clearing the in events
		insertQueue = true;
		processQueue = false;
		eventQueue1.clear();
		eventQueue2.clear();
		//
		orion_Master_SM.reset();
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
		public void raiseOperation_Call_Connect() {
			getInsertQueue().add(new Event("Connection_Port.Operation_Call_Connect"));
		}
		@Override
		public void raiseOperation_Call_Disconn() {
			getInsertQueue().add(new Event("Connection_Port.Operation_Call_Disconn"));
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
		public void raiseOrionDisconnCause() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionDisconnCause"));
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
		public void raiseOrionAppData() {
			getInsertQueue().add(new Event("StateMachine_Port.OrionAppData"));
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
			return orion_Master_SM.getSend_StateMachine_Port_OrionKeepAlive_Out();
		}
		@Override
		public boolean isRaisedOrionDisconnCause() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionDisconnCause_Out();
		}
		@Override
		public boolean isRaisedOrionConnReq() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionConnReq_Out();
		}
		@Override
		public boolean isRaisedOrionConnConf() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionConnConf_Out();
		}
		@Override
		public boolean isRaisedOrionAppData() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionAppData_Out();
		}
		@Override
		public boolean isRaisedOrionConnResp() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionConnResp_Out();
		}
		@Override
		public boolean isRaisedOrionDisconn() {
			return orion_Master_SM.getSend_StateMachine_Port_OrionDisconn_Out();
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
			return orion_Master_SM.getProcess_StateMachine_Port_OrionKeepAlive_Out();
		}
		@Override
		public boolean isRaisedOrionDisconnCause() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionDisconnCause_Out();
		}
		@Override
		public boolean isRaisedOrionConnReq() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionConnReq_Out();
		}
		@Override
		public boolean isRaisedOrionConnConf() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionConnConf_Out();
		}
		@Override
		public boolean isRaisedOrionAppData() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionAppData_Out();
		}
		@Override
		public boolean isRaisedOrionConnResp() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionConnResp_Out();
		}
		@Override
		public boolean isRaisedOrionDisconn() {
			return orion_Master_SM.getProcess_StateMachine_Port_OrionDisconn_Out();
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
					orion_Master_SM.setBlock_Port_Operation_Call_Invalid_In(true);
				break;
				case "Block_Port.Operation_Call_SendData": 
					orion_Master_SM.setBlock_Port_Operation_Call_SendData_In(true);
				break;
				case "Connection_Port.Operation_Call_Connect": 
					orion_Master_SM.setConnection_Port_Operation_Call_Connect_In(true);
				break;
				case "Connection_Port.Operation_Call_Disconn": 
					orion_Master_SM.setConnection_Port_Operation_Call_Disconn_In(true);
				break;
				case "StateMachine_Port.OrionKeepAlive": 
					orion_Master_SM.setStateMachine_Port_OrionKeepAlive_In(true);
				break;
				case "StateMachine_Port.OrionDisconnCause": 
					orion_Master_SM.setStateMachine_Port_OrionDisconnCause_In(true);
				break;
				case "StateMachine_Port.OrionConnReq": 
					orion_Master_SM.setStateMachine_Port_OrionConnReq_In(true);
				break;
				case "StateMachine_Port.OrionConnConf": 
					orion_Master_SM.setStateMachine_Port_OrionConnConf_In(true);
				break;
				case "StateMachine_Port.OrionAppData": 
					orion_Master_SM.setStateMachine_Port_OrionAppData_In(true);
				break;
				case "StateMachine_Port.OrionConnResp": 
					orion_Master_SM.setStateMachine_Port_OrionConnResp_In(true);
				break;
				case "StateMachine_Port.OrionDisconn": 
					orion_Master_SM.setStateMachine_Port_OrionDisconn_In(true);
				break;
				default:
					throw new IllegalArgumentException("No such event: " + event);
			}
		}
		executeStep();
	}
	
	private void executeStep() {
		int elapsedTime = (int) timer.getElapsedTime(this, TimeUnit.MILLISECOND);
		orion_Master_SM.setTimeoutKapcsolodik_2(orion_Master_SM.getTimeoutKapcsolodik_2() + elapsedTime);
		orion_Master_SM.setTimoeutKeepAliveReceiveTimeout_3(orion_Master_SM.getTimoeutKeepAliveReceiveTimeout_3() + elapsedTime);
		orion_Master_SM.setTimeoutKeepAliveSendTimeout_1(orion_Master_SM.getTimeoutKeepAliveSendTimeout_1() + elapsedTime);
		orion_Master_SM.runCycle();
		timer.saveTime(this);
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
		if (send_StateMachine_Port.isRaisedOrionDisconnCause()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconnCause();
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
		if (send_StateMachine_Port.isRaisedOrionAppData()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : send_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionAppData();
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
		if (process_StateMachine_Port.isRaisedOrionKeepAlive()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionKeepAlive();
			}
		}
		if (process_StateMachine_Port.isRaisedOrionDisconnCause()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionDisconnCause();
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
		if (process_StateMachine_Port.isRaisedOrionAppData()) {
			for (StateMachine_Interface_For_OrionInterface.Listener.Provided listener : process_StateMachine_Port.getRegisteredListeners()) {
				listener.raiseOrionAppData();
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
	}
	
	public void setTimer(TimerInterface timer) {
		this.timer = timer;
	}
	
	public boolean isStateActive(String region, String state) {
		switch (region) {
			case "region_1_in_Kapcsolodva_4":
				return orion_Master_SM.getRegion_1_in_Kapcsolodva_4() == Region_1_in_Kapcsolodva_4.valueOf(state);
			case "region_2_in_Kapcsolodva_4":
				return orion_Master_SM.getRegion_2_in_Kapcsolodva_4() == Region_2_in_Kapcsolodva_4.valueOf(state);
			case "main_region_of_Orion_Master_SM":
				return orion_Master_SM.getMain_region_of_Orion_Master_SM() == Main_region_of_Orion_Master_SM.valueOf(state);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return orion_Master_SM.toString();
	}
}
