package hu.bme.mit.gamma.stochstic.casestudy.dualgps.interfaces;

import hu.bme.mit.gamma.stochstic.casestudy.dualgps.*;
import java.util.List;

public interface SensorInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedFailstop();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseFailstop();
		}
		
		interface Required  {
		}
		
	}
}
