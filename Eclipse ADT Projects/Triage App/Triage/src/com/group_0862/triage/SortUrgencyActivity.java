package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SortUrgencyActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_urgency);
		
		PatientList active_patientlist = new PatientList();
		PatientList not_attended = new PatientList();
		
		try {
			active_patientlist = Program.getInstance().getActivePatientList();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (Patient patient : active_patientlist) {
			if (patient.getAttended().isEmpty()) not_attended.addPatient(patient);
		}
		not_attended.orderByUrgency();
		
		ListView urgency_list = getListView();
		ArrayAdapter<Patient> ulAdapter = new ArrayAdapter<Patient>(this, R.layout.single_list_item_view, R.id.patient_info, not_attended);
		urgency_list.setAdapter(ulAdapter);		
		
	}
	/**
	 * This method starts the CompleteInfoActivity if the patient selected has a health card number else it tells the user that
	 * the patient doesn't have a health card number.
	 */
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		
		Patient selected = (Patient) list.getAdapter().getItem(position);
		if (selected.getHealthNum() != null) {
			Intent intent = new Intent(this, CompleteInfoActivity.class);
			intent.putExtra("healthNum", String.valueOf(selected.getHealthNum()));
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "Patient has no health card number", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	

}
