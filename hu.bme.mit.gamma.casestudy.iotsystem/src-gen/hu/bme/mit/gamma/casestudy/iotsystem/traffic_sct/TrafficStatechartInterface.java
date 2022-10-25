package hu.bme.mit.gamma.casestudy.iotsystem.traffic_sct;

import hu.bme.mit.gamma.casestudy.iotsystem.interfaces.*;

public interface TrafficStatechartInterface {

	public EventStreamInterface.Required getCarArrives();
	public EventStreamInterface.Required getCarLeaves();
	public EventStreamInterface.Provided getCarArrivesOut();
	public TrafficEventStreamInterface.Provided getTrafficStream();
	
	void runCycle();
	void reset();

}
