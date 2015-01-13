package com.group_0862.triage;

public class Temperature extends VitalSigns {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5885051758171994061L;
    //private static final Integer base_temp = 39;
	
	public Temperature() {
	    super();
	    //this.base = base_temp;
	}
	
	public Integer getUrgency() {
		
		if (Double.parseDouble(this.getLatest()) <= 39.0 ) {
			return 1;
		} return 0;

	}
	
}