package com.group_0862.triage;



import java.io.IOException;
import java.text.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.group_0862.triage.Caption.Infos;

//TODO: use this activity for updating signs and prescription, have to putExtra from MenuActivity
/**
 * An Activity where nurse can click on which Patient needs his/her
 * information to be updated.
 * NOTE: this activity uses the defunct "PatientGroupOld" and "Caption" classes to display its information. Either delete the
 * activity or keep these classes.
 */
public class UpdateSignsActivity extends Activity implements OnChildClickListener{
	private PatientList patientList;
	SparseArray<PatientGroupOld> groups = new SparseArray<PatientGroupOld>();
	private UpdateSignsAdapter adapter;
	Infos[] infos = Infos.values();
	
	/**
	 * Called when Activity is created
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_update_signs);
	    try {
			createData();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    adapter = new UpdateSignsAdapter(this, groups);
	    listView.setAdapter(adapter);
	    listView.setOnChildClickListener(this);
	 }
	
	/**
	 * Supplies the Visit Record ExpandableListView with the active patients.
	 * @throws ParseException 
	 */
	private void createData() throws ParseException {
		try {
			patientList = Program.getInstance().getActivePatientList();
			int i = 0;
			for (Patient p: patientList) {
				//String[] patient_strings = p.toString().split(",");
				groups.append(i,createPatientGroup(p));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private PatientGroupOld createPatientGroup(Patient p){
		
		PatientGroupOld group = new PatientGroupOld(p);
		for (Infos i : infos) {
			Caption c = new Caption(i, p);
			group.children.add(c);
		}
		return group;
	}
	
	/**
	 * Saves all checked patients and their children to their respective visit records.
	 * @param view
	 * @throws ParseException 
	 */
	public void onClickSaveChanges(View view) throws ParseException{
		SparseArray<Caption.Infos>foundChanges = new SparseArray<Caption.Infos>();
		for (int i=0;i<adapter.getGroupCount();i++) {
			PatientGroupOld current_g = (PatientGroupOld) adapter.getGroup(i);
			Patient current_p = patientList.get(i);
			for (Caption c: current_g.children) {
				CheckedTextView child = (CheckedTextView) findViewById(R.id.checkedTextView);
				//EditText info = (EditText) findViewById(R.id.updateInfoEdit);
				if (c.checked) {
					for (Infos j : infos) {
						if (j.getName() == child.getText().toString()) {
							c.updatePatient(j, current_p);
							foundChanges.append(foundChanges.size(), j);
						}
					}				
				}
			}
		}
		if (foundChanges.size() == 0) {
			Toast.makeText(getApplicationContext(), "No changes found", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Found and saved changes in " + foundChanges.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Toggles the CheckedTextView's check mark.
	 * @param v
	 */
	public void toggleCheckMark(View v) {
		CheckedTextView checkView = (CheckedTextView) v;
		checkView.setChecked(!checkView.isChecked());
		
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		adapter.getChild(groupPosition, childPosition).checked=(!adapter.getChild(groupPosition, childPosition).checked);
		Log.d("TEST","---");
		toggleCheckMark(v);
		return true;
	}
}
