package com.wheel.tim.settlementgenerator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    Spinner typespin;
    SettleEnum settleEnum;
    Settlement sett;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create an ArrayAdapter using the string array and a default spinner layout
        typespin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typespin.setAdapter(adapter);
        typespin.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickGenerate(View view) {
        sett = new Settlement(settleEnum);
        TextView popText = (TextView) findViewById(R.id.popText);
        popText.setText(String.valueOf(sett.getPop()) + " persons.");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // converts the string name to uppercase (in order that it can be parsed by settleEnum).
        String convertedItem = ((String) parent.getItemAtPosition(position)).toUpperCase();
        settleEnum = SettleEnum.valueOf(convertedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
