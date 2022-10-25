package hu.bme.mit.gamma.stochastic.casestudy.orion_meas;

public interface ITimer {
	
	void setTimer(ITimerCallback callback, int eventID, long time, boolean isPeriodic);
	void unsetTimer(ITimerCallback callback, int eventID);
	
}
