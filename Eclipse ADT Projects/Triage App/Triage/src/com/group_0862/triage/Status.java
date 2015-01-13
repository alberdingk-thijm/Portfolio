package com.group_0862.triage;

import java.util.TreeMap;
import java.util.Calendar;

/**
 * This class is a superclass of all the Patient's vital signs and symptoms
 */
public class Status<K,V> extends TreeMap<Calendar,String>{

	
	private static final long serialVersionUID = 2781552712783933011L;
	
	/**
	 * constructor
	 */
	public Status() {
		super();
	}

	/**
	 * Return the most recently-added item in the Status object.
	 * @return String if the Status object 
	 */
	public String getLatest() {
		if (this.size() != 0) {
			return super.lastEntry().getValue();
		} else {
			return null;
		}
	}
		
	/**
	 * Adds a <Calendar,String> key-value pair to the Status object.
	 * and call updateUrgency() to change the value of patient's urgency
	 * @param changedValue
	 */
	public void update(String changedValue, Patient patient) {
		super.put(Calendar.getInstance(), changedValue);
		patient.setUrgency();  // TODO: check that this works
	}
	
	/**
	 * Return a string representation of the Status object. Rather than spitting out a hard-to-read Calendar, it uses the
	 * getTimeInMillis() method to create an equivalent key (that can be reinterpreted with strToMap).
	 * @return string representation of this
	 */
    public String toString() {
    	TreeMap<Long,String> long_map = new TreeMap<Long,String>();
    	for (Calendar key: this.keySet()) {
    		long_map.put(key.getTimeInMillis(), this.get(key));
    	}
    	return long_map.toString();
	}
    /**
	 * 	This method returns a Map given its String representation, str. 
	 * @param str 
	 * @return a Map that is represented by str
	 */
	public static TreeMap<Calendar, String> strToMap(String str){
		TreeMap<Calendar, String> result = new TreeMap<Calendar, String>();
		
		if (str.length() > 2) {
			String[] temporary = str.substring(1, str.length() - 1).split(VisitRecord.VALUE_SEP); //split each key-value pair

			for (String keyValPair : temporary){
				String[] temporary1 = keyValPair.split("=");
				Calendar time = Calendar.getInstance();
				time.setTimeInMillis(Long.parseLong(temporary1[0]));  // setting the time from the long made from the string
				String data = temporary1[1];
				result.put(time, data);
			}
		}
		
		return result;
	}	
	
	/**
	 * This method put the key-map data in Map returned by Status.strToMap(str) 
	 * and import it to this.
	 * @param str
	 */
	public void putAll(String str){
		for (Calendar key : Status.strToMap(str).keySet()){
			this.put(key, Status.strToMap(str).get(key));
		}
		
	}
}
