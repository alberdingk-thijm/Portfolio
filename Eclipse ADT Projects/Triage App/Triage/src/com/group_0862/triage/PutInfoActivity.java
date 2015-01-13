package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity has a few Text Field for nurse to put values that have to be updated
 *
 */
public class PutInfoActivity extends Activity {
	
	private Patient patient;
	private Integer index;
	private PatientList activePatientList;
	private String[] oldStrings;
	private String[] newStrings;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_put_info);
		/*patient = (Patient) getIntent().getSerializableExtra("patient");*/
		try {
			activePatientList = Program.getInstance().getActivePatientList();
			// by default (or if there was an error) the patient will be the last patient on the list
			index = getIntent().getIntExtra("index", -1);
			patient = activePatientList.get(index);  
			oldStrings = getPatientData(findViewById(R.id.update));  // ensures any data that is already present is in the EditTexts
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks and updates each changed field, using the various methods.
	 * Once finished, displays a Toast of the updated values.
	 * @param view
	 * @throws ParseException
	 * @throws IOException
	 */
	public void updateInformation(View view) throws ParseException, IOException{
		
		newStrings = gatherEdits(view);
		saveChanges(newStrings);
		activePatientList.set(index, patient);
		toastChanges();
	}
	
	/**
	 * Return the string values of each EditText in the activity which has been updated from the activity's patient attribute.
	 * @param v
	 * @return
	 */
	public String[] getPatientData(View v) {
		String healthNumEditText = getEditText(R.id.health_number,patient.getHealthNum());
		
		String nameEditText = getEditText(R.id.name,patient.getName());
		
		String birthdateEditText = getEditText(R.id.birthdate, patient.getBirthdate());
		
		String attendedEditText = getEditText(R.id.attended, patient.getAttended().getLatest());
		
		String tempEditText = getEditText(R.id.temperature, patient.getTemp().getLatest());
		
		String bpEditText = getEditText(R.id.blood_pressure, patient.getBp().getLatest());
		
		String hrEditText = getEditText(R.id.heart_rate, patient.getHr().getLatest());
		
		String disEditText = getEditText(R.id.discharged, patient.getDischarged());

		return new String[]{healthNumEditText, nameEditText, birthdateEditText, 
				attendedEditText, tempEditText, bpEditText, hrEditText, disEditText};
	}
	
	/**
	 * Return the String values of each EditText without first updating them.
	 * @param v
	 * @return
	 */
	public String[] gatherEdits(View v) {
		String healthNumEditText = getEditText(R.id.health_number, null);
		String nameEditText = getEditText(R.id.name, null);
		String birthdateEditText = getEditText(R.id.birthdate, null);
		String attendedEditText = getEditText(R.id.attended, null);
		String tempEditText = getEditText(R.id.temperature, null);
		String bpEditText = getEditText(R.id.blood_pressure, null);
		String hrEditText = getEditText(R.id.heart_rate, null);
		String disEditText = getEditText(R.id.discharged, null);
		return new String[]{healthNumEditText, nameEditText, birthdateEditText, 
				attendedEditText, tempEditText, bpEditText, hrEditText, disEditText};
	}
	
	/**
	 * Return an EditText's String item. 
	 * Accepts an attribute (from Patient) as an optional parameter to set that String to some value.
	 * @param id, attribute
	 * @return
	 */
	public String getEditText(int id, Object attribute) {
		EditText et = (EditText) findViewById(id);
		if (attribute != null) et.setText(attribute.toString());
		return et.getText().toString().trim();
	}
	
	public void saveChanges(String[] newVals) throws NumberFormatException, IOException, ParseException {
		if (!newVals[0].isEmpty() && (!newVals[0].equals(patient.getHealthNum().toString()))) patient.setHealthNum(Integer.parseInt(newVals[0]));
		if (!newVals[1].isEmpty() && (!newVals[1].equals(patient.getName().toString()))) patient.setName(new Name(newVals[1].split(" ")));
		if (!newVals[2].isEmpty() && (!newVals[2].equals(patient.getBirthdate().toString()))) patient.setBirthdate(Birthdate.strToBirthdate(newVals[2]));
		if (!newVals[3].isEmpty() && (!patient.getAttended().containsValue(newVals[3]))) patient.getAttended().update(newVals[3], patient);
		if (!newVals[4].isEmpty() && (!patient.getTemp().containsValue(newVals[4]))) patient.getTemp().update(newVals[4], patient);
		if (!newVals[5].isEmpty() && (!patient.getBp().containsValue(newVals[5]))) patient.getBp().update(newVals[5], patient);
		if (!newVals[6].isEmpty() && (!patient.getHr().containsValue(newVals[6]))) patient.getHr().update(newVals[6], patient);
		if (!newVals[7].isEmpty()) patient.setDischarged(Boolean.parseBoolean(newVals[7]));
	}
	
	public void toastChanges() {
		String toast = "Updated values:";
		for (int i=0; i<newStrings.length; i++) {
			if (oldStrings[i].compareTo(newStrings[i])!=0) {
				toast += ("\n" + oldStrings[i] + " --> " + newStrings[i]);
			}
		}
		Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
	}
	
}

