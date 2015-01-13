package com.group_0862.triage;

import java.sql.Timestamp;

/**
 * A superclass of HeartRate, Temperature, and BloodPressure
 */
public abstract class VitalSigns extends Status<Timestamp, String> {

	
	private static final long serialVersionUID = -5794652048445000216L;
	
	public abstract Integer getUrgency(); 
		
}
