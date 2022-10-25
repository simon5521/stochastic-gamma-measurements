package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*; 		

public class Orion_Master_SMStatemachine {
	
	enum Main_region_of_Orion_Master_SM {__Inactive__, Zarva_0, Kapcsolodik_2, Kapcsolodva_4}
	enum Region_2_in_Kapcsolodva_4 {__Inactive__, KeepAliveReceiveTimeout_3}
	enum Region_1_in_Kapcsolodva_4 {__Inactive__, KeepAliveSendTimeout_1}
	private boolean Process_StateMachine_Port_OrionAppData_Out;
	private boolean Send_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Send_StateMachine_Port_OrionConnConf_Out;
	private boolean Send_StateMachine_Port_OrionDisconn_Out;
	private boolean Process_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Send_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Process_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Send_StateMachine_Port_OrionAppData_Out;
	private boolean Send_StateMachine_Port_OrionConnReq_Out;
	private boolean Process_StateMachine_Port_OrionDisconn_Out;
	private boolean Process_StateMachine_Port_OrionConnConf_Out;
	private boolean Process_StateMachine_Port_OrionConnReq_Out;
	private boolean Process_StateMachine_Port_OrionConnResp_Out;
	private boolean Send_StateMachine_Port_OrionConnResp_Out;
	private boolean StateMachine_Port_OrionDisconn_In;
	private boolean StateMachine_Port_OrionConnConf_In;
	private boolean StateMachine_Port_OrionDisconnCause_In;
	private boolean StateMachine_Port_OrionConnResp_In;
	private boolean StateMachine_Port_OrionKeepAlive_In;
	private boolean Connection_Port_Operation_Call_Disconn_In;
	private boolean StateMachine_Port_OrionAppData_In;
	private boolean Block_Port_Operation_Call_SendData_In;
	private boolean StateMachine_Port_OrionConnReq_In;
	private boolean Block_Port_Operation_Call_Invalid_In;
	private boolean Connection_Port_Operation_Call_Connect_In;
	private Main_region_of_Orion_Master_SM main_region_of_Orion_Master_SM;
	private Region_2_in_Kapcsolodva_4 region_2_in_Kapcsolodva_4;
	private Region_1_in_Kapcsolodva_4 region_1_in_Kapcsolodva_4;
	private long TimeoutKapcsolodik_2;
	private long TimoeutKeepAliveReceiveTimeout_3;
	private long TimeoutKeepAliveSendTimeout_1;
	
	public Orion_Master_SMStatemachine() {
	}
	
	public void reset() {
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
		this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.TimoeutKeepAliveReceiveTimeout_3 = (5 * 1000);
		this.TimeoutKapcsolodik_2 = (5 * 1000);
		this.TimeoutKeepAliveSendTimeout_1 = (4 * 1000);
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
		this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
		this.StateMachine_Port_OrionDisconn_In = false;
		this.StateMachine_Port_OrionConnConf_In = false;
		this.StateMachine_Port_OrionDisconnCause_In = false;
		this.StateMachine_Port_OrionConnResp_In = false;
		this.StateMachine_Port_OrionKeepAlive_In = false;
		this.Connection_Port_Operation_Call_Disconn_In = false;
		this.StateMachine_Port_OrionAppData_In = false;
		this.Block_Port_Operation_Call_SendData_In = false;
		this.StateMachine_Port_OrionConnReq_In = false;
		this.Block_Port_Operation_Call_Invalid_In = false;
		this.Connection_Port_Operation_Call_Connect_In = false;
		this.Process_StateMachine_Port_OrionAppData_Out = false;
		this.Send_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Send_StateMachine_Port_OrionConnConf_Out = false;
		this.Send_StateMachine_Port_OrionDisconn_Out = false;
		this.Process_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Send_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Process_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Send_StateMachine_Port_OrionAppData_Out = false;
		this.Send_StateMachine_Port_OrionConnReq_Out = false;
		this.Process_StateMachine_Port_OrionDisconn_Out = false;
		this.Process_StateMachine_Port_OrionConnConf_Out = false;
		this.Process_StateMachine_Port_OrionConnReq_Out = false;
		this.Process_StateMachine_Port_OrionConnResp_Out = false;
		this.Send_StateMachine_Port_OrionConnResp_Out = false;
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		if ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0)) {
			this.TimeoutKapcsolodik_2 = 0;
		} else 
		if ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)) {
			this.TimeoutKapcsolodik_2 = 0;
		} else 
		if ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) {
			this.TimeoutKeepAliveSendTimeout_1 = 0;
			this.TimoeutKeepAliveReceiveTimeout_3 = 0;
		}
	}
	
	public void setProcess_StateMachine_Port_OrionAppData_Out(boolean Process_StateMachine_Port_OrionAppData_Out) {
		this.Process_StateMachine_Port_OrionAppData_Out = Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionAppData_Out() {
		return Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setSend_StateMachine_Port_OrionDisconnCause_Out(boolean Send_StateMachine_Port_OrionDisconnCause_Out) {
		this.Send_StateMachine_Port_OrionDisconnCause_Out = Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionDisconnCause_Out() {
		return Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnConf_Out(boolean Send_StateMachine_Port_OrionConnConf_Out) {
		this.Send_StateMachine_Port_OrionConnConf_Out = Send_StateMachine_Port_OrionConnConf_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnConf_Out() {
		return Send_StateMachine_Port_OrionConnConf_Out;
	}
	
	public void setSend_StateMachine_Port_OrionDisconn_Out(boolean Send_StateMachine_Port_OrionDisconn_Out) {
		this.Send_StateMachine_Port_OrionDisconn_Out = Send_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionDisconn_Out() {
		return Send_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionDisconnCause_Out(boolean Process_StateMachine_Port_OrionDisconnCause_Out) {
		this.Process_StateMachine_Port_OrionDisconnCause_Out = Process_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionDisconnCause_Out() {
		return Process_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public void setSend_StateMachine_Port_OrionKeepAlive_Out(boolean Send_StateMachine_Port_OrionKeepAlive_Out) {
		this.Send_StateMachine_Port_OrionKeepAlive_Out = Send_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionKeepAlive_Out() {
		return Send_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionKeepAlive_Out(boolean Process_StateMachine_Port_OrionKeepAlive_Out) {
		this.Process_StateMachine_Port_OrionKeepAlive_Out = Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionKeepAlive_Out() {
		return Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setSend_StateMachine_Port_OrionAppData_Out(boolean Send_StateMachine_Port_OrionAppData_Out) {
		this.Send_StateMachine_Port_OrionAppData_Out = Send_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionAppData_Out() {
		return Send_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnReq_Out(boolean Send_StateMachine_Port_OrionConnReq_Out) {
		this.Send_StateMachine_Port_OrionConnReq_Out = Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnReq_Out() {
		return Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionDisconn_Out(boolean Process_StateMachine_Port_OrionDisconn_Out) {
		this.Process_StateMachine_Port_OrionDisconn_Out = Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionDisconn_Out() {
		return Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnConf_Out(boolean Process_StateMachine_Port_OrionConnConf_Out) {
		this.Process_StateMachine_Port_OrionConnConf_Out = Process_StateMachine_Port_OrionConnConf_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnConf_Out() {
		return Process_StateMachine_Port_OrionConnConf_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnReq_Out(boolean Process_StateMachine_Port_OrionConnReq_Out) {
		this.Process_StateMachine_Port_OrionConnReq_Out = Process_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnReq_Out() {
		return Process_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnResp_Out(boolean Process_StateMachine_Port_OrionConnResp_Out) {
		this.Process_StateMachine_Port_OrionConnResp_Out = Process_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnResp_Out() {
		return Process_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnResp_Out(boolean Send_StateMachine_Port_OrionConnResp_Out) {
		this.Send_StateMachine_Port_OrionConnResp_Out = Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnResp_Out() {
		return Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setStateMachine_Port_OrionDisconn_In(boolean StateMachine_Port_OrionDisconn_In) {
		this.StateMachine_Port_OrionDisconn_In = StateMachine_Port_OrionDisconn_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconn_In() {
		return StateMachine_Port_OrionDisconn_In;
	}
	
	public void setStateMachine_Port_OrionConnConf_In(boolean StateMachine_Port_OrionConnConf_In) {
		this.StateMachine_Port_OrionConnConf_In = StateMachine_Port_OrionConnConf_In;
	}
	
	public boolean getStateMachine_Port_OrionConnConf_In() {
		return StateMachine_Port_OrionConnConf_In;
	}
	
	public void setStateMachine_Port_OrionDisconnCause_In(boolean StateMachine_Port_OrionDisconnCause_In) {
		this.StateMachine_Port_OrionDisconnCause_In = StateMachine_Port_OrionDisconnCause_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconnCause_In() {
		return StateMachine_Port_OrionDisconnCause_In;
	}
	
	public void setStateMachine_Port_OrionConnResp_In(boolean StateMachine_Port_OrionConnResp_In) {
		this.StateMachine_Port_OrionConnResp_In = StateMachine_Port_OrionConnResp_In;
	}
	
	public boolean getStateMachine_Port_OrionConnResp_In() {
		return StateMachine_Port_OrionConnResp_In;
	}
	
	public void setStateMachine_Port_OrionKeepAlive_In(boolean StateMachine_Port_OrionKeepAlive_In) {
		this.StateMachine_Port_OrionKeepAlive_In = StateMachine_Port_OrionKeepAlive_In;
	}
	
	public boolean getStateMachine_Port_OrionKeepAlive_In() {
		return StateMachine_Port_OrionKeepAlive_In;
	}
	
	public void setConnection_Port_Operation_Call_Disconn_In(boolean Connection_Port_Operation_Call_Disconn_In) {
		this.Connection_Port_Operation_Call_Disconn_In = Connection_Port_Operation_Call_Disconn_In;
	}
	
	public boolean getConnection_Port_Operation_Call_Disconn_In() {
		return Connection_Port_Operation_Call_Disconn_In;
	}
	
	public void setStateMachine_Port_OrionAppData_In(boolean StateMachine_Port_OrionAppData_In) {
		this.StateMachine_Port_OrionAppData_In = StateMachine_Port_OrionAppData_In;
	}
	
	public boolean getStateMachine_Port_OrionAppData_In() {
		return StateMachine_Port_OrionAppData_In;
	}
	
	public void setBlock_Port_Operation_Call_SendData_In(boolean Block_Port_Operation_Call_SendData_In) {
		this.Block_Port_Operation_Call_SendData_In = Block_Port_Operation_Call_SendData_In;
	}
	
	public boolean getBlock_Port_Operation_Call_SendData_In() {
		return Block_Port_Operation_Call_SendData_In;
	}
	
	public void setStateMachine_Port_OrionConnReq_In(boolean StateMachine_Port_OrionConnReq_In) {
		this.StateMachine_Port_OrionConnReq_In = StateMachine_Port_OrionConnReq_In;
	}
	
	public boolean getStateMachine_Port_OrionConnReq_In() {
		return StateMachine_Port_OrionConnReq_In;
	}
	
	public void setBlock_Port_Operation_Call_Invalid_In(boolean Block_Port_Operation_Call_Invalid_In) {
		this.Block_Port_Operation_Call_Invalid_In = Block_Port_Operation_Call_Invalid_In;
	}
	
	public boolean getBlock_Port_Operation_Call_Invalid_In() {
		return Block_Port_Operation_Call_Invalid_In;
	}
	
	public void setConnection_Port_Operation_Call_Connect_In(boolean Connection_Port_Operation_Call_Connect_In) {
		this.Connection_Port_Operation_Call_Connect_In = Connection_Port_Operation_Call_Connect_In;
	}
	
	public boolean getConnection_Port_Operation_Call_Connect_In() {
		return Connection_Port_Operation_Call_Connect_In;
	}
	
	public void setMain_region_of_Orion_Master_SM(Main_region_of_Orion_Master_SM main_region_of_Orion_Master_SM) {
		this.main_region_of_Orion_Master_SM = main_region_of_Orion_Master_SM;
	}
	
	public Main_region_of_Orion_Master_SM getMain_region_of_Orion_Master_SM() {
		return main_region_of_Orion_Master_SM;
	}
	
	public void setRegion_2_in_Kapcsolodva_4(Region_2_in_Kapcsolodva_4 region_2_in_Kapcsolodva_4) {
		this.region_2_in_Kapcsolodva_4 = region_2_in_Kapcsolodva_4;
	}
	
	public Region_2_in_Kapcsolodva_4 getRegion_2_in_Kapcsolodva_4() {
		return region_2_in_Kapcsolodva_4;
	}
	
	public void setRegion_1_in_Kapcsolodva_4(Region_1_in_Kapcsolodva_4 region_1_in_Kapcsolodva_4) {
		this.region_1_in_Kapcsolodva_4 = region_1_in_Kapcsolodva_4;
	}
	
	public Region_1_in_Kapcsolodva_4 getRegion_1_in_Kapcsolodva_4() {
		return region_1_in_Kapcsolodva_4;
	}
	
	public void setTimeoutKapcsolodik_2(long TimeoutKapcsolodik_2) {
		this.TimeoutKapcsolodik_2 = TimeoutKapcsolodik_2;
	}
	
	public long getTimeoutKapcsolodik_2() {
		return TimeoutKapcsolodik_2;
	}
	
	public void setTimoeutKeepAliveReceiveTimeout_3(long TimoeutKeepAliveReceiveTimeout_3) {
		this.TimoeutKeepAliveReceiveTimeout_3 = TimoeutKeepAliveReceiveTimeout_3;
	}
	
	public long getTimoeutKeepAliveReceiveTimeout_3() {
		return TimoeutKeepAliveReceiveTimeout_3;
	}
	
	public void setTimeoutKeepAliveSendTimeout_1(long TimeoutKeepAliveSendTimeout_1) {
		this.TimeoutKeepAliveSendTimeout_1 = TimeoutKeepAliveSendTimeout_1;
	}
	
	public long getTimeoutKeepAliveSendTimeout_1() {
		return TimeoutKeepAliveSendTimeout_1;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _2012625064 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2);
		boolean _158617141 = (this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3);
		boolean _1001372695 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0);
		boolean _1171678961 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4);
		boolean _1547973585 = (this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1);
		boolean _282341796 = ((_1171678961) && (_158617141));
		boolean _1432288238 = ((_1171678961) && (_1547973585));
		boolean _290782189 = ((_1171678961) && (_158617141));
		boolean _1420679617 = ((_1171678961) && (_158617141));
		boolean _2053146804 = ((_1171678961) && (_1547973585));
		boolean _guard_1571393317 = ((((((_1001372695) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0)))) && (((5 * 1000) <= this.TimeoutKapcsolodik_2)))));
		if (_guard_1571393317) {
			this.Send_StateMachine_Port_OrionConnReq_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodik_2;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_463220675 = ((((((_1001372695) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0)))) && (this.Connection_Port_Operation_Call_Connect_In))));
		if (_guard_463220675) {
			this.Send_StateMachine_Port_OrionConnReq_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodik_2;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_56993516 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_56993516) {
			this.Send_StateMachine_Port_OrionConnConf_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodva_4;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
			this.TimeoutKeepAliveSendTimeout_1 = 0;
			this.TimoeutKeepAliveReceiveTimeout_3 = 0;
		}
		boolean _guard_336222668 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (((5 * 1000) <= this.TimeoutKapcsolodik_2)))));
		if (_guard_336222668) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1228788041 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_1228788041) {
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1621109157 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_1621109157) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_453358497 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_453358497) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_924386907 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionKeepAlive_In))));
		if (_guard_924386907) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_914430691 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionAppData_In))));
		if (_guard_914430691) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_176635189 = ((((((_2012625064) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_176635189) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_2045718264 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.Connection_Port_Operation_Call_Disconn_In))));
		if (_guard_2045718264) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1585347895 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_1585347895) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_871952041 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_871952041) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1358332004 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_1358332004) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1364261409 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_1364261409) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1105805048 = ((((((_1171678961) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_1105805048) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1282541099 = ((((((_1432288238) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1)))))) && (((4 * 1000) <= this.TimeoutKeepAliveSendTimeout_1)))) && (((!(((_1171678961) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_1171678961) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_1282541099) {
			this.Send_StateMachine_Port_OrionKeepAlive_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
			this.TimeoutKeepAliveSendTimeout_1 = 0;
		}
		boolean _guard_1874703552 = ((((((_2053146804) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1)))))) && (this.Block_Port_Operation_Call_SendData_In))) && (((!(((_1171678961) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_1171678961) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_1874703552) {
			this.Send_StateMachine_Port_OrionAppData_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
			this.TimeoutKeepAliveSendTimeout_1 = 0;
		}
		boolean _guard_817166966 = ((((((_282341796) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && (((5 * 1000) <= this.TimoeutKeepAliveReceiveTimeout_3)))) && (((!(((_1171678961) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_1171678961) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_817166966) {
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
			this.TimeoutKapcsolodik_2 = 0;
		}
		boolean _guard_1707497077 = ((((((_1420679617) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && (this.StateMachine_Port_OrionAppData_In))) && (((!(((_1171678961) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_1171678961) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_1707497077) {
			this.Process_StateMachine_Port_OrionAppData_Out = true;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
			this.TimoeutKeepAliveReceiveTimeout_3 = 0;
		}
		boolean _guard_798201515 = ((((((_290782189) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && (this.StateMachine_Port_OrionKeepAlive_In))) && (((!(((_1171678961) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_1171678961) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_1171678961) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_798201515) {
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
			this.TimoeutKeepAliveReceiveTimeout_3 = 0;
		}
	}
	
	private void clearOutEvents() {
		Process_StateMachine_Port_OrionAppData_Out = false;
		Send_StateMachine_Port_OrionDisconnCause_Out = false;
		Send_StateMachine_Port_OrionConnConf_Out = false;
		Send_StateMachine_Port_OrionDisconn_Out = false;
		Process_StateMachine_Port_OrionDisconnCause_Out = false;
		Send_StateMachine_Port_OrionKeepAlive_Out = false;
		Process_StateMachine_Port_OrionKeepAlive_Out = false;
		Send_StateMachine_Port_OrionAppData_Out = false;
		Send_StateMachine_Port_OrionConnReq_Out = false;
		Process_StateMachine_Port_OrionDisconn_Out = false;
		Process_StateMachine_Port_OrionConnConf_Out = false;
		Process_StateMachine_Port_OrionConnReq_Out = false;
		Process_StateMachine_Port_OrionConnResp_Out = false;
		Send_StateMachine_Port_OrionConnResp_Out = false;
	}
	
	private void clearInEvents() {
		StateMachine_Port_OrionDisconn_In = false;
		StateMachine_Port_OrionConnConf_In = false;
		StateMachine_Port_OrionDisconnCause_In = false;
		StateMachine_Port_OrionConnResp_In = false;
		StateMachine_Port_OrionKeepAlive_In = false;
		Connection_Port_Operation_Call_Disconn_In = false;
		StateMachine_Port_OrionAppData_In = false;
		Block_Port_Operation_Call_SendData_In = false;
		StateMachine_Port_OrionConnReq_In = false;
		Block_Port_Operation_Call_Invalid_In = false;
		Connection_Port_Operation_Call_Connect_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Process_StateMachine_Port_OrionAppData_Out = " + Process_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconnCause_Out = " + Send_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnConf_Out = " + Send_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconn_Out = " + Send_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconnCause_Out = " + Process_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionKeepAlive_Out = " + Send_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionKeepAlive_Out = " + Process_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionAppData_Out = " + Send_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnReq_Out = " + Send_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconn_Out = " + Process_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnConf_Out = " + Process_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnReq_Out = " + Process_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnResp_Out = " + Process_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnResp_Out = " + Send_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"StateMachine_Port_OrionDisconn_In = " + StateMachine_Port_OrionDisconn_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnConf_In = " + StateMachine_Port_OrionConnConf_In + System.lineSeparator() +
			"StateMachine_Port_OrionDisconnCause_In = " + StateMachine_Port_OrionDisconnCause_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnResp_In = " + StateMachine_Port_OrionConnResp_In + System.lineSeparator() +
			"StateMachine_Port_OrionKeepAlive_In = " + StateMachine_Port_OrionKeepAlive_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Disconn_In = " + Connection_Port_Operation_Call_Disconn_In + System.lineSeparator() +
			"StateMachine_Port_OrionAppData_In = " + StateMachine_Port_OrionAppData_In + System.lineSeparator() +
			"Block_Port_Operation_Call_SendData_In = " + Block_Port_Operation_Call_SendData_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnReq_In = " + StateMachine_Port_OrionConnReq_In + System.lineSeparator() +
			"Block_Port_Operation_Call_Invalid_In = " + Block_Port_Operation_Call_Invalid_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Connect_In = " + Connection_Port_Operation_Call_Connect_In + System.lineSeparator() +
			"main_region_of_Orion_Master_SM = " + main_region_of_Orion_Master_SM + System.lineSeparator() +
			"region_2_in_Kapcsolodva_4 = " + region_2_in_Kapcsolodva_4 + System.lineSeparator() +
			"region_1_in_Kapcsolodva_4 = " + region_1_in_Kapcsolodva_4 + System.lineSeparator() +
			"TimeoutKapcsolodik_2 = " + TimeoutKapcsolodik_2 + System.lineSeparator() +
			"TimoeutKeepAliveReceiveTimeout_3 = " + TimoeutKeepAliveReceiveTimeout_3 + System.lineSeparator() +
			"TimeoutKeepAliveSendTimeout_1 = " + TimeoutKeepAliveSendTimeout_1
		;
	}
	
}
