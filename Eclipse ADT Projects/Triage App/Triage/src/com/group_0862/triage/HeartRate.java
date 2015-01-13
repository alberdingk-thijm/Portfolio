package com.group_0862.triage;

/**
 * This class represents HeartRate
 */
public class HeartRate extends VitalSigns {

	
	private static final long serialVersionUID = 2269341569715766549L;
	
	/**
	 * Constructor
	 */
	public HeartRate() {
		super();
	}

	public Integer getUrgency() {
		if ((Integer.parseInt(this.getLatest()) >= 100) || Integer.parseInt(this.getLatest()) <= 50) {
			return 1;
		} else return 0;
	}

}
