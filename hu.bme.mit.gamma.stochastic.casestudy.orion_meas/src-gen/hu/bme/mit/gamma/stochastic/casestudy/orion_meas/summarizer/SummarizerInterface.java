package hu.bme.mit.gamma.stochastic.casestudy.orion_meas.summarizer;

import hu.bme.mit.gamma.stochastic.casestudy.orion_meas.interfaces.*;

public interface SummarizerInterface {

	public ConnectionStateInterface.Required getInPort();
	public ConnectionStateInterface.Provided getOutPort();
	
	void runCycle();
	void reset();

}
