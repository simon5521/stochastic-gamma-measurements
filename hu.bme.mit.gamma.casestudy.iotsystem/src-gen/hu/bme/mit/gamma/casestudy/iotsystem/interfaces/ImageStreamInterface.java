package hu.bme.mit.gamma.casestudy.iotsystem.interfaces;

import hu.bme.mit.gamma.casestudy.iotsystem.*;
import java.util.List;

public interface ImageStreamInterface {
	
	interface Provided extends Listener.Required {
		
		public boolean isRaisedNewData();
		public double getBlurred();
		public boolean getCar();
		
		void registerListener(Listener.Provided listener);
		List<Listener.Provided> getRegisteredListeners();
	}
	
	interface Required extends Listener.Provided {
		
		
		void registerListener(Listener.Required listener);
		List<Listener.Required> getRegisteredListeners();
	}
	
	interface Listener {
		
		interface Provided  {
			void raiseNewData(double blurred, boolean car);
		}
		
		interface Required  {
		}
		
	}
}
