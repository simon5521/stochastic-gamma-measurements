package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.List;

public interface ConnectionStateInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedDisconn();
		public boolean isRaisedConn();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseDisconn();
			void raiseConn();
		}
		
		interface Required  {
		}
		
	}
}
