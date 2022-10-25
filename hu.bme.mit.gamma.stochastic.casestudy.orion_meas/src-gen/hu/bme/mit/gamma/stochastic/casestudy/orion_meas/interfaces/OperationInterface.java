package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import java.util.List;

public interface OperationInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedFail();
		public boolean isRaisedRecover();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseFail();
			void raiseRecover();
		}
		
		interface Required  {
		}
		
	}
}
