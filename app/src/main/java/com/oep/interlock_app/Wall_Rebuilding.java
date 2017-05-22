package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.*;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding extends AppCompatActivity {
    // getting to the job layout Views
    private Spinner locationSpinner;
    private Spinner accessibilitySpinner;
    private Spinner maneuverSpinner;
    private View[] views = new View[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wall_rebuilding_tojob);
        // getting to the job layout Views
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        accessibilitySpinner = (Spinner) findViewById(R.id.accessibility_spinner);
        maneuverSpinner = (Spinner) findViewById(R.id.maneuver_spinner);
        views[0] = locationSpinner;
        views[1] = accessibilitySpinner;
        views[2] = maneuverSpinner;
        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException npex){
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }catch(NullPointerException ex){
                //back button not supported
            }
        }
        //listeners
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = locationSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(locationSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = locationSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(locationSpinner);
            }
        });
        accessibilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = accessibilitySpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(accessibilitySpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = accessibilitySpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(accessibilitySpinner);
            }
        });
        maneuverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = maneuverSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(maneuverSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = maneuverSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(maneuverSpinner);
            }
        });
    }



    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Wall_Rebuilding2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("locationIndex", locationSpinner.getSelectedItemPosition());
            intent.putExtra("accessIndex", accessibilitySpinner.getSelectedItemPosition());
            intent.putExtra("maneuverIndex", maneuverSpinner.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(views);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
