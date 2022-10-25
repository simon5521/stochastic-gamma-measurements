package hu.bme.mit.gamma.stochastic.casestudy.orion.orion_slave_sm;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*; 		

public class Orion_Slave_SMStatemachine {
	
	enum Main_region_of_Orion_Slave_SM {__Inactive__, Kapcsolodva_1, Zarva_2, Kapcsolodik_3}
	enum Region_2_in_Kapcsolodva_1 {__Inactive__, KeepAliveReceiveTimeout_4}
	enum Region_1_in_Kapcsolodva_1 {__Inactive__, KeepAliveSendTimeout_0}
	private boolean Handle_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Handle_StateMachine_Port_OrionConnConf_Out;
	private boolean Send_StateMachine_Port_OrionConnConf_Out;
	private boolean Send_StateMachine_Port_OrionDisconn_Out;
	private boolean Handle_StateMachine_Port_OrionConnReq_Out;
	private boolean Handle_StateMachine_Port_OrionAppData_Out;
	private boolean Process_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Handle_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Send_StateMachine_Port_OrionConnResp_Out;
	private boolean Handle_StateMachine_Port_OrionConnResp_Out;
	private boolean Process_StateMachine_Port_OrionConnReq_Out;
	private boolean Handle_StateMachine_Port_OrionDisconn_Out;
	private boolean Send_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Send_StateMachine_Port_OrionAppData_Out;
	private boolean Process_StateMachine_Port_OrionDisconnCause_Out;
	private boolean Send_StateMachine_Port_OrionKeepAlive_Out;
	private boolean Process_StateMachine_Port_OrionConnResp_Out;
	private boolean Process_StateMachine_Port_OrionConnConf_Out;
	private boolean Send_StateMachine_Port_OrionConnReq_Out;
	private boolean Process_StateMachine_Port_OrionAppData_Out;
	private boolean Process_StateMachine_Port_OrionDisconn_Out;
	private boolean StateMachine_Port_OrionConnResp_In;
	private boolean StateMachine_Port_OrionConnConf_In;
	private boolean StateMachine_Port_OrionConnReq_In;
	private boolean StateMachine_Port_OrionDisconn_In;
	private boolean Block_Port_Operation_Call_Invalid_In;
	private boolean Block_Port_Operation_Call_SendData_In;
	private boolean StateMachine_Port_OrionAppData_In;
	private boolean StateMachine_Port_OrionKeepAlive_In;
	private boolean Connection_Port_Operation_Call_Connect_In;
	private boolean StateMachine_Port_OrionDisconnCause_In;
	private boolean Connection_Port_Operation_Call_Disconn_In;
	private Main_region_of_Orion_Slave_SM main_region_of_Orion_Slave_SM;
	private Region_2_in_Kapcsolodva_1 region_2_in_Kapcsolodva_1;
	private Region_1_in_Kapcsolodva_1 region_1_in_Kapcsolodva_1;
	private long TimeoutKeepAliveReceiveTimeout_4;
	private long TimeoutKapcsolodik_3;
	private long TimeoutKeepAliveSendTimeout_0;
	
	public Orion_Slave_SMStatemachine() {
	}
	
	public void reset() {
		this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
		this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.TimeoutKeepAliveReceiveTimeout_4 = (5 * 1000);
		this.TimeoutKapcsolodik_3 = (5 * 1000);
		this.TimeoutKeepAliveSendTimeout_0 = (4 * 1000);
		this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.__Inactive__;
		this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
		this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
		this.StateMachine_Port_OrionConnResp_In = false;
		this.StateMachine_Port_OrionConnConf_In = false;
		this.StateMachine_Port_OrionConnReq_In = false;
		this.StateMachine_Port_OrionDisconn_In = false;
		this.Block_Port_Operation_Call_Invalid_In = false;
		this.Block_Port_Operation_Call_SendData_In = false;
		this.StateMachine_Port_OrionAppData_In = false;
		this.StateMachine_Port_OrionKeepAlive_In = false;
		this.Connection_Port_Operation_Call_Connect_In = false;
		this.StateMachine_Port_OrionDisconnCause_In = false;
		this.Connection_Port_Operation_Call_Disconn_In = false;
		this.Handle_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Handle_StateMachine_Port_OrionConnConf_Out = false;
		this.Send_StateMachine_Port_OrionConnConf_Out = false;
		this.Send_StateMachine_Port_OrionDisconn_Out = false;
		this.Handle_StateMachine_Port_OrionConnReq_Out = false;
		this.Handle_StateMachine_Port_OrionAppData_Out = false;
		this.Process_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Handle_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Send_StateMachine_Port_OrionConnResp_Out = false;
		this.Handle_StateMachine_Port_OrionConnResp_Out = false;
		this.Process_StateMachine_Port_OrionConnReq_Out = false;
		this.Handle_StateMachine_Port_OrionDisconn_Out = false;
		this.Send_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Send_StateMachine_Port_OrionAppData_Out = false;
		this.Process_StateMachine_Port_OrionDisconnCause_Out = false;
		this.Send_StateMachine_Port_OrionKeepAlive_Out = false;
		this.Process_StateMachine_Port_OrionConnResp_Out = false;
		this.Process_StateMachine_Port_OrionConnConf_Out = false;
		this.Send_StateMachine_Port_OrionConnReq_Out = false;
		this.Process_StateMachine_Port_OrionAppData_Out = false;
		this.Process_StateMachine_Port_OrionDisconn_Out = false;
		this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		if ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) {
			this.TimeoutKeepAliveSendTimeout_0 = 0;
			this.TimeoutKeepAliveReceiveTimeout_4 = 0;
		} else 
		if ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)) {
			this.TimeoutKapcsolodik_3 = 0;
		}
	}
	
	public void setHandle_StateMachine_Port_OrionDisconnCause_Out(boolean Handle_StateMachine_Port_OrionDisconnCause_Out) {
		this.Handle_StateMachine_Port_OrionDisconnCause_Out = Handle_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionDisconnCause_Out() {
		return Handle_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public void setHandle_StateMachine_Port_OrionConnConf_Out(boolean Handle_StateMachine_Port_OrionConnConf_Out) {
		this.Handle_StateMachine_Port_OrionConnConf_Out = Handle_StateMachine_Port_OrionConnConf_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionConnConf_Out() {
		return Handle_StateMachine_Port_OrionConnConf_Out;
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
	
	public void setHandle_StateMachine_Port_OrionConnReq_Out(boolean Handle_StateMachine_Port_OrionConnReq_Out) {
		this.Handle_StateMachine_Port_OrionConnReq_Out = Handle_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionConnReq_Out() {
		return Handle_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setHandle_StateMachine_Port_OrionAppData_Out(boolean Handle_StateMachine_Port_OrionAppData_Out) {
		this.Handle_StateMachine_Port_OrionAppData_Out = Handle_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionAppData_Out() {
		return Handle_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionKeepAlive_Out(boolean Process_StateMachine_Port_OrionKeepAlive_Out) {
		this.Process_StateMachine_Port_OrionKeepAlive_Out = Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionKeepAlive_Out() {
		return Process_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setHandle_StateMachine_Port_OrionKeepAlive_Out(boolean Handle_StateMachine_Port_OrionKeepAlive_Out) {
		this.Handle_StateMachine_Port_OrionKeepAlive_Out = Handle_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionKeepAlive_Out() {
		return Handle_StateMachine_Port_OrionKeepAlive_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnResp_Out(boolean Send_StateMachine_Port_OrionConnResp_Out) {
		this.Send_StateMachine_Port_OrionConnResp_Out = Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnResp_Out() {
		return Send_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setHandle_StateMachine_Port_OrionConnResp_Out(boolean Handle_StateMachine_Port_OrionConnResp_Out) {
		this.Handle_StateMachine_Port_OrionConnResp_Out = Handle_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionConnResp_Out() {
		return Handle_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnReq_Out(boolean Process_StateMachine_Port_OrionConnReq_Out) {
		this.Process_StateMachine_Port_OrionConnReq_Out = Process_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnReq_Out() {
		return Process_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setHandle_StateMachine_Port_OrionDisconn_Out(boolean Handle_StateMachine_Port_OrionDisconn_Out) {
		this.Handle_StateMachine_Port_OrionDisconn_Out = Handle_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getHandle_StateMachine_Port_OrionDisconn_Out() {
		return Handle_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setSend_StateMachine_Port_OrionDisconnCause_Out(boolean Send_StateMachine_Port_OrionDisconnCause_Out) {
		this.Send_StateMachine_Port_OrionDisconnCause_Out = Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionDisconnCause_Out() {
		return Send_StateMachine_Port_OrionDisconnCause_Out;
	}
	
	public void setSend_StateMachine_Port_OrionAppData_Out(boolean Send_StateMachine_Port_OrionAppData_Out) {
		this.Send_StateMachine_Port_OrionAppData_Out = Send_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionAppData_Out() {
		return Send_StateMachine_Port_OrionAppData_Out;
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
	
	public void setProcess_StateMachine_Port_OrionConnResp_Out(boolean Process_StateMachine_Port_OrionConnResp_Out) {
		this.Process_StateMachine_Port_OrionConnResp_Out = Process_StateMachine_Port_OrionConnResp_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnResp_Out() {
		return Process_StateMachine_Port_OrionConnResp_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionConnConf_Out(boolean Process_StateMachine_Port_OrionConnConf_Out) {
		this.Process_StateMachine_Port_OrionConnConf_Out = Process_StateMachine_Port_OrionConnConf_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionConnConf_Out() {
		return Process_StateMachine_Port_OrionConnConf_Out;
	}
	
	public void setSend_StateMachine_Port_OrionConnReq_Out(boolean Send_StateMachine_Port_OrionConnReq_Out) {
		this.Send_StateMachine_Port_OrionConnReq_Out = Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public boolean getSend_StateMachine_Port_OrionConnReq_Out() {
		return Send_StateMachine_Port_OrionConnReq_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionAppData_Out(boolean Process_StateMachine_Port_OrionAppData_Out) {
		this.Process_StateMachine_Port_OrionAppData_Out = Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionAppData_Out() {
		return Process_StateMachine_Port_OrionAppData_Out;
	}
	
	public void setProcess_StateMachine_Port_OrionDisconn_Out(boolean Process_StateMachine_Port_OrionDisconn_Out) {
		this.Process_StateMachine_Port_OrionDisconn_Out = Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public boolean getProcess_StateMachine_Port_OrionDisconn_Out() {
		return Process_StateMachine_Port_OrionDisconn_Out;
	}
	
	public void setStateMachine_Port_OrionConnResp_In(boolean StateMachine_Port_OrionConnResp_In) {
		this.StateMachine_Port_OrionConnResp_In = StateMachine_Port_OrionConnResp_In;
	}
	
	public boolean getStateMachine_Port_OrionConnResp_In() {
		return StateMachine_Port_OrionConnResp_In;
	}
	
	public void setStateMachine_Port_OrionConnConf_In(boolean StateMachine_Port_OrionConnConf_In) {
		this.StateMachine_Port_OrionConnConf_In = StateMachine_Port_OrionConnConf_In;
	}
	
	public boolean getStateMachine_Port_OrionConnConf_In() {
		return StateMachine_Port_OrionConnConf_In;
	}
	
	public void setStateMachine_Port_OrionConnReq_In(boolean StateMachine_Port_OrionConnReq_In) {
		this.StateMachine_Port_OrionConnReq_In = StateMachine_Port_OrionConnReq_In;
	}
	
	public boolean getStateMachine_Port_OrionConnReq_In() {
		return StateMachine_Port_OrionConnReq_In;
	}
	
	public void setStateMachine_Port_OrionDisconn_In(boolean StateMachine_Port_OrionDisconn_In) {
		this.StateMachine_Port_OrionDisconn_In = StateMachine_Port_OrionDisconn_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconn_In() {
		return StateMachine_Port_OrionDisconn_In;
	}
	
	public void setBlock_Port_Operation_Call_Invalid_In(boolean Block_Port_Operation_Call_Invalid_In) {
		this.Block_Port_Operation_Call_Invalid_In = Block_Port_Operation_Call_Invalid_In;
	}
	
	public boolean getBlock_Port_Operation_Call_Invalid_In() {
		return Block_Port_Operation_Call_Invalid_In;
	}
	
	public void setBlock_Port_Operation_Call_SendData_In(boolean Block_Port_Operation_Call_SendData_In) {
		this.Block_Port_Operation_Call_SendData_In = Block_Port_Operation_Call_SendData_In;
	}
	
	public boolean getBlock_Port_Operation_Call_SendData_In() {
		return Block_Port_Operation_Call_SendData_In;
	}
	
	public void setStateMachine_Port_OrionAppData_In(boolean StateMachine_Port_OrionAppData_In) {
		this.StateMachine_Port_OrionAppData_In = StateMachine_Port_OrionAppData_In;
	}
	
	public boolean getStateMachine_Port_OrionAppData_In() {
		return StateMachine_Port_OrionAppData_In;
	}
	
	public void setStateMachine_Port_OrionKeepAlive_In(boolean StateMachine_Port_OrionKeepAlive_In) {
		this.StateMachine_Port_OrionKeepAlive_In = StateMachine_Port_OrionKeepAlive_In;
	}
	
	public boolean getStateMachine_Port_OrionKeepAlive_In() {
		return StateMachine_Port_OrionKeepAlive_In;
	}
	
	public void setConnection_Port_Operation_Call_Connect_In(boolean Connection_Port_Operation_Call_Connect_In) {
		this.Connection_Port_Operation_Call_Connect_In = Connection_Port_Operation_Call_Connect_In;
	}
	
	public boolean getConnection_Port_Operation_Call_Connect_In() {
		return Connection_Port_Operation_Call_Connect_In;
	}
	
	public void setStateMachine_Port_OrionDisconnCause_In(boolean StateMachine_Port_OrionDisconnCause_In) {
		this.StateMachine_Port_OrionDisconnCause_In = StateMachine_Port_OrionDisconnCause_In;
	}
	
	public boolean getStateMachine_Port_OrionDisconnCause_In() {
		return StateMachine_Port_OrionDisconnCause_In;
	}
	
	public void setConnection_Port_Operation_Call_Disconn_In(boolean Connection_Port_Operation_Call_Disconn_In) {
		this.Connection_Port_Operation_Call_Disconn_In = Connection_Port_Operation_Call_Disconn_In;
	}
	
	public boolean getConnection_Port_Operation_Call_Disconn_In() {
		return Connection_Port_Operation_Call_Disconn_In;
	}
	
	public void setMain_region_of_Orion_Slave_SM(Main_region_of_Orion_Slave_SM main_region_of_Orion_Slave_SM) {
		this.main_region_of_Orion_Slave_SM = main_region_of_Orion_Slave_SM;
	}
	
	public Main_region_of_Orion_Slave_SM getMain_region_of_Orion_Slave_SM() {
		return main_region_of_Orion_Slave_SM;
	}
	
	public void setRegion_2_in_Kapcsolodva_1(Region_2_in_Kapcsolodva_1 region_2_in_Kapcsolodva_1) {
		this.region_2_in_Kapcsolodva_1 = region_2_in_Kapcsolodva_1;
	}
	
	public Region_2_in_Kapcsolodva_1 getRegion_2_in_Kapcsolodva_1() {
		return region_2_in_Kapcsolodva_1;
	}
	
	public void setRegion_1_in_Kapcsolodva_1(Region_1_in_Kapcsolodva_1 region_1_in_Kapcsolodva_1) {
		this.region_1_in_Kapcsolodva_1 = region_1_in_Kapcsolodva_1;
	}
	
	public Region_1_in_Kapcsolodva_1 getRegion_1_in_Kapcsolodva_1() {
		return region_1_in_Kapcsolodva_1;
	}
	
	public void setTimeoutKeepAliveReceiveTimeout_4(long TimeoutKeepAliveReceiveTimeout_4) {
		this.TimeoutKeepAliveReceiveTimeout_4 = TimeoutKeepAliveReceiveTimeout_4;
	}
	
	public long getTimeoutKeepAliveReceiveTimeout_4() {
		return TimeoutKeepAliveReceiveTimeout_4;
	}
	
	public void setTimeoutKapcsolodik_3(long TimeoutKapcsolodik_3) {
		this.TimeoutKapcsolodik_3 = TimeoutKapcsolodik_3;
	}
	
	public long getTimeoutKapcsolodik_3() {
		return TimeoutKapcsolodik_3;
	}
	
	public void setTimeoutKeepAliveSendTimeout_0(long TimeoutKeepAliveSendTimeout_0) {
		this.TimeoutKeepAliveSendTimeout_0 = TimeoutKeepAliveSendTimeout_0;
	}
	
	public long getTimeoutKeepAliveSendTimeout_0() {
		return TimeoutKeepAliveSendTimeout_0;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _95260907 = (this.region_1_in_Kapcsolodva_1 == Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0);
		boolean _1856903344 = (this.region_2_in_Kapcsolodva_1 == Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4);
		boolean _1704971753 = (this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3);
		boolean _674165941 = (this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1);
		boolean _2136975967 = ((_674165941) && (_1856903344));
		boolean _241488121 = ((_674165941) && (_95260907));
		boolean _1393452320 = ((_674165941) && (_95260907));
		boolean _1156989782 = ((_674165941) && (_1856903344));
		boolean _1496198247 = (this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Zarva_2);
		boolean _33365200 = ((_674165941) && (_1856903344));
		boolean _guard_194199296 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_194199296) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_325328363 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_325328363) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_253713452 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_253713452) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_471727250 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_471727250) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_823611579 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_823611579) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1828154161 = ((((((_674165941) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)))) && (this.Connection_Port_Operation_Call_Disconn_In))));
		if (_guard_1828154161) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1694353832 = ((((((_1393452320) && ((((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) && ((this.region_1_in_Kapcsolodva_1 == Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0)))))) && (((4 * 1000) <= this.TimeoutKeepAliveSendTimeout_0)))) && (((!(((_674165941) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnConf_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_674165941) && (this.Connection_Port_Operation_Call_Disconn_In)))))));
		if (_guard_1694353832) {
			this.Send_StateMachine_Port_OrionKeepAlive_Out = true;
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0;
			this.TimeoutKeepAliveSendTimeout_0 = 0;
		}
		boolean _guard_334555844 = ((((((_241488121) && ((((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) && ((this.region_1_in_Kapcsolodva_1 == Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0)))))) && (this.Block_Port_Operation_Call_SendData_In))) && (((!(((_674165941) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnConf_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_674165941) && (this.Connection_Port_Operation_Call_Disconn_In)))))));
		if (_guard_334555844) {
			this.Send_StateMachine_Port_OrionAppData_Out = true;
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0;
			this.TimeoutKeepAliveSendTimeout_0 = 0;
		}
		boolean _guard_195756486 = ((((((_2136975967) && ((((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) && ((this.region_2_in_Kapcsolodva_1 == Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4)))))) && (((5 * 1000) <= this.TimeoutKeepAliveReceiveTimeout_4)))) && (((!(((_674165941) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnConf_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_674165941) && (this.Connection_Port_Operation_Call_Disconn_In)))))));
		if (_guard_195756486) {
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.__Inactive__;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.__Inactive__;
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1652525449 = ((((((_1156989782) && ((((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) && ((this.region_2_in_Kapcsolodva_1 == Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4)))))) && (this.StateMachine_Port_OrionAppData_In))) && (((!(((_674165941) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnConf_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_674165941) && (this.Connection_Port_Operation_Call_Disconn_In)))))));
		if (_guard_1652525449) {
			this.Process_StateMachine_Port_OrionAppData_Out = true;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4;
			this.TimeoutKeepAliveReceiveTimeout_4 = 0;
		}
		boolean _guard_23224440 = ((((((_33365200) && ((((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodva_1)) && ((this.region_2_in_Kapcsolodva_1 == Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4)))))) && (this.StateMachine_Port_OrionKeepAlive_In))) && (((!(((_674165941) && (this.Block_Port_Operation_Call_Invalid_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnResp_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnReq_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionConnConf_In)))) && (!(((_674165941) && (this.StateMachine_Port_OrionDisconnCause_In)))) && (!(((_674165941) && (this.Connection_Port_Operation_Call_Disconn_In)))))));
		if (_guard_23224440) {
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4;
			this.TimeoutKeepAliveReceiveTimeout_4 = 0;
		}
		boolean _guard_1497514891 = ((((((_1496198247) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Zarva_2)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_1497514891) {
			this.Send_StateMachine_Port_OrionConnResp_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Kapcsolodik_3;
			this.TimeoutKapcsolodik_3 = 0;
		}
		boolean _guard_501016576 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionConnConf_In))));
		if (_guard_501016576) {
			this.Handle_StateMachine_Port_OrionConnConf_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Kapcsolodva_1;
			this.region_1_in_Kapcsolodva_1 = Region_1_in_Kapcsolodva_1.KeepAliveSendTimeout_0;
			this.region_2_in_Kapcsolodva_1 = Region_2_in_Kapcsolodva_1.KeepAliveReceiveTimeout_4;
			this.TimeoutKeepAliveSendTimeout_0 = 0;
			this.TimeoutKeepAliveReceiveTimeout_4 = 0;
		}
		boolean _guard_1937456795 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (((5 * 1000) <= this.TimeoutKapcsolodik_3)))));
		if (_guard_1937456795) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1266447070 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionDisconnCause_In))));
		if (_guard_1266447070) {
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_994450898 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionConnResp_In))));
		if (_guard_994450898) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1170589721 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionConnReq_In))));
		if (_guard_1170589721) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1578753139 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionAppData_In))));
		if (_guard_1578753139) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_1119861531 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.Block_Port_Operation_Call_Invalid_In))));
		if (_guard_1119861531) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
		boolean _guard_519861750 = ((((((_1704971753) && ((this.main_region_of_Orion_Slave_SM == Main_region_of_Orion_Slave_SM.Kapcsolodik_3)))) && (this.StateMachine_Port_OrionKeepAlive_In))));
		if (_guard_519861750) {
			this.Send_StateMachine_Port_OrionDisconn_Out = true;
			this.main_region_of_Orion_Slave_SM = Main_region_of_Orion_Slave_SM.Zarva_2;
		}
	}
	
	private void clearOutEvents() {
		Handle_StateMachine_Port_OrionDisconnCause_Out = false;
		Handle_StateMachine_Port_OrionConnConf_Out = false;
		Send_StateMachine_Port_OrionConnConf_Out = false;
		Send_StateMachine_Port_OrionDisconn_Out = false;
		Handle_StateMachine_Port_OrionConnReq_Out = false;
		Handle_StateMachine_Port_OrionAppData_Out = false;
		Process_StateMachine_Port_OrionKeepAlive_Out = false;
		Handle_StateMachine_Port_OrionKeepAlive_Out = false;
		Send_StateMachine_Port_OrionConnResp_Out = false;
		Handle_StateMachine_Port_OrionConnResp_Out = false;
		Process_StateMachine_Port_OrionConnReq_Out = false;
		Handle_StateMachine_Port_OrionDisconn_Out = false;
		Send_StateMachine_Port_OrionDisconnCause_Out = false;
		Send_StateMachine_Port_OrionAppData_Out = false;
		Process_StateMachine_Port_OrionDisconnCause_Out = false;
		Send_StateMachine_Port_OrionKeepAlive_Out = false;
		Process_StateMachine_Port_OrionConnResp_Out = false;
		Process_StateMachine_Port_OrionConnConf_Out = false;
		Send_StateMachine_Port_OrionConnReq_Out = false;
		Process_StateMachine_Port_OrionAppData_Out = false;
		Process_StateMachine_Port_OrionDisconn_Out = false;
	}
	
	private void clearInEvents() {
		StateMachine_Port_OrionConnResp_In = false;
		StateMachine_Port_OrionConnConf_In = false;
		StateMachine_Port_OrionConnReq_In = false;
		StateMachine_Port_OrionDisconn_In = false;
		Block_Port_Operation_Call_Invalid_In = false;
		Block_Port_Operation_Call_SendData_In = false;
		StateMachine_Port_OrionAppData_In = false;
		StateMachine_Port_OrionKeepAlive_In = false;
		Connection_Port_Operation_Call_Connect_In = false;
		StateMachine_Port_OrionDisconnCause_In = false;
		Connection_Port_Operation_Call_Disconn_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Handle_StateMachine_Port_OrionDisconnCause_Out = " + Handle_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionConnConf_Out = " + Handle_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnConf_Out = " + Send_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconn_Out = " + Send_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionConnReq_Out = " + Handle_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionAppData_Out = " + Handle_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionKeepAlive_Out = " + Process_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionKeepAlive_Out = " + Handle_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnResp_Out = " + Send_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionConnResp_Out = " + Handle_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnReq_Out = " + Process_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Handle_StateMachine_Port_OrionDisconn_Out = " + Handle_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionDisconnCause_Out = " + Send_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionAppData_Out = " + Send_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconnCause_Out = " + Process_StateMachine_Port_OrionDisconnCause_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionKeepAlive_Out = " + Send_StateMachine_Port_OrionKeepAlive_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnResp_Out = " + Process_StateMachine_Port_OrionConnResp_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionConnConf_Out = " + Process_StateMachine_Port_OrionConnConf_Out + System.lineSeparator() +
			"Send_StateMachine_Port_OrionConnReq_Out = " + Send_StateMachine_Port_OrionConnReq_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionAppData_Out = " + Process_StateMachine_Port_OrionAppData_Out + System.lineSeparator() +
			"Process_StateMachine_Port_OrionDisconn_Out = " + Process_StateMachine_Port_OrionDisconn_Out + System.lineSeparator() +
			"StateMachine_Port_OrionConnResp_In = " + StateMachine_Port_OrionConnResp_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnConf_In = " + StateMachine_Port_OrionConnConf_In + System.lineSeparator() +
			"StateMachine_Port_OrionConnReq_In = " + StateMachine_Port_OrionConnReq_In + System.lineSeparator() +
			"StateMachine_Port_OrionDisconn_In = " + StateMachine_Port_OrionDisconn_In + System.lineSeparator() +
			"Block_Port_Operation_Call_Invalid_In = " + Block_Port_Operation_Call_Invalid_In + System.lineSeparator() +
			"Block_Port_Operation_Call_SendData_In = " + Block_Port_Operation_Call_SendData_In + System.lineSeparator() +
			"StateMachine_Port_OrionAppData_In = " + StateMachine_Port_OrionAppData_In + System.lineSeparator() +
			"StateMachine_Port_OrionKeepAlive_In = " + StateMachine_Port_OrionKeepAlive_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Connect_In = " + Connection_Port_Operation_Call_Connect_In + System.lineSeparator() +
			"StateMachine_Port_OrionDisconnCause_In = " + StateMachine_Port_OrionDisconnCause_In + System.lineSeparator() +
			"Connection_Port_Operation_Call_Disconn_In = " + Connection_Port_Operation_Call_Disconn_In + System.lineSeparator() +
			"main_region_of_Orion_Slave_SM = " + main_region_of_Orion_Slave_SM + System.lineSeparator() +
			"region_2_in_Kapcsolodva_1 = " + region_2_in_Kapcsolodva_1 + System.lineSeparator() +
			"region_1_in_Kapcsolodva_1 = " + region_1_in_Kapcsolodva_1 + System.lineSeparator() +
			"TimeoutKeepAliveReceiveTimeout_4 = " + TimeoutKeepAliveReceiveTimeout_4 + System.lineSeparator() +
			"TimeoutKapcsolodik_3 = " + TimeoutKapcsolodik_3 + System.lineSeparator() +
			"TimeoutKeepAliveSendTimeout_0 = " + TimeoutKeepAliveSendTimeout_0
		;
	}
	
}
