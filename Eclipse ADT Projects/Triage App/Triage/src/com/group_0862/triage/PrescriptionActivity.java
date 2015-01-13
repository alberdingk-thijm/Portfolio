package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity showing previous prescription of Patient
 */
public class PrescriptionActivity extends Activity{
	
	Program prog;
	Prescription presc;
	Patient prescriptionRecipient;
	final ArrivalTime now = new ArrivalTime();
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescription);
		try {
			prog = Program.getInstance();
			//prog.setActivePatientList(new PatientList()); // only used to help with saving
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/** shows previous prescription
	 * @param view
	 * @throws IOException
	 * @throws ParseException
	 */
	public void onClickHealthNum(View view) {
		EditText healthNumText = (EditText) findViewById(R.id.input_health_num);
		String healthNumStr = healthNumText.getText().toString();
		if (healthNumStr.isEmpty()) Toast.makeText(getApplicationContext(), "Please input health number", Toast.LENGTH_LONG).show();
		else {
		Integer healthNum = Integer.parseInt(healthNumStr);

		String prevPresc = prog.searchRecords(healthNum, new String[]{PatientAttribute.PRESCRIPTION.getName()});
		
		TextView prevPrescDisp = (TextView) findViewById(R.id.textView1);
		prevPrescDisp.setText("Previous Prescription Record:" + "/n" + prevPresc);
		}
	}
	
	/**
	 * add new Prescription to Patient
	 * @param view
	 * @throws IOException
	 * @throws ParseException
	 */
	public void onClickAddPresc(View view) {
		EditText healthNumText = (EditText) findViewById(R.id.input_health_num);
		String healthNumStr = healthNumText.getText().toString();
		EditText prescNameText = (EditText) findViewById(R.id.input_presc_name);
		String prescNameStr = prescNameText.getText().toString();
		EditText prescInsText = (EditText) findViewById(R.id.input_presc_instruction);
		String prescInsStr = prescInsText.getText().toString();
		if (healthNumStr.isEmpty()) Toast.makeText(getApplicationContext(), "Please input health number", Toast.LENGTH_LONG).show();
		else if (prescNameStr.isEmpty()) Toast.makeText(getApplicationContext(), "Please input name of medication", Toast.LENGTH_LONG).show();
		else if (prescInsStr.isEmpty()) Toast.makeText(getApplicationContext(), "Please input medication instructions", Toast.LENGTH_LONG).show();
		else {
			Integer healthNum = Integer.parseInt(healthNumStr);
			try {
				prescriptionRecipient = new Patient(now, healthNum);
				prog.add(prescriptionRecipient);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			/*for (Patient p : prog.getActivePatientList()) {
				if (p.getHealthNum().equals(healthNum)) {
					p.getPrescription().update(prescNameStr + prescInsStr, p);
					Toast.makeText(getApplicationContext(), "Added prescription!", Toast.LENGTH_LONG).show();
				}
			}*/
			prescriptionRecipient.getPrescription().update("Name: " + prescNameStr +"; Instruction: " + prescInsStr, prescriptionRecipient);
			prescriptionRecipient.getAttended().update("Doctor", prescriptionRecipient);
			VisitRecord vr = prog.getDatabase().get(healthNum);
			vr.updateAll(prescriptionRecipient);  // VisitRecord will look for all kinds of crazy stuff
			vr.update(now, PatientAttribute.PRESCRIPTION.getName(), prescriptionRecipient.getPrescription());
			vr.update(now, PatientAttribute.ATTENDED.getName(), prescriptionRecipient.getAttended());
			try {
				prog.saveAll();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("PrescriptionActivity", "Error saving patient.");
			}
			prog.setActivePatientList(new PatientList());  // reset activePatientList
		}
	}
}
