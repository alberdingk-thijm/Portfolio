package com.group_0862.triage;

import java.sql.Timestamp;

/**
 * This is a class containing time attended as Keys and physician's name as values.
 */
public class Attended extends Status<Timestamp, String>{
private static final long serialVersionUID = -415973220358917589L;
	
	/**
	 * Constructor
	 */
	public Attended() {
		super();
	}
}
