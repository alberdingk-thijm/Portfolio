package com.group_0862.triage;

import java.io.IOException;
import java.text.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * This Activity shows the history of the patient
 */
public class CompleteInfoActivity extends Activity {
	
	protected static String userStatus; //either "nurse" or "physician"
	@Override
	/**
	 * When Activity is created, display a Patient's full history.
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_info);
		String data = (String) getIntent().getSerializableExtra("healthNum");
		int healthNum = Integer.parseInt(data);
		String info = "";
		try {
			PatientAttribute[] attributes = null;
			if (userStatus.equals("nurse")) attributes = new PatientAttribute[]{PatientAttribute.TEMPERATURE, PatientAttribute.BLOODPRESSURE, PatientAttribute.HEARTRATE}; //nurse can only see previous records of temp, bp and hr
			else attributes = PatientAttribute.values();
			info = Program.getInstance().searchRecords2(healthNum, attributes); //doctors can see all records
			 
		} catch (IOException e) {
			e.getMessage();
		} catch (ParseException e) {
			e.getMessage();
		}
		
		
		TextView infoDisplay = (TextView) findViewById(R.id.complete_display);
		infoDisplay.setText(info);
		infoDisplay.setMovementMethod(new ScrollingMovementMethod());
		//TODO: display string no history when there's no history
	}
}

