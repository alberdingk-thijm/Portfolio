package com.group_0862.triage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

public class Birthdate extends GregorianCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3622604733157253754L;
	final Integer year = this.get(Calendar.YEAR);
	final Integer month = this.get(Calendar.MONTH);
	final Integer day = this.get(Calendar.DAY_OF_MONTH);
	Locale locale = Locale.CANADA;
	
	public Birthdate() {
		super();
	}
	
	@Override
	public String toString() {
		//changed format from '/' to '-' since the original patient_records.txt uses "-"
		String result = String.format(locale,"%02d-%02d-%02d", year, month, day);
		return result;
		
	}
	
	public static Birthdate strToBirthdate(String s) {
		Birthdate bdate = new Birthdate();
		if (s.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
			String[] vals = s.split("-");
			bdate.set(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));
		} else {
			Log.w("Birthdate","Set to current time: birthdate was incorrectly formatted.");
		}
		return bdate;
	}

}
