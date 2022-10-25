package hu.bme.mit.gamma.stochastic.casestudy.orion_meas;

public interface TimerInterface {
	
	public void saveTime(Object object);
	public long getElapsedTime(Object object, TimeUnit timeUnit);
	
	public enum TimeUnit {
		SECOND, MILLISECOND, MICROSECOND, NANOSECOND
	}
	
}
