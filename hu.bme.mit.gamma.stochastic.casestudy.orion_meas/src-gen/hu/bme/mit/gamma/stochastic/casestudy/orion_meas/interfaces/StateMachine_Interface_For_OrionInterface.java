package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.List;

public interface StateMachine_Interface_For_OrionInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedOrionAppData();
		public boolean isRaisedOrionKeepAlive();
		public boolean isRaisedOrionDisconn();
		public boolean isRaisedOrionConnConf();
		public boolean isRaisedOrionDisconnCause();
		public boolean isRaisedOrionConnResp();
		public boolean isRaisedOrionConnReq();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseOrionAppData();
			void raiseOrionKeepAlive();
			void raiseOrionDisconn();
			void raiseOrionConnConf();
			void raiseOrionDisconnCause();
			void raiseOrionConnResp();
			void raiseOrionConnReq();
		}
		
		interface Required  {
		}
		
	}
}
