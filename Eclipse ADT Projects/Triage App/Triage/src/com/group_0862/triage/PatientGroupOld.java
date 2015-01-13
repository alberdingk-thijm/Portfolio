package com.group_0862.triage;

import java.util.ArrayList;
import java.util.List;
import com.group_0862.triage.Caption;


/**
 * Simple tree-like class which takes a Patient and a 
 * list of children for displaying the listItems of the 
 * PatientListAdapter ExpandableListView.
 * @author Tim
 *
 */
public class PatientGroupOld {

	public Patient p;
	public String string;
	public final List<Caption> children = new ArrayList<Caption>();

	public PatientGroupOld(Patient patient) {
		this.p = patient;
		this.string = patient.getArrivalTime().toString();
	}
}
