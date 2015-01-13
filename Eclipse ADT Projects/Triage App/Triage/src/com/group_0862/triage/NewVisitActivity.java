package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioButton;

public class NewVisitActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_visit);
		
		RadioButton new_patient_button = (RadioButton) findViewById(R.id.radio_new);
		new_patient_button.setChecked(true);
	}
	
	/**
	 * Checks if the radio buttons indicate if the patient is new or returning. If new, then use addPatient, else use addVisit
	 * @param view
	 */
	public void register(View view) {
		
		RadioGroup patient_type = (RadioGroup) findViewById(R.id.radio_button);
		int selectedId = patient_type.getCheckedRadioButtonId();
		boolean isNew = ((RadioButton) patient_type.getChildAt(0)).isChecked();
		if (isNew) {
			
		}
		
		switch (selectedId) {
			case 0x7f080008:
				try {
					addPatient(view);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 0x7f080009:
				try {
					addVisit(view);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
		}
	}

	/**
	 * Called when nurse clicks the "New patient" button.
	 * If the health number was given, proceed to NewPatientActivity create a record for this patient.
	 * @param view
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void addPatient(View view) throws IOException, ParseException {
		VisitRecord vr = null;
		
		// getting values from all fields
		EditText healthNumText = (EditText) findViewById(R.id.health_num_input);
		String healthNumStr = healthNumText.getText().toString();
		
		EditText fullNameText = (EditText) findViewById(R.id.full_name);
		String[] nameList = fullNameText.getText().toString().split(" ");
		Name name = new Name(nameList);
		
		// get the View, type-cast it as an EditText, get the Editable, get the String of the Editable, turn that into an Integer...
		String birthStr = ((EditText) findViewById(R.id.year)).getText().toString() + "-" + 
		((EditText) findViewById(R.id.month)).getText().toString() + "-" + ((EditText) findViewById(R.id.day)).getText().toString();
		/*Integer year = Integer.parseInt(((EditText) findViewById(R.id.year)).getText().toString());
		Integer month = Integer.parseInt(((EditText) findViewById(R.id.month)).getText().toString());
		Integer day = Integer.parseInt(((EditText) findViewById(R.id.day)).getText().toString());
		Birthdate birthdate = new Birthdate();*/
		Birthdate birthdate = null;
		try {
			/*birthdate.set(year, month, day);*/
			birthdate = Birthdate.strToBirthdate(birthStr);
		} catch (IllegalArgumentException e) {
			Toast.makeText(getApplicationContext(), "Invalid format!", Toast.LENGTH_LONG).show();
		}
		
		if (!healthNumStr.isEmpty() && name.hasCorrectFormat() && birthdate != null) {
			Integer healthNum = Integer.parseInt(healthNumStr);
			vr = new VisitRecord(healthNum, name, birthdate);
			Program p = Program.getInstance();
			p.getDatabase().put(healthNum, vr); // add patient's record to database
			Patient patient = new Patient(new ArrivalTime(), healthNum); // new visit
			p.getActivePatientList().addPatient(patient);
			Toast.makeText(getApplicationContext(), "Patient added and visit recorded!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "Please fill in all fields!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Called when nurse clicks the "Returning patient" button.
	 * @param view
	 * @throws IOException
	 * @throws ParseException 
	 */
	public void addVisit(View view) throws IOException, ParseException{

		Patient patient = null;
		EditText healthNumText = (EditText) findViewById(R.id.health_num_input);
		String healthNumStr = healthNumText.getText().toString();
		
		if (!healthNumStr.isEmpty()){
			Integer healthNum = Integer.parseInt(healthNumStr);
			
			for (Patient p : Program.getInstance().getActivePatientList()) {
				if (p.getHealthNum().equals(healthNum)) {
					Toast.makeText(getApplicationContext(), "Patient already admitted", Toast.LENGTH_LONG).show();
					return;
				}
			}
			
			
			if (Program.getInstance().getDatabase().containsKey(healthNum)) {
				patient = new Patient(new ArrivalTime(), healthNum);
			    patient.setHealthNum(healthNum);
			} else {
				Toast.makeText(getApplicationContext(), "Patient not in database", Toast.LENGTH_LONG).show();
		    }
		} else {
			patient = new Patient(new ArrivalTime());
		}
		
		Program.getInstance().getActivePatientList().addPatient(patient);
		Toast.makeText(getApplicationContext(), "Visit created!", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
		startActivity(intent);
	}
}