package com.group_0862.triage;

/**
 * Simple element of the SparseArrays used in the PatientList classes.
 * Has a connected patient and a representative string.
 * @author Tim
 *
 */
public class PatientItem {

	public Patient p;
	public String string;

	public PatientItem(Patient patient) {
		this.p = patient;
		this.string = patient.getArrivalTime().toString() + 
				/* also provides the name if it exists */(patient.getName() != null ? ", " + patient.getName().toString() : "");
	}
}
