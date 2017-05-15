package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding3 extends AppCompatActivity {
    Spinner baseShiftSpinner;
    SeekBar rootsSeekBar;
    SeekBar plantsSeekBar;
    CheckBox glueCheckBox;
    CheckBox clipsCheckBox;
    int rootsNum = 0;
    int plantsNum = 0;
    View[] views = new View[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_complications);
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
        //views
        baseShiftSpinner = (Spinner) findViewById(R.id.baseShift_spinner);
        rootsSeekBar = (SeekBar) findViewById(R.id.roots_seek_bar);
        final TextView rootsTV = (TextView) findViewById(R.id.roots_tv);
        plantsSeekBar = (SeekBar) findViewById(R.id.plants_seek_bar);
        final TextView plantsTV = (TextView) findViewById(R.id.plants_tv);
        glueCheckBox = (CheckBox) findViewById(R.id.glue_CheckBox);
        clipsCheckBox = (CheckBox) findViewById(R.id.clips_CheckBox);
        views[0] = baseShiftSpinner;
        //item change listener
        baseShiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = baseShiftSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(views);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = baseShiftSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(views);
            }
        });
        //SeekBar listeners
        rootsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress<=12){
                    seekBar.setProgress(0);
                    rootsTV.setText("None");
                    rootsNum = 0;
                }else if(progress<=50){
                    seekBar.setProgress(33);
                    rootsTV.setText("Some");
                    rootsNum = 1;
                }else if(progress<=88){
                    seekBar.setProgress(66);
                    rootsTV.setText("A moderate amount");
                    rootsNum = 2;
                }else{
                    seekBar.setProgress(100);
                    rootsTV.setText("Lots");
                    rootsNum = 3;
                }
            }
        });
        plantsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress<=12){
                    seekBar.setProgress(0);
                    plantsTV.setText("None");
                    plantsNum = 0;
                }else if(progress<=50){
                    seekBar.setProgress(33);
                    plantsTV.setText("Some");
                    plantsNum = 1;
                }else if(progress<=88){
                    seekBar.setProgress(66);
                    plantsTV.setText("A moderate amount");
                    plantsNum = 2;
                }else{
                    seekBar.setProgress(100);
                    plantsTV.setText("Lots");
                    plantsNum = 3;
                }
            }
        });
    }

    public void fabClicked(View fab) {
        if (areViewsValid(views)) {
            //start activity (getIntent to save extras)
            Intent intent = getIntent();
            //update class
            intent.setClass(getApplicationContext(), Wall_Rebuilding_Estimation.class);
            //extras--for passing data
            intent.putExtra("baseShiftIndex", baseShiftSpinner.getSelectedItemPosition());
            intent.putExtra("rootsNum", rootsNum);
            intent.putExtra("plantsNum", plantsNum);
            intent.putExtra("glueChecked", glueCheckBox.isChecked());
            intent.putExtra("clipsChecked", clipsCheckBox.isChecked());
            startActivity(intent);
        }else{
            updateViewValidity(views);
        }
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
