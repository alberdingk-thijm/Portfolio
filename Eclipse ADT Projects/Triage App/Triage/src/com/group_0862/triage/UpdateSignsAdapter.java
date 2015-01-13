package com.group_0862.triage;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.group_0862.triage.Caption.Infos;
//import android.widget.Toast;

/**
 * Custom ListAdapter class used for the PutInfo Activity.
 * @author Tim
 *
 */
public class UpdateSignsAdapter extends BaseExpandableListAdapter {

	private final SparseArray<PatientGroupOld> groups;
	public LayoutInflater inflater;
	public Activity activity;
	
	public UpdateSignsAdapter(Activity act, SparseArray<PatientGroupOld> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}
	
	/**
	 * Append a Patient p to the adapter's groups as a new item of the list. 
	 * Uses the standard caption titles as provided in Caption's Infos enumerator.
	 * @param p is a patient object
	 */
	public void appendPatient(Patient p) {
		PatientGroupOld patientGroup = new PatientGroupOld(p);
		for (Infos i : Infos.values()) {
			Caption c = new Caption(i, p);
			patientGroup.children.add(c);
		}
		this.groups.append(this.groups.size(), patientGroup);
	}

	@Override
	public Caption getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Caption children = (Caption) getChild(groupPosition, childPosition);
		TextView text = null;
		EditText edit = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
		}
		text = (CheckedTextView) convertView.findViewById(R.id.checkedTextView);
		text.setText(children.left);
		edit = (EditText) convertView.findViewById(R.id.updateInfoEdit); 
		edit.setText(children.right);
		
		/*if(((CheckedTextView) text).isChecked()) {
			   text.setImageResource(R.drawable.btn_check_on_holo_light);
			  } else {
			   .setImageResource(R.drawable.btn_check_off_normal_holo_light);
			  }*/
		
		return convertView;
	}
	
	

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public PatientGroupOld getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}
	
	public SparseArray<PatientGroupOld> getGroups() {
		return groups;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
		/*if (getGroup(groupPosition) == add) {
			Group nextGroup = new Group(add);
			groups.put(groupPosition++, nextGroup);
			
			
			//groups.setValueAt(groupPosition, )
		}*/
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		PatientGroupOld pGroup = (PatientGroupOld) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(pGroup.string);
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
} 