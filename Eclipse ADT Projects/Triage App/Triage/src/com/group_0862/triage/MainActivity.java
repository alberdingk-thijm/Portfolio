package com.group_0862.triage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    @Override
    /**
     * This method is called when the Activity is launched. 
     * It displays textboxes for entering the user's username and password
     * and moves to the menu available to the user's job.
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
    }
    
    public void onClickLogin(View view) throws FileNotFoundException {
    	EditText usernameText = (EditText) findViewById(R.id.username);
    	EditText passwordText = (EditText) findViewById(R.id.password);
    	
    	String usernameStr = usernameText.getText().toString();
    	String passwordStr = passwordText.getText().toString();
    	
    	File pw = new File(this.getApplicationContext().getFilesDir(), "passwords.txt");
    	Scanner scanner = new Scanner(new FileInputStream(pw.getPath()));
    	
    	String[] info = null;
    	Intent loginIntent = null;
    	
    	scan:
    	while (scanner.hasNext()) {
    		info = scanner.next().split(",");
    		String user = info[0];
    		String pass = info[1];
    		String job = info[2];
    		
    		if (usernameStr.equals(user) && passwordStr.equals(pass)) {
    			if (job.equals("nurse")) {
    				CompleteInfoActivity.userStatus = "nurse"; //tell CompleteInfoActivity to display only temp, bp, and hr
    			} else {
    				CompleteInfoActivity.userStatus = "physician"; //tell CompleteInfoActivity to display everything about the Patient
    			}
    			loginIntent = new Intent(this, MenuActivity.class);
    			loginIntent.putExtra("isNurse", job.equals("nurse"));
    			break scan;
    		}
    	}
    	
    	if (loginIntent != null) {
    		startActivity(loginIntent);
    	} else {
    		Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_LONG).show();
    	}
    	
    }
 }

