package com.group_0862.triage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
//import java.util.Date;

/**
 * This is a Patient class which contains all information
 * of a Patient within 1 visit, including a reference to their
 * previous visits.
 */
public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer healthNum;
	private Name name;
	private Birthdate birthdate;
	private Attended attended;
	private Boolean discharged;
	private ArrivalTime arrivalTime;
	private VisitRecord history;
	private Temperature temp;
	private BloodPressure bp;
	private HeartRate hr;
	private Prescription prescription;
	@SuppressWarnings("unused")
	private Integer urgency; 
	
	/**
	 * Construct a new Patient object.
	 * @param arrivalTime, healthNum, name, birthdate
	 * @throws IOException 
	 * @throws ParseException 
	 */
	
	public Patient(ArrivalTime arrivalTime, Integer healthNum) throws IOException, ParseException {
		Program program = Program.getInstance();
		this.arrivalTime = arrivalTime;
		this.healthNum = healthNum;
		if (program.getDatabase().containsKey(healthNum)) this.history = program.getDatabase().get(healthNum);
		if (this.history != null) {
			this.name = this.history.getName();
			this.setBirthdate(this.history.getBirthdate()); //so that it will call updateUrgency
		}
				
		this.attended = new Attended();
		this.discharged = false;
		//Vital Signs
		this.temp = new Temperature();
		this.bp = new BloodPressure();
		this.hr = new HeartRate();
		this.setUrgency();
		this.prescription = new Prescription();
	}
	// For a new visit
	/**
	 * @param arrivalTime
	 */
	public Patient(ArrivalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
		
		this.attended = new Attended();
		this.discharged = false;
		//Vital Signs
		this.temp = new Temperature();
		this.bp = new BloodPressure();
		this.hr = new HeartRate();
		this.urgency = 0;
		this.prescription = new Prescription();
	}
	
	/**
	 * This method saves the updated information to the patient's history (VisitRecord)
	 * and from history to the system (generate/write to files) in the format of 
	 * this.history.writeToFile() which is the VisitRecord writeToFile()
	 * @param data, directoryFolder
	 * @throws Exception 
	 */
	public void save(File directoryFolder) throws Exception {
		if (this.healthNum != null) {
			// save to history
			history.updateAll2(this);
			
			File saveFile = new File(directoryFolder, this.healthNum.toString() + ".txt");
			try {
				FileOutputStream outputStream = new FileOutputStream(saveFile);
				// if file doesn't exist, create it
				if (!saveFile.exists()) {
					saveFile.createNewFile();
				}
				// write to this file
				outputStream.write(this.history.writeToFile2().getBytes());
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!this.getAttended().isEmpty()) {
				Program p = Program.getInstance();
				p.getDatabase().get(this.healthNum).updateAll2(this);
				p.getActivePatientList().remove(this);
			}
		} else {
			Exception e = new Exception();
			throw e;
		}
	}
	
	/*
	 * need getters for the Activity, specifically PutInfoActivity.java	
	 */
	
	/**
	 * Call the getter for the referenced class of the PatientAttributes enum and return the appropriate value.
	 * @param c
	 * @return
	 */
	public Object getFromEnum(PatientAttribute pa) {
		Object o = null;
		pa.getC().cast(o); // type-casts o
		switch(pa) {
		case ARRIVAL:
			o = this.getArrivalTime();
			break;
		case ATTENDED:
			o = this.getAttended();
			break;
		case BIRTHDATE:
			o = this.getBirthdate();
			break;
		case BLOODPRESSURE:
			o = this.getBp();
			break;
		case DISCHARGED:
			o = this.getDischarged();
			break;
		case HEALTHNUM:
			o = this.getHealthNum();
			break;
		case HEARTRATE:
			o = this.getHr();
			break;
		case NAME:
			o = this.getName();
			break;
		case PRESCRIPTION:
			o = this.getPrescription();
			break;
		case TEMPERATURE:
			o = this.getTemp();
			break;
		case URGENCY:
			o = this.getUrgency();
			break;
		default:
			o = null;
			break;
		}
		return o;
	}
	
	/**
	 * @return temp
	 */
	public Temperature getTemp() {
		return temp;
	}
	
	/**
	 * @return bp
	 */
	public BloodPressure getBp() {
		return bp;
	}
	
	/**
	 * @return hr
	 */
	public HeartRate getHr() {
		return hr;
	}
	
	/** 
	 * @return this Patient's history
	 */
	public VisitRecord getHistory() {
		return history;
	}

	/**
	 * @return this Patient's arrivalTime
	 */
	public ArrivalTime getArrivalTime() {
		return this.arrivalTime;
	}
	
	/**
	 * @return healthNum
	 */
	public Integer getHealthNum() {
		return this.healthNum;
	}
	
	/**
	 * Return the age of the patient. For use within the getUrgency method.
	 * @return
	 */
	private Integer getAge() {
		int birthYear = this.birthdate.get(Calendar.YEAR);
		int birthMonth = this.birthdate.get(Calendar.MONTH);
		int birthDay = this.birthdate.get(Calendar.DAY_OF_MONTH);
		int currentYear = this.arrivalTime.get(Calendar.YEAR);
		int currentMonth = this.arrivalTime.get(Calendar.MONTH);
		int currentDay = this.arrivalTime.get(Calendar.DAY_OF_MONTH);
		int age = currentYear - birthYear;
		if (birthMonth > currentMonth) {
		  age--;
		} else if (birthMonth == currentMonth) {
		  if (birthDay > currentDay) {
		    age--;
		  }
		}
		return age;
	}
	
	public Integer getUrgency() {
		
		Integer urgencyVal = 0;
		
		if (this.birthdate != null) {
			if (getAge() < 2) urgencyVal += 1;
		}
		if (this.hr != null && !this.hr.isEmpty()) urgencyVal += this.hr.getUrgency();
		if (this.temp != null && !this.temp.isEmpty()) urgencyVal += this.temp.getUrgency();
		if (this.bp != null && !this.bp.isEmpty()) urgencyVal += this.bp.getUrgency();
		
		return urgencyVal;
	}
	
	public void setUrgency() {
		this.urgency = this.getUrgency();
	}
	
	/**
	 * @param name
	 */
	public void setName(Name name) {
		this.name = name;
	}
	
	/**
	 * @return name
	 */
	public Name getName() {
		return name;
	}
	
	/**
	 * @param birthdate
	 */
	public void setBirthdate(Birthdate birthdate) {
		this.birthdate = birthdate;
		this.setUrgency();
	}
	
	/**
	 * @return birthdate
	 */
	public Birthdate getBirthdate() {
		return this.birthdate;
	}
	
	/**
	 * @param attended
	 */
	public void setAttended(Attended attended) {
		this.attended = attended;
	}
	
	/**
	 * @return attended
	 */
	public Attended getAttended() {
		return this.attended;
	}
	
	/**
	 * By setting healthNum, automatically looks at database and see if
	 * there is previous history and more information about the patient
	 * 
	 * 
	 * @param healthNum
	 * @throws IOException
	 * @throws ParseException 
	 */
	public void setHealthNum(Integer healthNum) throws IOException, ParseException {
		
		if (Program.getInstance().getDatabase().containsKey(healthNum)) {
			VisitRecord vr = Program.getInstance().getDatabase().get(healthNum);
			
			this.name = vr.getName();
			this.birthdate = vr.getBirthdate();
			this.history = vr;
			this.healthNum = healthNum;
		} else {
			this.healthNum = healthNum;
		}
		
	}
	
	/**
	 * This method is called when there may not be a Program instance created yet (most likely while populating).
	 * @param healthNum
	 * @param isProgramInstantiated
	 * @throws IOException
	 * @throws ParseException 
	 */
	public void setHealthNum(Integer healthNum, boolean isProgramInstantiated) throws IOException, ParseException {
		if (isProgramInstantiated) {
			setHealthNum(healthNum);
		} else {
			this.healthNum = healthNum;
		}
	}
	
	/**
	 * @param arrivalTime
	 */
	public void setArrivalTime(ArrivalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	
	/*Below are setters for temp, bp, hr, symptoms, and history.
	 * Should only be used in Program.populateFromFolder() method
	 * where we are trying to load the information from the text files
	 * to the database when launching.
	 */
	
	/**
	 * Sets temp
	 * @param temp
	 */
	public void setTemp(Temperature temp) {
		this.temp = temp;
		this.setUrgency();
	}
	
	/**
	 * Sets bp
	 * @param bp
	 */
	public void setBp(BloodPressure bp) {
		this.bp = bp;
		this.setUrgency();
	}
	
	/**
	 * Sets hr
	 * @param hr
	 */
	public void setHr(HeartRate hr) {
		this.hr = hr;
		this.setUrgency();
	}
	
	
	/**
	 * Sets history
	 * @param history
	 */
	public void setHistory(VisitRecord history) {
		this.history = history;
	}

	/**
	 * @return String representation of Patient, which includes all the attributes except for history
	 */
	public String toString() {
		
		StringBuilder result = new StringBuilder((this.healthNum != null ? this.healthNum + VisitRecord.VALUE_SEP : "") + (this.name != null ? this.name.toString() : "") +  ((this.birthdate != null && this.name != null) ? VisitRecord.VALUE_SEP + this.birthdate.toString() : (this.birthdate != null ? this.birthdate.toString() : "") + ((this.healthNum != null || this.name != null || this.birthdate != null) ? ";" : "")));
		result.append("\n" + this.arrivalTime.toString() + "\n"
				+ this.attended + VisitRecord.VALUE_SEP + this.temp.toString() + VisitRecord.VALUE_SEP + 
				this.bp.toString() + VisitRecord.VALUE_SEP + this.hr.toString());
	    return result.toString();
	    //TODO: no prescription information!
	}
	
	/**
	 * @return a Boolean, true iff patient has been discharged
	 */
	public Boolean getDischarged() {
		return discharged;
	}
	
	/**
	 * @param discharged
	 */
	public void setDischarged(Boolean discharged) {
		this.discharged = discharged;
	}

	public Prescription getPrescription() {
		return prescription;
	}
	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	/**
	 * Comparator for patient arrival times.
	 */

	public static Comparator<Patient> ArrivalTimeComparator = new Comparator<Patient>() {

		/**
		 * Compares patient arrival times of patients p1 and p2. Returns a negative number, 0, positive if p1's arrival
		 * arrival time is less than, equal to, greater than p2's arrival time.
		 * @param p1 is the first Patient object
		 * @param p2 is the second Patient object
		 * @return Negative, 0, positive integer if p1.arrivalTime less than, equal to, greater than p2.arrivalTime
		 */

		
		public int compare(Patient p1, Patient p2) {
			return p1.getArrivalTime().compareTo(p2.getArrivalTime());
		}
	};
	
	/**
	 * Comparator for patient urgency.
	 */
	public static Comparator<Patient> UrgencyComparator = new Comparator<Patient>() {
		/**
		 * Compares patient urgency of patients p1 and p2. Returns a negative number, 0, positive if p1's arrival
		 * arrival time is greater than, equal to, less than p2's arrival time.
		 * @param p1 is the first Patient object
		 * @param p2 is the second Patient object
		 * @return Negative, 0, positive integer if p1.urgency greater than, equal to, less than p2.urgency
		 */
		public int compare(Patient p1, Patient p2) {
			return p2.getUrgency() - p1.getUrgency();
		}
	};

}
