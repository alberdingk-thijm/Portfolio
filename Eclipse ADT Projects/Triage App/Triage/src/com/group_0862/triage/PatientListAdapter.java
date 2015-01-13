package com.group_0862.triage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PatientListAdapter extends BaseAdapter {
	
	private final SparseArray<PatientItem> groups;
	public LayoutInflater inflater;
	public Activity activity;
	
	public PatientListAdapter(Activity act, SparseArray<PatientItem> groups){
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public PatientItem getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		PatientItem pGroup = getItem(position);
		((TextView) convertView).setText(pGroup.string);
		return convertView;
	}

}
