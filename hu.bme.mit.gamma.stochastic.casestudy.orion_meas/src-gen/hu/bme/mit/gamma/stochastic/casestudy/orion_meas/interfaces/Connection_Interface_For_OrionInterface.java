package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.List;

public interface Connection_Interface_For_OrionInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedOperation_Call_Connect();
		public boolean isRaisedOperation_Call_Disconn();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseOperation_Call_Connect();
			void raiseOperation_Call_Disconn();
		}
		
		interface Required  {
		}
		
	}
}
