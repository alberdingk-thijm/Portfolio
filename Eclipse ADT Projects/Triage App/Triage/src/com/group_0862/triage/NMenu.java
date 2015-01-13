package com.group_0862.triage;

public enum NMenu {
	
	LOOKUP ("Look up a Patient", LookUpActivity.class),
	NEW ("Create New Visit", NewVisitActivity.class),
	LIST ("View Active Patients", PatientListActivity.class),
	URGENCY ("Sort by Urgency",SortUrgencyActivity.class),
	SAVE ("Save Changes");
	
	private final Class<?> c;
	private final String s;
	private NMenu(String s, Class<?> c) {
		this.c = c;
		this.s = s;
	}
	
	private NMenu(String s) {
		this.c = NMenu.class;
		this.s = s;
	}
	
	public Class<?> getC(){
		return c;
	}

	public String getS() {
		return s;
	}

}
