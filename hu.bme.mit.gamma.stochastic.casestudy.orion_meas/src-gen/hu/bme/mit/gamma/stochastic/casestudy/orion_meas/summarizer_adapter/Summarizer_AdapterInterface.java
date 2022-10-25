package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer_adapter;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.*;
import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.ConnectionStateInterface;

public interface Summarizer_AdapterInterface {
	
	ConnectionStateInterface.Required getInPort();
	ConnectionStateInterface.Provided getOutPort();
	
	void reset();
	
	void start();
	
}
