package com.group_0862.triage;

/**
 * This class represents BloodPressure
 */
public class BloodPressure extends VitalSigns {

	
	private static final long serialVersionUID = -415973220358917589L;
	
	/**
	 * Constructor
	 */
	public BloodPressure() {
		super();
	}

	public Integer getUrgency() {
		String[] split_bp = this.getLatest().split("/");
		if ((Integer.parseInt(split_bp[0]) >= 140) || (Integer.parseInt(split_bp[1]) >= 90)) {
			return 1;
		} else return 0;
	}

}
