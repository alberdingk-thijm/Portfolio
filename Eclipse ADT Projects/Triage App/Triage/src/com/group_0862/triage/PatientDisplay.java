package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This Activity displays Patient's basic information 
 */
public class PatientDisplay extends Activity {
	
	String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_display);
		this.data = (String) this.getIntent().getSerializableExtra("healthNum");
		this.data.trim();
		Integer healthNum = Integer.parseInt(this.data);
		String info = "";
		try {
			info = Program.getInstance().searchBasicInfo(healthNum);			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		TextView infoDisplay = (TextView) findViewById(R.id.patient_display);
		infoDisplay.setText(info);
		
		
	}
	
	public void lookHistory(View view){
		Intent intent = new Intent(this, CompleteInfoActivity.class);
		intent.putExtra("healthNum", this.data);
		startActivity(intent);	
	}
}