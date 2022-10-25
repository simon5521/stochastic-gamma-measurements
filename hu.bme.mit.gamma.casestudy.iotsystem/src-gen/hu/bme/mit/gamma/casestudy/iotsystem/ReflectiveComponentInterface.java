package hu.bme.mit.gamma.casestudy.iotsystem;

import java.util.Objects;

public interface ReflectiveComponentInterface {
	
	void reset();
			
	String[] getPorts();
			
	String[] getEvents(String port);
			
	void raiseEvent(String port, String event, Object[] parameters);
			
	boolean isRaisedEvent(String port, String event, Object[] parameters);
	
	void schedule(String instance);
	
	boolean isStateActive(String region, String state);
	
	String[] getRegions();
	
	String[] getStates(String region);
	
	String[] getVariables();
	
	Object getValue(String variable);
	
	default boolean checkVariableValue(String variable, Object expectedValue) {
		return Objects.deepEquals(getValue(variable), expectedValue);
	}
	
	String[] getComponents();
	
	ReflectiveComponentInterface getComponent(String component);
	
}
