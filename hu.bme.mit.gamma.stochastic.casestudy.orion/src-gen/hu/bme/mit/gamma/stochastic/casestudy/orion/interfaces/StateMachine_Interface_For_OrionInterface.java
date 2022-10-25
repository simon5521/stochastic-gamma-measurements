package hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import java.util.List;

public interface StateMachine_Interface_For_OrionInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedOrionKeepAlive();
		public boolean isRaisedOrionConnConf();
		public boolean isRaisedOrionConnResp();
		public boolean isRaisedOrionConnReq();
		public boolean isRaisedOrionAppData();
		public boolean isRaisedOrionDisconn();
		public boolean isRaisedOrionDisconnCause();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseOrionKeepAlive();
			void raiseOrionConnConf();
			void raiseOrionConnResp();
			void raiseOrionConnReq();
			void raiseOrionAppData();
			void raiseOrionDisconn();
			void raiseOrionDisconnCause();
		}
		
		interface Required  {
		}
		
	}
}
