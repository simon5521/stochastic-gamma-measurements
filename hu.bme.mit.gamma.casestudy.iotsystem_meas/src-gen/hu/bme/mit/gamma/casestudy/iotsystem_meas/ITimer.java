package hu.bme.mit.gamma.casestudy.iotsystem_meas;

public interface ITimer {
	
	void setTimer(ITimerCallback callback, int eventID, long time, boolean isPeriodic);
	void unsetTimer(ITimerCallback callback, int eventID);
	
}
