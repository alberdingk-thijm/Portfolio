package com.group_0862.triage;

import java.util.ArrayList;
import java.util.Collections;

import com.group_0862.triage.Patient;

/**
 * This is a collection of Patient objects.
 */
public class PatientList extends ArrayList<Patient> {
	
	
	private static final long serialVersionUID = 3165002156371425365L;
	int length = this.size();
	
	/**
	 * Constructor for PatientList
	 */
	public PatientList() {
		super();
	}
	
	public void orderByArrivalTime() {
		Collections.sort(this, com.group_0862.triage.Patient.ArrivalTimeComparator);		
	}

	public void orderByUrgency() {
		Collections.sort(this, com.group_0862.triage.Patient.UrgencyComparator);
	}
	
	/**
	 * Return a Patient object from the PatientList based on the given ArrivalTime.
	 * @return
	 */
	public Patient find(ArrivalTime time) {
		Patient found = null;
		for (Patient p : this) {
			if (p.getArrivalTime().equals(time)) {
				found = p;
			}
		}
		return found;
	}
	
	/**
	 * This method adds patient to this.
	 * @param patient
	 */
	public void addPatient(Patient patient){
		this.add(patient);
	}
	
	/**
	 * Used to display list of active patients in UpdateSignsActivity. List numbering starts from 1.
	 * @return String representation of PatientList,
	 */
	public String toString(){
		String result = "";

		for (int i = 0; i < this.size(); i++){ 
			result += "\n" + String.valueOf(i + 1) + ". " + this.get(i).getArrivalTime().toString().split("\\.")[0] 
					+ (this.get(i).getName() != null ? (VisitRecord.VALUE_SEP + this.get(i).getName().toString()) : "") 
					+ (this.get(i).getHealthNum() != null ? (VisitRecord.VALUE_SEP + Integer.toString(this.get(i).getHealthNum())) : "");

		}
		
		return result.trim();
	}
	
	
}
