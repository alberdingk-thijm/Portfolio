package com.group_0862.triage;

public enum PatientAttribute {
	ARRIVAL (ArrivalTime.class, "arrival", false),
	HEALTHNUM (Integer.class, "healthNum", false),
	NAME (Name.class, "name", false),
	BIRTHDATE (Birthdate.class, "birthdate", false),
	ATTENDED (Attended.class, "attended", true),
	TEMPERATURE (Temperature.class, "temp", true),
	BLOODPRESSURE (BloodPressure.class, "bp", true),
	HEARTRATE (HeartRate.class, "hr", true),
	URGENCY (Integer.class, "urgency", true),
	DISCHARGED (Boolean.class, "discharged", true),
	PRESCRIPTION (Prescription.class, "prescription", true);
	
	private final Class<?> c;
	private final String name;
	private final boolean update;
	private PatientAttribute(Class<?> c, String name, boolean update) {
		this.c = c;
		this.name = name;
		this.update = update;
	}
	public Class<?> getC() {
		return c;
	}
	public String getName() {
		return name;
	}
	public boolean canUpdate() {
		return update;
	}

}
