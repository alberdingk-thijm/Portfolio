package com.group_0862.triage;

public enum PMenu {
	
	PRESCRIBE ("Fill out Prescription",PrescriptionActivity.class),
	LOOKUP ("Look up a Patient", LookUpActivity.class);
	
	private final String s;
	private final Class<?> c;
	private PMenu(String s, Class<?> c) {
		this.s = s;
		this.c = c;
	}
	
	public Class<?> getC(){
		return c;
	}

	public String getS() {
		return s;
	}

}
