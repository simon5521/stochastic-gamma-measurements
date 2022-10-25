package hu.bme.mit.gamma.stochastic.casestudy.orion.channel;

import hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces.*; 		

public class ChannelStatechartStatemachine {
	
	enum OrionDisconnRegion {__Inactive__, IdleOrionDisconn}
	private boolean Input_OrionDisconn_In;
	private boolean Input_OrionConnConf_In;
	private boolean Input_OrionKeepAlive_In;
	private boolean Input_OrionConnResp_In;
	private boolean Input_OrionDisconnCause_In;
	private boolean Input_OrionConnReq_In;
	private boolean Input_OrionAppData_In;
	private boolean Output_OrionDisconnCause_Out;
	private boolean Output_OrionDisconn_Out;
	private boolean Output_OrionKeepAlive_Out;
	private boolean Output_OrionConnReq_Out;
	private boolean Output_OrionAppData_Out;
	private boolean Output_OrionConnResp_Out;
	private boolean Output_OrionConnConf_Out;
	private OrionDisconnRegion orionDisconnRegion;
	
	public ChannelStatechartStatemachine() {
	}
	
	public void reset() {
		this.orionDisconnRegion = OrionDisconnRegion.__Inactive__;
		clearOutEvents();
		clearInEvents();
		this.orionDisconnRegion = OrionDisconnRegion.__Inactive__;
		this.Input_OrionDisconn_In = false;
		this.Input_OrionConnConf_In = false;
		this.Input_OrionKeepAlive_In = false;
		this.Input_OrionConnResp_In = false;
		this.Input_OrionDisconnCause_In = false;
		this.Input_OrionConnReq_In = false;
		this.Input_OrionAppData_In = false;
		this.Output_OrionDisconnCause_Out = false;
		this.Output_OrionDisconn_Out = false;
		this.Output_OrionKeepAlive_Out = false;
		this.Output_OrionConnReq_Out = false;
		this.Output_OrionAppData_Out = false;
		this.Output_OrionConnResp_Out = false;
		this.Output_OrionConnConf_Out = false;
		this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
	}
	
	public void setInput_OrionDisconn_In(boolean Input_OrionDisconn_In) {
		this.Input_OrionDisconn_In = Input_OrionDisconn_In;
	}
	
	public boolean getInput_OrionDisconn_In() {
		return Input_OrionDisconn_In;
	}
	
	public void setInput_OrionConnConf_In(boolean Input_OrionConnConf_In) {
		this.Input_OrionConnConf_In = Input_OrionConnConf_In;
	}
	
	public boolean getInput_OrionConnConf_In() {
		return Input_OrionConnConf_In;
	}
	
	public void setInput_OrionKeepAlive_In(boolean Input_OrionKeepAlive_In) {
		this.Input_OrionKeepAlive_In = Input_OrionKeepAlive_In;
	}
	
	public boolean getInput_OrionKeepAlive_In() {
		return Input_OrionKeepAlive_In;
	}
	
	public void setInput_OrionConnResp_In(boolean Input_OrionConnResp_In) {
		this.Input_OrionConnResp_In = Input_OrionConnResp_In;
	}
	
	public boolean getInput_OrionConnResp_In() {
		return Input_OrionConnResp_In;
	}
	
	public void setInput_OrionDisconnCause_In(boolean Input_OrionDisconnCause_In) {
		this.Input_OrionDisconnCause_In = Input_OrionDisconnCause_In;
	}
	
	public boolean getInput_OrionDisconnCause_In() {
		return Input_OrionDisconnCause_In;
	}
	
	public void setInput_OrionConnReq_In(boolean Input_OrionConnReq_In) {
		this.Input_OrionConnReq_In = Input_OrionConnReq_In;
	}
	
	public boolean getInput_OrionConnReq_In() {
		return Input_OrionConnReq_In;
	}
	
	public void setInput_OrionAppData_In(boolean Input_OrionAppData_In) {
		this.Input_OrionAppData_In = Input_OrionAppData_In;
	}
	
	public boolean getInput_OrionAppData_In() {
		return Input_OrionAppData_In;
	}
	
	public void setOutput_OrionDisconnCause_Out(boolean Output_OrionDisconnCause_Out) {
		this.Output_OrionDisconnCause_Out = Output_OrionDisconnCause_Out;
	}
	
	public boolean getOutput_OrionDisconnCause_Out() {
		return Output_OrionDisconnCause_Out;
	}
	
	public void setOutput_OrionDisconn_Out(boolean Output_OrionDisconn_Out) {
		this.Output_OrionDisconn_Out = Output_OrionDisconn_Out;
	}
	
	public boolean getOutput_OrionDisconn_Out() {
		return Output_OrionDisconn_Out;
	}
	
	public void setOutput_OrionKeepAlive_Out(boolean Output_OrionKeepAlive_Out) {
		this.Output_OrionKeepAlive_Out = Output_OrionKeepAlive_Out;
	}
	
	public boolean getOutput_OrionKeepAlive_Out() {
		return Output_OrionKeepAlive_Out;
	}
	
	public void setOutput_OrionConnReq_Out(boolean Output_OrionConnReq_Out) {
		this.Output_OrionConnReq_Out = Output_OrionConnReq_Out;
	}
	
	public boolean getOutput_OrionConnReq_Out() {
		return Output_OrionConnReq_Out;
	}
	
	public void setOutput_OrionAppData_Out(boolean Output_OrionAppData_Out) {
		this.Output_OrionAppData_Out = Output_OrionAppData_Out;
	}
	
	public boolean getOutput_OrionAppData_Out() {
		return Output_OrionAppData_Out;
	}
	
	public void setOutput_OrionConnResp_Out(boolean Output_OrionConnResp_Out) {
		this.Output_OrionConnResp_Out = Output_OrionConnResp_Out;
	}
	
	public boolean getOutput_OrionConnResp_Out() {
		return Output_OrionConnResp_Out;
	}
	
	public void setOutput_OrionConnConf_Out(boolean Output_OrionConnConf_Out) {
		this.Output_OrionConnConf_Out = Output_OrionConnConf_Out;
	}
	
	public boolean getOutput_OrionConnConf_Out() {
		return Output_OrionConnConf_Out;
	}
	
	public void setOrionDisconnRegion(OrionDisconnRegion orionDisconnRegion) {
		this.orionDisconnRegion = orionDisconnRegion;
	}
	
	public OrionDisconnRegion getOrionDisconnRegion() {
		return orionDisconnRegion;
	}
	
	public void runCycle() {
		clearOutEvents();
		changeState();
		clearInEvents();
	}

	private void changeState() {
		boolean _194921824 = (this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn);
		boolean _guard_459308212 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionDisconn_In))));
		if (_guard_459308212) {
			this.Output_OrionDisconn_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_382357503 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionDisconnCause_In))));
		if (_guard_382357503) {
			this.Output_OrionDisconnCause_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_1540764528 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionConnReq_In))));
		if (_guard_1540764528) {
			this.Output_OrionConnReq_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_731383585 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionAppData_In))));
		if (_guard_731383585) {
			this.Output_OrionAppData_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_717940715 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionKeepAlive_In))));
		if (_guard_717940715) {
			this.Output_OrionKeepAlive_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_769544721 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionConnConf_In))));
		if (_guard_769544721) {
			this.Output_OrionConnConf_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
		boolean _guard_1037965369 = ((((((_194921824) && ((this.orionDisconnRegion == OrionDisconnRegion.IdleOrionDisconn)))) && (this.Input_OrionConnResp_In))));
		if (_guard_1037965369) {
			this.Output_OrionConnResp_Out = true;
			this.orionDisconnRegion = OrionDisconnRegion.IdleOrionDisconn;
		}
	}
	
	private void clearOutEvents() {
		Output_OrionDisconnCause_Out = false;
		Output_OrionDisconn_Out = false;
		Output_OrionKeepAlive_Out = false;
		Output_OrionConnReq_Out = false;
		Output_OrionAppData_Out = false;
		Output_OrionConnResp_Out = false;
		Output_OrionConnConf_Out = false;
	}
	
	private void clearInEvents() {
		Input_OrionDisconn_In = false;
		Input_OrionConnConf_In = false;
		Input_OrionKeepAlive_In = false;
		Input_OrionConnResp_In = false;
		Input_OrionDisconnCause_In = false;
		Input_OrionConnReq_In = false;
		Input_OrionAppData_In = false;
	}
	
	@Override
	public String toString() {
		return
			"Input_OrionDisconn_In = " + Input_OrionDisconn_In + System.lineSeparator() +
			"Input_OrionConnConf_In = " + Input_OrionConnConf_In + System.lineSeparator() +
			"Input_OrionKeepAlive_In = " + Input_OrionKeepAlive_In + System.lineSeparator() +
			"Input_OrionConnResp_In = " + Input_OrionConnResp_In + System.lineSeparator() +
			"Input_OrionDisconnCause_In = " + Input_OrionDisconnCause_In + System.lineSeparator() +
			"Input_OrionConnReq_In = " + Input_OrionConnReq_In + System.lineSeparator() +
			"Input_OrionAppData_In = " + Input_OrionAppData_In + System.lineSeparator() +
			"Output_OrionDisconnCause_Out = " + Output_OrionDisconnCause_Out + System.lineSeparator() +
			"Output_OrionDisconn_Out = " + Output_OrionDisconn_Out + System.lineSeparator() +
			"Output_OrionKeepAlive_Out = " + Output_OrionKeepAlive_Out + System.lineSeparator() +
			"Output_OrionConnReq_Out = " + Output_OrionConnReq_Out + System.lineSeparator() +
			"Output_OrionAppData_Out = " + Output_OrionAppData_Out + System.lineSeparator() +
			"Output_OrionConnResp_Out = " + Output_OrionConnResp_Out + System.lineSeparator() +
			"Output_OrionConnConf_Out = " + Output_OrionConnConf_Out + System.lineSeparator() +
			"orionDisconnRegion = " + orionDisconnRegion
		;
	}
	
}
