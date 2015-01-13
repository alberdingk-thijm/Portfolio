package com.group_0862.triage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Scanner;
import java.util.TreeMap;

import android.util.Log;

/**Program is a class which is initiated when the application is launched.
 * @author c3alberd
 *
 */
public class Program implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private TreeMap<Integer, VisitRecord> database; //Map of Patient's healthNum and his/her history
	private PatientList activePatientList;
	private static File dir; //= new File("src/com/group_0862/triage");
	private static Program instance;
	
	/**
	 * This method initializes program by loading the information from visitRecords folder if exists,
	 * or patient_records.txt.
	 * @throws ParseException 
	 */
	private Program() throws IOException, ParseException{
		this.database = new TreeMap<Integer, VisitRecord>();
		this.activePatientList = new PatientList();

		File visitRecords = new File(dir, "visit_records"); //creating a folder/directory called visit_records
		File patientRecords = new File(dir, "patient_records.txt");
		
		if (visitRecords.exists() && visitRecords.listFiles().length != 0) {
			//deleteFilesFromDir(visitRecords.listFiles());
			this.populateFromFolder(visitRecords); //adds info from visit_records directory to database
		} else {
			visitRecords.mkdir(); //making a folder called "visit_records"
			this.populateFirstTime(patientRecords.getPath()); //populating data to activePatientList
		}
	}
	
	@SuppressWarnings("unused")
	private void deleteFilesFromDir(File[] listFiles) {
		for (File f : listFiles) {
			f.delete();
		}
		
	}

	/**
	 * Return the static instance of the Program if it exists, otherwise creates it from a given directory.
	 * @return instance
	 * @throws IOException if dir not found.
	 * @throws ParseException 
	 */
	public static Program getInstance() throws IOException, ParseException{
		if (instance == null) {
			instance = new Program();
		}
		return instance;
	}
	
	/**
	 * @param dir the dir to set
	 */
	public static void setDir(File dir) {
		Program.dir = dir;
	}

	/**
	 * @return dir
	 */
	public File getDir() {
		return dir;
	}
	
	/**
	 * @param database
	 */
	public void setDatabase(TreeMap<Integer, VisitRecord> database) {
		this.database = database;
	}
	
	/**
	 * @param activePatientList
	 */
	public void setActivePatientList(PatientList activePatientList) {
		this.activePatientList = activePatientList;
	}
	
	/**
	 * @param instance
	 */
	public static void setInstance(Program instance) {
		Program.instance = instance;
	}

	/**
	 *  This method enters the data from the file in filePath into this.database.
	 *  Each key-value pair is a health card number key (to be used as a filename) and the associated VisitRecord value.
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	private void populateFirstTime(String filePath) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(filePath));
		String[] record;
		while(scanner.hasNextLine()) {
			record = scanner.nextLine().split(VisitRecord.VALUE_SEP);
			VisitRecord vr = vrHelper(record);
			this.database.put(vr.getHealthNum(), vr);
		}
		scanner.close();
	}
	
	/**This method enters the data from the files in folder into this.database.
	 * The 
	 * @param folder
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void populateFromFolder(File folder) throws IOException, ParseException {
		File[] directoryList = folder.listFiles(); //Making an array of Files
		if (directoryList != null){ // if that folder is not empty,
			for (File f : directoryList) {
			//for (int i = 0; i < directoryList.length; i++) { // for every file in the folder
				Scanner scanner = new Scanner(new FileInputStream(f));
				
				String[] record; // The following lines are for obtaining healthNum, name, and birthdate
				record = scanner.nextLine().split(VisitRecord.VALUE_SEP);
				VisitRecord vr = vrHelper(record);
				
				// Scans each line in the current file. Each line represents one VisitRecord entry.
				while(scanner.hasNext(VisitRecord.VISIT_SEP)) {
					record = scanner.nextLine().split(VisitRecord.TIME_SEP);  //split the arrivalTime and the rest of the information
					
					String timeString = record[0];
					ArrivalTime time = ArrivalTime.strToArrivalTime(timeString);  // changed to use the strToArrivalTime method
					Patient patient = new Patient(time);
					if (record.length > 1) {
						TreeMap<PatientAttribute,Object> statusMap = this.getStatuses2(record[1].split(VisitRecord.VALUE_SEP));
						patient.setAttended((Attended) statusMap.get(PatientAttribute.ATTENDED));
						patient.setTemp((Temperature) statusMap.get(PatientAttribute.TEMPERATURE));
						patient.setBp((BloodPressure) statusMap.get(PatientAttribute.BLOODPRESSURE));
						patient.setHr((HeartRate) statusMap.get(PatientAttribute.HEARTRATE));
						patient.setDischarged((Boolean) statusMap.get(PatientAttribute.DISCHARGED));
						patient.setPrescription((Prescription) statusMap.get(PatientAttribute.PRESCRIPTION));

						vr.updateAll2(patient);

					}
					
					if (!patient.getDischarged()) {
						patient.setName(vr.getName());
						if (instance != null) {
							patient.setHealthNum(vr.getHealthNum(), true);
						} else {
							patient.setHealthNum(vr.getHealthNum(), false);
						}
						patient.setBirthdate(vr.getBirthdate());
						patient.setHistory(vr);
						patient.setUrgency();
						this.activePatientList.addPatient(patient);
					}
				}
				this.database.put(vr.getHealthNum(), vr);
				scanner.close();
				}
			}
		}
	

	/**
	 * Return a new VisitRecord from the given string array record. Used to help parse the health card number,
	 * name and birthdate of patients.
	 * @param record
	 * @return VisitRecord
	 */
	private VisitRecord vrHelper(String[] record){
		Integer healthNum = Integer.parseInt(record[0]);
		Name name = new Name(record[1].split(" "));
		Birthdate birthdate = Birthdate.strToBirthdate(record[2]);
		return new VisitRecord(healthNum, name, birthdate);
	}
	
	/**
	 * Return a TreeMap of <PatientAttribute,Object> which contains all the status-type fields 
	 * (attended, temperature, bloodPressure, heartRate, prescription and discharged) of the String[] record, 
	 * instantiated to their respective classes.
	 * NOTE: Each String of record should be stored as "PATIENTATTRIBUTE:Object" in their .txt file.
	 * @param record
	 * @return Object[] of the value of the statuses
	 */
	public TreeMap<PatientAttribute,Object> getStatuses2(String[] record) {
		TreeMap<PatientAttribute,String> stringMap = new TreeMap<PatientAttribute,String>();
		TreeMap<PatientAttribute,Object> objectMap = new TreeMap<PatientAttribute,Object>();
		for (String s : record) {
			String[] pair = s.split(":");
			stringMap.put(PatientAttribute.valueOf(pair[0]), pair[1]);
		}
		for (PatientAttribute pa : stringMap.keySet()) {
			switch(pa) {
			case ATTENDED:
				Attended attended = new Attended();
				attended.putAll(Status.strToMap((String) stringMap.get(pa)));
				objectMap.put(pa, attended);
				break;
			case BLOODPRESSURE:
				BloodPressure bloodPressure = new BloodPressure();
				bloodPressure.putAll(Status.strToMap((String) stringMap.get(pa)));
				objectMap.put(pa, bloodPressure);
				break;
			case DISCHARGED:
				Boolean discharged = Boolean.parseBoolean((String) stringMap.get(pa));
				objectMap.put(pa, discharged);
				break;
			case HEARTRATE:
				HeartRate heartRate = new HeartRate();
				heartRate.putAll(Status.strToMap((String) stringMap.get(pa)));
				objectMap.put(pa, heartRate);
				break;
			case PRESCRIPTION:
				Prescription prescription = new Prescription();
				prescription.putAll(Status.strToMap((String) stringMap.get(pa)));
				objectMap.put(pa, prescription);
				break;
			case TEMPERATURE:
				Temperature temperature = new Temperature();
				temperature.putAll(Status.strToMap((String) stringMap.get(pa)));
				objectMap.put(pa, temperature);
				break;
			default:
				break;
			
			}
		}

		return objectMap;
	}
	
	/**
	 * @param record
	 * @return Object[] of the value of the statuses
	 */
	public Object[] getStatuses(String[] record) {
		// Record indices based on ALPHABETICAL ORDER!!!!!!!
		Attended attended = new Attended();
		attended.putAll(Status.strToMap(record[0]));
		
		Temperature temperature = new Temperature();
		temperature.putAll(Status.strToMap(record[5]));
		
		BloodPressure bloodPressure = new BloodPressure();
		bloodPressure.putAll(Status.strToMap(record[1]));
		
		HeartRate heartRate = new HeartRate();
		heartRate.putAll(Status.strToMap(record[3]));
		
		Boolean discharged = Boolean.parseBoolean(record[2]);
		
		Prescription prescription = new Prescription();
		prescription.putAll(Status.strToMap(record[4]));
		
		Object[] s = {attended, temperature, bloodPressure, heartRate, discharged, prescription};
		
		return s;
	}
	/**
	 * This method puts the patient into activePatientList if they are not already in it
	 * @param patient
	 */
	public void add(Patient patient) {
		if (!activePatientList.contains(patient)) {
			this.activePatientList.addPatient(patient);
		}
	}

	/**
	 * @return the database
	 */
	public TreeMap<Integer, VisitRecord> getDatabase() {
		return this.database;
	}
	
	/**
	 * @return the activePatientList
	 */
	public PatientList getActivePatientList() {
		return activePatientList;
	}
	
	/**
	 * This method updates each Patient's changes into their history and database.
	 * This method also writes this data to the files in visitRecords.
	 * @throws Exception 
	 */
	public void saveAll() throws Exception {
		File visitRecords = new File(dir, "visit_records");
		if (visitRecords.listFiles().length == 0) { // nothing saved yet
			for (Integer healthNum : this.database.keySet()) {
				File saveFile = new File(visitRecords, healthNum.toString() + ".txt");
				try {
					FileOutputStream outputStream = new FileOutputStream(saveFile);
					saveFile.createNewFile(); //create file
					
					// write to this file
					outputStream.write(this.database.get(healthNum).writeToFile2().getBytes());
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		
		if (activePatientList != null) {
			for (Patient p : activePatientList) {
				p.save(visitRecords);
			}
			this.activePatientList = new PatientList();
		}
		
	}
	
	/**
	 * @param healthNum
	 * @return string of the basic info (name, healthNum, and birthdate) 
	 * of Patient with the healthNum specified
	 */
	public String searchBasicInfo(Integer healthNum) {
		String result = null;
		if (database.containsKey(healthNum)) {
			String patientInfo = database.get(healthNum).showRecent().split("\n")[0];
			result = "Health Card Number: " +  patientInfo.split(VisitRecord.VALUE_SEP)[0] + "\n";
			result += "Name: " + patientInfo.split(VisitRecord.VALUE_SEP)[1] + "\n";
			result += "Birthdate: " + patientInfo.split(VisitRecord.VALUE_SEP)[2];
		} else {
			result = "No patient with the given health card number was found.";
			Log.w("Program", "searchBasicInfo: no patient with the given health card number was found.");
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param healthNum, listStatus
	 * @return string of Statuses listed in listStatus in the previous records
	 * of Patient with the healthNum specified
	 */
	public String searchRecords(Integer healthNum, String[] listStatus) {
		String patientRecords = null;
		if (database.containsKey(healthNum)) {
			patientRecords = database.get(healthNum).toString(listStatus);
		} else {
			patientRecords = "No patient with the given health card number was found.";
			Log.w("Program", "searchRecords: no patient with the given health card number was found.");
		}
		
		return patientRecords;
 	}
	
	public String searchRecords2(Integer healthNum, PatientAttribute[] attributes) {
		String patientRecords = null;
		if (database.containsKey(healthNum)) {
			patientRecords = database.get(healthNum).toString2(attributes);
		} else {
			patientRecords = "No patient with the given health card number was found.";
			Log.w("Program", "searchRecords: no patient with the given health card number was found.");
		}
		
		return patientRecords;
	}
}	

