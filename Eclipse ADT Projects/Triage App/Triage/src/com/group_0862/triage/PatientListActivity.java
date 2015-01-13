package com.group_0862.triage;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class PatientListActivity extends ListActivity {
	
	public SparseArray<PatientItem> patientItems = new SparseArray<PatientItem>();
	private PatientList activePatients;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_list);
		createData();
		PatientListAdapter adapter = new PatientListAdapter(this, patientItems);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		PatientItem item = (PatientItem) getListAdapter().getItem(position);
		Intent putInfoIntent = new Intent(this, PutInfoActivity.class);
		putInfoIntent.putExtra("index", activePatients.indexOf(item.p));
		startActivity(putInfoIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void createData() {
		try {
			activePatients = Program.getInstance().getActivePatientList();
			int i = 0;
			for (Patient p: activePatients) {
				patientItems.append(i,new PatientItem(p));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
}
