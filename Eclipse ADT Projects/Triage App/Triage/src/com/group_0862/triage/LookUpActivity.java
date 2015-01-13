package com.group_0862.triage;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity allows nurse to look up patient's info and history
 *
 */
public class LookUpActivity extends Activity {

	@Override
	/**
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_up);
	}
	
	/**
	 * This method transfers to another Activity where Patient's info is displayed.
	 * @param view
	 */
	public void searchVr(View view){
		EditText inputEditText = (EditText) findViewById(R.id.lookup_input);
		String inputStr = inputEditText.getText().toString();
		if (inputStr.isEmpty()){
			Toast.makeText(getApplicationContext(), "Please input health card number", Toast.LENGTH_LONG).show();
		}
		else {
			Intent intent = new Intent(this, PatientDisplay.class);
			intent.putExtra("healthNum", inputStr);
			startActivity(intent);
		}
	}
}
