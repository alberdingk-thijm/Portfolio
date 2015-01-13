package com.group_0862.triage;

//import java.util.Comparator;

//import java.util.NavigableMap;
//import java.util.NavigableSet;
//import java.util.Set;
//import java.util.SortedMap;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TreeMap;

//import com.group_0862.triage.Patient;
//import java.lang.StringBuilder;

/**
 * A class containing all of Patient's previous visit records
 */
public class VisitRecord {


	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7340266350295098755L;
	private Integer healthNum;
	private Name name;
	private Birthdate birthdate;
	private TreeMap<Calendar, TreeMap<String, Object>> records;
	private TreeMap<Calendar, TreeMap<PatientAttribute, Object>> records2;
	public static final String TIME_SEP = " >>> ";
	public static final String VALUE_SEP = ",";
	public static final String STATUS_SEP = "\n";
	public static final String VISIT_SEP = " <<< ";
	static String[] statuses = {"attended", "temp", "bp", "hr", "urgency", "discharged", "prescription"}; // urgency is last recorded urgency
	
	/**
	 * VisitRecord constructor using a patient's basic information and a TreeMap containing other information.
	 * @param healthNUm, name, birthdate
	 */
	public VisitRecord(Integer healthNum, Name name, Birthdate birthdate) {
		this.healthNum = healthNum;
		this.name = name;
		this.birthdate = birthdate;
		
		TreeMap<Calendar, TreeMap<String, Object>> records = new TreeMap<Calendar, TreeMap<String, Object>>();
		TreeMap<Calendar, TreeMap<PatientAttribute, Object>> records2 = new TreeMap<Calendar, TreeMap<PatientAttribute, Object>>();
		this.records = records;
		this.records2 = records2;
	}
	
	public TreeMap<PatientAttribute, Object> generateStorage() {
		TreeMap<PatientAttribute, Object> values = new TreeMap<PatientAttribute, Object>();
		for (PatientAttribute patr : PatientAttribute.values()) values.put(patr, null);
		return values;
	}
	
	/**
	 * Changes the states of attended, temp, bp, hr, and symptoms for a given visiting session.
	 * @param arrivalTime, status, statusToChange
	 */
	public void update(Calendar arrivalTime, String status, Object statusToChange) {
		// for a new visit time, first add the new arrivalTime as key and otherInfo as value
		if (!(records.containsKey(arrivalTime))) {
			TreeMap<String, Object> otherInfo = new TreeMap<String, Object>();
			
			for (String s : statuses) {
				otherInfo.put(s, null); 
			}
			
			this.records.put(arrivalTime, otherInfo);
		}
		// find the entry with the arrivalTime and find the status, then replace it
		this.records.get(arrivalTime).put(status, statusToChange);
	}
	
	public void update2(ArrivalTime arrivalTime, Patient p, PatientAttribute pa) {
		if (!(records2.containsKey(arrivalTime))) {
			this.records2.put(arrivalTime, generateStorage());
		}
		this.records2.get(arrivalTime).put(pa, p.getFromEnum(pa));
	}
	
	/**
	 * Change the state of all of a given patient p's PatientAttributes for this visiting session.
	 * @param p
	 */
	public void updateAll2(Patient p) {
		ArrivalTime time = p.getArrivalTime();
		for (PatientAttribute pa : PatientAttribute.values()) {
			if (pa.canUpdate()) {
				update2(time, p, pa);
			}
		}
	}
	
	/**
	 * Updates all the states (attended, temp, bp, hr, discharged, urgency, and prescription) from a Patient object.
	 * @param patient
	 */
	public void updateAll(Patient patient) {
		//Timestamp time = new Timestamp(System.currentTimeMillis()); // shouldn't this be patient.arrivalTime?
		Calendar time = patient.getArrivalTime();
		update(time, "attended", patient.getAttended());
		update(time, "temp", patient.getTemp());
		update(time, "bp", patient.getBp());
		update(time, "hr", patient.getHr());
		update(time, "urgency", patient.getUrgency());
		update(time, "discharged", patient.getDischarged());
		update(time, "prescription", patient.getPrescription());
	}

	/**
	 * @return the name
	 */
	public Name getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(Name name) {
		this.name = name;
	}

	/**
	 * @return the birthdate
	 */
	public Birthdate getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Birthdate birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @return the records
	 */
	public TreeMap<Calendar, TreeMap<String, Object>> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(TreeMap<Calendar, TreeMap<String, Object>> records) {
		this.records = records;
	}

	/**
	 * @return the healthNum
	 */
	public Integer getHealthNum() {
		return this.healthNum;
	}
	
	/**
	 * @param healthNum the healthNum to set
	 */
	public void setHealthNum(Integer healthNum) {
		this.healthNum = healthNum;
	}
	
	/**
	 * @return String representation of the patient's information on their most recent visit.
	 */
	public String showRecent() {
		String result = this.healthNum.toString() + VALUE_SEP + this.name.toString() + VALUE_SEP + this.birthdate.toString();
		if (!this.records.isEmpty()) {
			return result + "\n" + this.find(this.records.lastKey(), statuses);
		} else {
		
			return result + "\n" + "No previous records were found.";
		}
	}
	
	/**
	 * @return String representation of the patient's information on their most recent visit.
	 */
	public String showRecent2() {
		String result = this.healthNum.toString() + VALUE_SEP + this.name.toString() + VALUE_SEP + this.birthdate.toString();
		if (!this.records.isEmpty()) {
			return result + "\n" + this.find2(this.records.lastKey(), PatientAttribute.values());
		} else {
		
			return result + "\n" + "No previous records were found.";
		}
	}
	
	/**
	 * @return result, a String representation of the format in which records are saved to their .txt file.
	 */
	public String writeToFile() {
		// getting healthNum, name, birthdate
		String result =  this.showRecent().split("\n")[0];

		return result + "\n" + this.toString(statuses);
	}
	
	/**
	 * @return result, a String representation of the format in which records are saved to their .txt file.
	 */
	public String writeToFile2() {
		// getting healthNum, name, birthdate
		String result =  this.showRecent().split("\n")[0];

		return result + STATUS_SEP + this.toString2(PatientAttribute.values());
	}
	
	/**
	 * @return the string representation of this that only includes Statuses listed in listStatus
	 */
	public String toString(String[] listStatus) {
		String result = "";
		
		// loop through every visiting session
		for (Calendar arrivalTime : this.records.keySet()) {
			result += "\n" + this.find(arrivalTime, listStatus);
		}
		return result.trim();
	}
	
	public String toString2(PatientAttribute[] attributes) {
		String result = "";
		int visitNum = 1;
		for (Calendar arrivalTime : this.records2.keySet()) {
			result += STATUS_SEP + String.valueOf(visitNum) + ". " + this.find2(arrivalTime, attributes);
			visitNum++;
		}
		return result;
	}
	
	/**
	 * @param arrivalTime
	 * @return patient's state at that time as a String
	 */
	public String find(Calendar arrivalTime, String[] listStatus) {
		// finds the given arrivalTime and returns the patient's state at that time as a String
		TreeMap<String, Object> entry = this.records.get(arrivalTime);
		
		Object[] values = new Object[listStatus.length];  // the values which we want to add
		int i = 0;
		for (String s: listStatus) {
			values[i] = entry.get(s);
			i++;
		}
		String result = arrivalTime.toString() + TIME_SEP;
		
		for (Object o : values) {
			// first element (attended) doesn't need VisitRecord.VALUE_SEP, for formatting reasons
			if (entry.get("attended").equals(o)) {
				result += o.toString();
			} else if (entry.get("urgency").equals(o)); // urgency is not recorded to file
			else {
				result += VALUE_SEP + o.toString();
			}
		}
		return result;
	}
	
	/**
	 * Return a more "human-readable" version of find.
	 * @param arrivalTime
	 * @param attributes
	 * @return
	 */
	public String find2(Calendar arrivalTime, PatientAttribute[] attributes) {
		TreeMap<PatientAttribute, Object> entry = this.records2.get(arrivalTime);
		
		String result = arrivalTime.toString() + TIME_SEP;
		
		for (PatientAttribute pa : attributes) {
			if (pa.canUpdate()) {
				result += STATUS_SEP + pa.name() + ":" + entry.get(pa) + VALUE_SEP;
			}
		}
		result += VISIT_SEP;
		return result;
	}
}
