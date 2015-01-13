package com.group_0862.triage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

public class ArrivalTime extends GregorianCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5103276063579602502L;
	final Integer year = this.get(Calendar.YEAR);
	final Integer month = this.get(Calendar.MONTH);
	final Integer day = this.get(Calendar.DAY_OF_MONTH);
	final Integer hour = this.get(Calendar.HOUR_OF_DAY);
	final Integer minute = this.get(Calendar.MINUTE);
	final Integer second = this.get(Calendar.SECOND);
	Locale locale = Locale.CANADA;
	
	public ArrivalTime() {
		super();
	}
	
	@Override
	public String toString() {
		//changed '/' to '-'
		String result = String.format(locale, "%02d:%02d:%02d on %04d-%02d-%02d", hour, minute, second, year, month, day);
		return result;
		
	}
	
	public static ArrivalTime strToArrivalTime(String s) {
		ArrivalTime aTime = new ArrivalTime();
		if (s.matches("[0-9]{2}:[0-9]{2}:[0-9]{2} on [0-9]{4}-[0-9]{2}-[0-9]{2}")) {
		String[] vals = s.split("[^0-9no]");
		aTime.clear();
		aTime.set(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]),
				Integer.parseInt(vals[4]), Integer.parseInt(vals[5]), Integer.parseInt(vals[6]));	
		} else {
			Log.w("ArrivalTime","Set to current time: arrival time was incorrectly formatted.");
		}
		return aTime;
	}
	
	/**
	 * Return true if this and other have the same year, month, day, hour, minute and second values.
	 * @return
	 */
	public boolean equals(ArrivalTime other) {
		return (this.year == other.year) && (this.month == other.month) && (this.day == other.day) && 
				(this.hour == other.hour) && (this.minute == other.minute) && (this.second == other.second);
	}

}
