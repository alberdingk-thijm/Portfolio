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

import com.group_0862.triage.NMenu;
import com.group_0862.triage.PMenu;

public class MenuActivity extends ListActivity {

	private NMenu nmenu;
	private PMenu pmenu;
	private static boolean isNurse;
	private String[] menu;
	
	@Override
    /**
     * This method is called when the Activity is launched. 
     * It displays menu,
     * and transfers to next Activity when user clicked a selection.
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
		
		isNurse = getIntent().getBooleanExtra("isNurse", true);
		menu = new String[(isNurse ? NMenu.values().length : PMenu.values().length)];
		int i = 0;
		if (isNurse) {
			for (NMenu n : NMenu.values()) {
				menu[i] = n.getS();
				i++;
			}
		} else {
			for (PMenu p : PMenu.values()) {
				menu[i] = p.getS();
				i++;
			}
		}
		
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		try {
			Program.setDir(this.getApplicationContext().getFilesDir());
			@SuppressWarnings("unused")
			Program program = Program.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// initiating and assigning the list adapter
		ArrayAdapter<String> nMenuAdapter = new ArrayAdapter<String>(this, R.layout.single_list_item_view, R.id.product_label, menu);
		setListAdapter(nMenuAdapter);
	}
    
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
    	super.onListItemClick(list, view, position, id);
    	String choice = menu[position];  // the PMenu or NMenu string which we will compare to the possible values
    	if (!isNurse) {
    		for (PMenu p : PMenu.values()) {
        		if (p.getS() == choice)
        			pmenu = p;
        	}
    		switch (pmenu) {
    		case PRESCRIBE:
    			Intent prescriptionIntent = new Intent(this, pmenu.getC());
    			startActivity(prescriptionIntent);
    		case LOOKUP:
    			Intent lookUpIntent = new Intent(this, pmenu.getC());
    			startActivity(lookUpIntent);
    			break;
    		}
    	} else {
    		for (NMenu n : NMenu.values()) {
        		if (n.getS() == choice)
        			nmenu = n;
        	}
    		switch (nmenu) {
    		case LOOKUP:
    			Intent lookUpIntent = new Intent(this, nmenu.getC());
    			startActivity(lookUpIntent);
    			break;
    		case NEW:
    			Intent newVisitIntent = new Intent(this, nmenu.getC());
    			startActivity(newVisitIntent);
    			break;
    		case LIST:
    			Intent listIntent = new Intent(this, nmenu.getC());
    			startActivity(listIntent);
    			break;
    		case URGENCY:
    			Intent urgencyIntent = new Intent(this, nmenu.getC());
    			startActivity(urgencyIntent);
    			break;
    		case SAVE:
    			try {
    				Program.getInstance().saveAll();
    				Toast.makeText(getApplicationContext(), "Changes were saved!", Toast.LENGTH_LONG).show();
    			} catch (IOException e) {
    				e.printStackTrace();
    			} catch (Exception e) {
    				Toast.makeText(getApplicationContext(), "Missing health card number, cannot save.", Toast.LENGTH_LONG).show();
    			} break;
    		}
    	}
	}

 }

