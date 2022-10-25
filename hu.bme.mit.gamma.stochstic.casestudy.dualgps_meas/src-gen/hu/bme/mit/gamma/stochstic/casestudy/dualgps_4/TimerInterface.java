package hu.bme.mit.gamma.stochstic.casestudy.dualgps_4;

public interface TimerInterface {
	
	public void saveTime(Object object);
	public long getElapsedTime(Object object, TimeUnit timeUnit);
	
	public enum TimeUnit {
		SECOND, MILLISECOND, MICROSECOND, NANOSECOND
	}
	
}
