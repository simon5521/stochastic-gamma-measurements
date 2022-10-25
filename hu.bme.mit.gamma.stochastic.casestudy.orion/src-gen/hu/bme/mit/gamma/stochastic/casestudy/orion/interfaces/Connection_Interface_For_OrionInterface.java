package hu.bme.mit.gamma.stochastic.casestudy.orion.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion.*;
import java.util.List;

public interface Connection_Interface_For_OrionInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedOperation_Call_Disconn();
		public boolean isRaisedOperation_Call_Connect();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseOperation_Call_Disconn();
			void raiseOperation_Call_Connect();
		}
		
		interface Required  {
		}
		
	}
}
