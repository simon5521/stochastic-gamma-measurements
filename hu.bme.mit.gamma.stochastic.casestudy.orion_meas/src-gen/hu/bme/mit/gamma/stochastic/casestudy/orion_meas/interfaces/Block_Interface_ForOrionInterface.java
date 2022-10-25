package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.List;

public interface Block_Interface_ForOrionInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedOperation_Call_SendData();
		public boolean isRaisedOperation_Call_Invalid();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseOperation_Call_SendData();
			void raiseOperation_Call_Invalid();
		}
		
		interface Required  {
		}
		
	}
}
