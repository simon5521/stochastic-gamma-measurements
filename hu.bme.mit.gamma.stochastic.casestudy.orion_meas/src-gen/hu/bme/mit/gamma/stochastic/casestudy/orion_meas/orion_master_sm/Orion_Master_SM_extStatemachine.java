package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.orion_master_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*; 		

public class Orion_Master_SM_extStatemachine {
	
	enum Main_region_of_Orion_Master_SM {__Inactive__, Zarva_0, Kapcsolodik_2, Kapcsolodva_4}
	enum Region_2_in_Kapcsolodva_4 {__Inactive__, KeepAliveReceiveTimeout_3}
	enum Region_1_in_Kapcsolodva_4 {__Inactive__, KeepAliveSendTimeout_1}
	private boolean StateMachine_Port_OrionDisconn_In;
	private boolean StateMachine_Port_OrionKeepAlive_In;
	private boolean Connection_Port_Operation_Call_Disconn_In;
	private boolean TimeoutKeepAliveSendTimeout_1_newEvent_In;
	private boolean Connection_Port_Operation_Call_Connect_In;
	private boolean Block_Port_Operation_Call_Invalid_In;
	private boolean StateMachine_Port_OrionConnReq_In;
	private boolean TimeoutKapcsolodik_2_newEvent_In;
	private boolean TimoeutKeepAliveReceiveTimeout_3_newEvent_In;
	private boolean StateMachine_Port_OrionConnResp_In;
	private boolean StateMachine_Port_OrionAppData_In;
	private boolean Block_Port_Operation_Call_SendData_In;
	private boolean StateMachine_Port_OrionConnConf_In;
	private boolean TimeoutZarva_0_newEvent_In;
	private boolean StateMachine_Port_OrionDisconnCause_In;
	private boolean Send_StateMachine_Port_OrionConnReq_Out;
	private boolean Send_StateMachine_Port_OrionAppData_Out;
	private boolean Process_StateMachine_Port_OrionAppData_Out;
	private boolean Process_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Send_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Status_disconn_Out;
	private boolean Process_StateMachine_Port_OrionDisconn_Out;
	private boolean Process_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Process_StateMachine_Port_OrionConnConf_Out;
	private boolean Process_StateMachine_Port_OrionConnReq_Out;
	private boolean Send_StateMachine_Port_OrionConnConf_Out;
	private boolean Send_StateMachine_Port_OrionConnResp_Out;
	private boolean Send_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Send_StateMachine_Port_OrionDisconn_Out;
	private boolean Status_conn_Out;
	private boolean Process_StateMachine_Port_OrionConnResp_Out;
	private Main_region_of_Orion_Master_SM main_region_of_Orion_Master_SM;
	private Region_2_in_Kapcsolodva_4 region_2_in_Kapcsolodva_4;
	private Region_1_in_Kapcsolodva_4 region_1_in_Kapcsolodva_4;
	
	public Orion_Master_SM_extStatemachine() {
	}
	
	public void reset() {
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
		this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
		this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
		this.StateMachine_Port_OrionDisconn_In = false;
		this.StateMachine_Port_OrionKeepAlive_In = false;
		this.Connection_Port_Operation_Call_Disconn_In = false;
		this.TimeoutKeepAliveSendTimeout_1_newEvent_In = false;
		this.Connection_Port_Operation_Call_Connect_In = false;
		this.Block_Port_Operation_Call_Invalid_In = false;
		this.StateMachine_Port_OrionConnReq_In = false;
		this.TimeoutKapcsolodik_2_newEvent_In = false;
		this.TimoeutKeepAliveReceiveTimeout_3_newEvent_In = false;
		this.StateMachine_Port_OrionConnResp_In = false;
		this.StateMachine_Port_OrionAppData_In = false;
		this.Block_Port_Operation_Call_SendData_In = false;
		this.StateMachine_Port_OrionConnConf_In = false;
		this.TimeoutZarva_0_newEvent_In = false;
		this.StateMachine_Port_OrionDisconnCause_In = false;
		this.Send_StateMachine_Port_OrionConnReq_Out = false;
		this.Send_StateMachine_Port_OrionAppData_Out = false;
		this.Process_StateMachine_Port_OrionAppData_Out = false;
		this.Process_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Send_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Status_disconn_Out = false;
		this.Process_StateMachine_Port_OrionDisconn_Out = false;
		this.Process_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Process_StateMachine_Port_OrionConnConf_Out = false;
		this.Process_StateMachine_Port_OrionConnReq_Out = false;
		this.Send_StateMachine_Port_OrionConnConf_Out = false;
		this.Send_StateMachine_Port_OrionConnResp_Out = false;
		this.Send_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Send_StateMachine_Port_OrionDisconn_Out = false;
		this.Status_conn_Out = false;
		this.Process_StateMachine_Port_OrionConnResp_Out = false;
		this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		if ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) {
			this.Status_conn_Out = true;
		}
	}
	
	public void setStateMachine_Port_OrionDisconn_In(boolean StateMachine_Port_OrionDisconn_In) {
		this.StateMachine_Port_OrionDisconn_In = StateMachine_Port_OrionDisconn_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconn_In() {
		return StateMachine_Port_OrionDisconn_In;
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
	
	public void setTimeoutKeepAliveSendTimeout_1_newEvent_In(boolean TimeoutKeepAliveSendTimeout_1_newEvent_In) {
		this.TimeoutKeepAliveSendTimeout_1_newEvent_In = TimeoutKeepAliveSendTimeout_1_newEvent_In;
	}
	
	public boolean getTimeoutKeepAliveSendTimeout_1_newEvent_In() {
		return TimeoutKeepAliveSendTimeout_1_newEvent_In;
	}
	
	public void setConnection_Port_Operation_Call_Connect_In(boolean Connection_Port_Operation_Call_Connect_In) {
		this.Connection_Port_Operation_Call_Connect_In = Connection_Port_Operation_Call_Connect_In;
	}
	
	public boolean getConnection_Port_Operation_Call_Connect_In() {
		return Connection_Port_Operation_Call_Connect_In;
	}
	
	public void setBlock_Port_Operation_Call_Invalid_In(boolean Block_Port_Operation_Call_Invalid_In) {
		this.Block_Port_Operation_Call_Invalid_In = Block_Port_Operation_Call_Invalid_In;
	}
	
	public boolean getBlock_Port_Operation_Call_Invalid_In() {
		return Block_Port_Operation_Call_Invalid_In;
	}
	
	public void setStateMachine_Port_OrionConnReq_In(boolean StateMachine_Port_OrionConnReq_In) {
		this.StateMachine_Port_OrionConnReq_In = StateMachine_Port_OrionConnReq_In;
	}
	
	public boolean getStateMachine_Port_OrionConnReq_In() {
		return StateMachine_Port_OrionConnReq_In;
	}
	
	public void setTimeoutKapcsolodik_2_newEvent_In(boolean TimeoutKapcsolodik_2_newEvent_In) {
		this.TimeoutKapcsolodik_2_newEvent_In = TimeoutKapcsolodik_2_newEvent_In;
	}
	
	public boolean getTimeoutKapcsolodik_2_newEvent_In() {
		return TimeoutKapcsolodik_2_newEvent_In;
	}
	
	public void setTimoeutKeepAliveReceiveTimeout_3_newEvent_In(boolean TimoeutKeepAliveReceiveTimeout_3_newEvent_In) {
		this.TimoeutKeepAliveReceiveTimeout_3_newEvent_In = TimoeutKeepAliveReceiveTimeout_3_newEvent_In;
	}
	
	public boolean getTimoeutKeepAliveReceiveTimeout_3_newEvent_In() {
		return TimoeutKeepAliveReceiveTimeout_3_newEvent_In;
	}
	
	public void setStateMachine_Port_OrionConnResp_In(boolean StateMachine_Port_OrionConnResp_In) {
		this.StateMachine_Port_OrionConnResp_In = StateMachine_Port_OrionConnResp_In;
	}
	
	public boolean getStateMachine_Port_OrionConnResp_In() {
		return StateMachine_Port_OrionConnResp_In;
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
	
	public void setStateMachine_Port_OrionConnConf_In(boolean StateMachine_Port_OrionConnConf_In) {
		this.StateMachine_Port_OrionConnConf_In = StateMachine_Port_OrionConnConf_In;
	}
	
	public boolean getStateMachine_Port_OrionConnConf_In() {
		return StateMachine_Port_OrionConnConf_In;
	}
	
	public void setTimeoutZarva_0_newEvent_In(boolean TimeoutZarva_0_newEvent_In) {
		this.TimeoutZarva_0_newEvent_In = TimeoutZarva_0_newEvent_In;
	}
	
	public boolean getTimeoutZarva_0_newEvent_In() {
		return TimeoutZarva_0_newEvent_In;
	}
	
	public void setStateMachine_Port_OrionDisconnCause_In(boolean StateMachine_Port_OrionDisconnCause_In) {
		this.StateMachine_Port_OrionDisconnCause_In = StateMachine_Port_OrionDisconnCause_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconnCause_In() {
		return StateMachine_Port_OrionDisconnCause_In;
	}
	
	public void setSend_StateMachine_Port_OrionConnReq_Out(boolean Send_StateMachine_Port_OrionConnReq_Out) {
		this.Send_StateMachine_Port_OrionConnReq_Out = Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnReq_Out() {
		return Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setSend_StateMachine_Port_OrionAppData_Out(boolean Send_StateMachine_Port_OrionAppData_Out) {
		this.Send_StateMachine_Port_OrionAppData_Out = Send_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionAppData_Out() {
		return Send_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionAppData_Out(boolean Process_StateMachine_Port_OrionAppData_Out) {
		this.Process_StateMachine_Port_OrionAppData_Out = Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionAppData_Out() {
		return Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionKeepAlive_Out(boolean Process_StateMachine_Port_OrionKeepAlive_Out) {
		this.Process_StateMachine_Port_OrionKeepAlive_Out = Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionKeepAlive_Out() {
		return Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setSend_StateMachine_Port_OrionDisconnCause_Out(boolean Send_StateMachine_Port_OrionDisconnCause_Out) {
		this.Send_StateMachine_Port_OrionDisconnCause_Out = Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionDisconnCause_Out() {
		return Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public void setStatus_disconn_Out(boolean Status_disconn_Out) {
		this.Status_disconn_Out = Status_disconn_Out;
	}
	
	public boolean getStatus_disconn_Out() {
		return Status_disconn_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionDisconn_Out(boolean Process_StateMachine_Port_OrionDisconn_Out) {
		this.Process_StateMachine_Port_OrionDisconn_Out = Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionDisconn_Out() {
		return Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionDisconnCause_Out(boolean Process_StateMachine_Port_OrionDisconnCause_Out) {
		this.Process_StateMachine_Port_OrionDisconnCause_Out = Process_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionDisconnCause_Out() {
		return Process_StateMachine_Port_OrionDisconnCause_Out;
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
	
	public void setSend_StateMachine_Port_OrionConnConf_Out(boolean Send_StateMachine_Port_OrionConnConf_Out) {
		this.Send_StateMachine_Port_OrionConnConf_Out = Send_StateMachine_Port_OrionConnConf_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnConf_Out() {
		return Send_StateMachine_Port_OrionConnConf_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnResp_Out(boolean Send_StateMachine_Port_OrionConnResp_Out) {
		this.Send_StateMachine_Port_OrionConnResp_Out = Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnResp_Out() {
		return Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setSend_StateMachine_Port_OrionKeepAlive_Out(boolean Send_StateMachine_Port_OrionKeepAlive_Out) {
		this.Send_StateMachine_Port_OrionKeepAlive_Out = Send_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionKeepAlive_Out() {
		return Send_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setSend_StateMachine_Port_OrionDisconn_Out(boolean Send_StateMachine_Port_OrionDisconn_Out) {
		this.Send_StateMachine_Port_OrionDisconn_Out = Send_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionDisconn_Out() {
		return Send_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setStatus_conn_Out(boolean Status_conn_Out) {
		this.Status_conn_Out = Status_conn_Out;
	}
	
	public boolean getStatus_conn_Out() {
		return Status_conn_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnResp_Out(boolean Process_StateMachine_Port_OrionConnResp_Out) {
		this.Process_StateMachine_Port_OrionConnResp_Out = Process_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnResp_Out() {
		return Process_StateMachine_Port_OrionConnResp_Out;
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
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _2049568740 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2);
		boolean _633456758 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4);
		boolean _205351251 = (this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1);
		boolean _1830187398 = (this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0);
		boolean _1672663753 = (this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3);
		boolean _987062864 = ((_633456758) && (_205351251));
		boolean _1102806013 = ((_633456758) && (_1672663753));
		boolean _744089068 = ((_633456758) && (_1672663753));
		boolean _687118764 = ((_633456758) && (_205351251));
		boolean _1308753220 = ((_633456758) && (_1672663753));
		boolean _guard_1481183066 = ((((((_1830187398) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0)))) && ((this.TimeoutZarva_0_newEvent_In)))));
		if (_guard_1481183066) {
			this.Send_StateMachine_Port_OrionConnReq_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodik_2;
		}
		boolean _guard_82118538 = ((((((_1830187398) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Zarva_0)))) && (this.Connection_Port_Operation_Call_Connect_In))));
		if (_guard_82118538) {
			this.Send_StateMachine_Port_OrionConnReq_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodik_2;
		}
		boolean _guard_179139180 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_179139180) {
			this.Send_StateMachine_Port_OrionConnConf_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Kapcsolodva_4;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
			this.Status_conn_Out = true;
		}
		boolean _guard_880665620 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && ((this.TimeoutKapcsolodik_2_newEvent_In)))));
		if (_guard_880665620) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_41598027 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_41598027) {
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_548357123 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_548357123) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_1118414337 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_1118414337) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_126136522 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionKeepAlive_In))));
		if (_guard_126136522) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_317908756 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionAppData_In))));
		if (_guard_317908756) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_1985983379 = ((((((_2049568740) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodik_2)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_1985983379) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_2003804404 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.Connection_Port_Operation_Call_Disconn_In))));
		if (_guard_2003804404) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_724107546 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_724107546) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_1334151757 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_1334151757) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_1919026883 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_1919026883) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_766063103 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_766063103) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_1037534185 = ((((((_633456758) && ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_1037534185) {
			this.Status_disconn_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_754318676 = ((((((_987062864) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1)))))) && ((this.TimeoutKeepAliveSendTimeout_1_newEvent_In)))) && (((!(((_633456758) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_633456758) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_754318676) {
			this.Send_StateMachine_Port_OrionKeepAlive_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
		}
		boolean _guard_919095851 = ((((((_687118764) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_1_in_Kapcsolodva_4 == Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1)))))) && (this.Block_Port_Operation_Call_SendData_In))) && (((!(((_633456758) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_633456758) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_919095851) {
			this.Send_StateMachine_Port_OrionAppData_Out = true;
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.KeepAliveSendTimeout_1;
		}
		boolean _guard_591385723 = ((((((_744089068) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && ((this.TimoeutKeepAliveReceiveTimeout_3_newEvent_In)))) && (((!(((_633456758) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_633456758) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_591385723) {
			if ((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) {
				this.Status_disconn_Out = true;
			}
			this.region_1_in_Kapcsolodva_4 = Region_1_in_Kapcsolodva_4.__Inactive__;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Master_SM = Main_region_of_Orion_Master_SM.Zarva_0;
		}
		boolean _guard_482448868 = ((((((_1102806013) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && (this.StateMachine_Port_OrionAppData_In))) && (((!(((_633456758) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_633456758) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_482448868) {
			this.Process_StateMachine_Port_OrionAppData_Out = true;
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
		}
		boolean _guard_1379101776 = ((((((_1308753220) && ((((this.main_region_of_Orion_Master_SM == Main_region_of_Orion_Master_SM.Kapcsolodva_4)) && ((this.region_2_in_Kapcsolodva_4 == Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3)))))) && (this.StateMachine_Port_OrionKeepAlive_In))) && (((!(((_633456758) && (this.Connection_Port_Operation_Call_Disconn_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_633456758) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_633456758) && (this.StateMachine_Port_OrionConnConf_In)))))));
		if (_guard_1379101776) {
			this.region_2_in_Kapcsolodva_4 = Region_2_in_Kapcsolodva_4.KeepAliveReceiveTimeout_3;
		}
	}
	
	private void clearOutEvents() {
		Send_StateMachine_Port_OrionConnReq_Out = false;
		Send_StateMachine_Port_OrionAppData_Out = false;
		Process_StateMachine_Port_OrionAppData_Out = false;
		Process_StateMachine_Port_OrionKeepAlive_Out = false;
		Send_StateMachine_Port_OrionDisconnCause_Out = false;
		Status_disconn_Out = false;
		Process_StateMachine_Port_OrionDisconn_Out = false;
		Process_StateMachine_Port_OrionDisconnCause_Out = false;
		Process_StateMachine_Port_OrionConnConf_Out = false;
		Process_StateMachine_Port_OrionConnReq_Out = false;
		Send_StateMachine_Port_OrionConnConf_Out = false;
		Send_StateMachine_Port_OrionConnResp_Out = false;
		Send_StateMachine_Port_OrionKeepAlive_Out = false;
		Send_StateMachine_Port_OrionDisconn_Out = false;
		Status_conn_Out = false;
		Process_StateMachine_Port_OrionConnResp_Out = false;
	}
	
	private void clearInEvents() {
		StateMachine_Port_OrionDisconn_In = false;
		StateMachine_Port_OrionKeepAlive_In = false;
		Connection_Port_Operation_Call_Disconn_In = false;
		TimeoutKeepAliveSendTimeout_1_newEvent_In = false;
		Connection_Port_Operation_Call_Connect_In = false;
		Block_Port_Operation_Call_Invalid_In = false;
		StateMachine_Port_OrionConnReq_In = false;
		TimeoutKapcsolodik_2_newEvent_In = false;
		TimoeutKeepAliveReceiveTimeout_3_newEvent_In = false;
		StateMachine_Port_OrionConnResp_In = false;
		StateMachine_Port_OrionAppData_In = false;
		Block_Port_Operation_Call_SendData_In = false;
		StateMachine_Port_OrionConnConf_In = false;
		TimeoutZarva_0_newEvent_In = false;
		StateMachine_Port_OrionDisconnCause_In = false;
	}
	
	@Override
	public String toString() {
		return
			"StateMachine_Port_OrionDisconn_In = " + StateMachine_Port_OrionDisconn_In + System.lineSeparator() +
			"StateMachine_Port_OrionKeepAlive_In = " + StateMachine_Port_OrionKeepAlive_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Disconn_In = " + Connection_Port_Operation_Call_Disconn_In + System.lineSeparator() +
			"TimeoutKeepAliveSendTimeout_1_newEvent_In = " + TimeoutKeepAliveSendTimeout_1_newEvent_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Connect_In = " + Connection_Port_Operation_Call_Connect_In + System.lineSeparator() +
			"Block_Port_Operation_Call_Invalid_In = " + Block_Port_Operation_Call_Invalid_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnReq_In = " + StateMachine_Port_OrionConnReq_In + System.lineSeparator() +
			"TimeoutKapcsolodik_2_newEvent_In = " + TimeoutKapcsolodik_2_newEvent_In + System.lineSeparator() +
			"TimoeutKeepAliveReceiveTimeout_3_newEvent_In = " + TimoeutKeepAliveReceiveTimeout_3_newEvent_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnResp_In = " + StateMachine_Port_OrionConnResp_In + System.lineSeparator() +
			"StateMachine_Port_OrionAppData_In = " + StateMachine_Port_OrionAppData_In + System.lineSeparator() +
			"Block_Port_Operation_Call_SendData_In = " + Block_Port_Operation_Call_SendData_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnConf_In = " + StateMachine_Port_OrionConnConf_In + System.lineSeparator() +
			"TimeoutZarva_0_newEvent_In = " + TimeoutZarva_0_newEvent_In + System.lineSeparator() +
			"StateMachine_Port_OrionDisconnCause_In = " + StateMachine_Port_OrionDisconnCause_In + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnReq_Out = " + Send_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionAppData_Out = " + Send_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionAppData_Out = " + Process_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionKeepAlive_Out = " + Process_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconnCause_Out = " + Send_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Status_disconn_Out = " + Status_disconn_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconn_Out = " + Process_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconnCause_Out = " + Process_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnConf_Out = " + Process_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnReq_Out = " + Process_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnConf_Out = " + Send_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnResp_Out = " + Send_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionKeepAlive_Out = " + Send_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconn_Out = " + Send_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Status_conn_Out = " + Status_conn_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnResp_Out = " + Process_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"main_region_of_Orion_Master_SM = " + main_region_of_Orion_Master_SM + System.lineSeparator() +
			"region_2_in_Kapcsolodva_4 = " + region_2_in_Kapcsolodva_4 + System.lineSeparator() +
			"region_1_in_Kapcsolodva_4 = " + region_1_in_Kapcsolodva_4
		;
	}
	
}
