package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.interfaces;

import java.util.List;
import hu.bme.mit.gamma.stochstic.casestudy.dualgps_4.*;

public interface SensorInterface {

	interface Provided extends Listener.Required {
		
		boolean isRaisedFailstop();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
	interface Provided {
		void raiseFailstop();
		}
		
	interface Required {
		}
		
	}

}
