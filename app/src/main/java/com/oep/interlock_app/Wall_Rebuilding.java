package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.*;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding extends AppCompatActivity {
    // getting to the job layout Views
    private Spinner locationSpinner;

    private int accessibilityNum = 0;
    private int maneuverNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wall_rebuilding_tojob);
        // getting to the job layout Views
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        SeekBar accessibilitySeekBar = (SeekBar) findViewById(R.id.accessibility_seek_bar);
        final TextView accessibilityTextView = (TextView) findViewById(R.id.accessibility_tv);
        SeekBar maneuverSeekBar = (SeekBar) findViewById(R.id.maneuver_seek_bar);
        final TextView maneuverTextView = (TextView) findViewById(R.id.maneuver_tv);

        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }catch(NullPointerException ex){
                //back button not supported
            }
        }
        //spinner listeners
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
        //Seek Bar Listeners
        accessibilitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress == 0){
                    accessibilityTextView.setText("Hard to access");
                    accessibilityNum = 0;
                }else if(progress == 1){
                    accessibilityTextView.setText("Average");
                    accessibilityNum = 1;
                }else if(progress == 2){
                    accessibilityTextView.setText("Easy to access");
                    accessibilityNum = 2;
                }
            }
        });
        maneuverSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress == 0){
                    maneuverTextView.setText("Little room");
                    maneuverNum = 0;
                }else if(progress == 1){
                    maneuverTextView.setText("Some room");
                    maneuverNum = 1;
                }else if(progress == 2){
                    maneuverTextView.setText("Lots of room");
                    maneuverNum = 2;
                }
            }
        });
    }



    public void fabClicked(View view){
        if(isViewValid(locationSpinner)) {
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Wall_Rebuilding2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("locationIndex", locationSpinner.getSelectedItemPosition());
            intent.putExtra("accessIndex", accessibilityNum);
            intent.putExtra("maneuverIndex", maneuverNum);
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(locationSpinner);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
