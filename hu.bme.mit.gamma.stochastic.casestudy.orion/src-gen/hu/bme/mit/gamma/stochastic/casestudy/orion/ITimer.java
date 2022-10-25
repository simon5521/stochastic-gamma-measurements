package hu.bme.mit.gamma.stochastic.casestudy.orion;

public interface ITimer {
	
	void setTimer(ITimerCallback callback, int eventID, long time, boolean isPeriodic);
	void unsetTimer(ITimerCallback callback, int eventID);
	
}
