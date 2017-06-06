package com.oep.interlock_app;

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

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding3 extends AppCompatActivity {
    CheckBox glueCheckBox;
    CheckBox clipsCheckBox;
    int baseShiftNum = 0;
    int rootsNum = 0;
    int plantsNum = 0;

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
        SeekBar baseShiftSeekBar = (SeekBar) findViewById(R.id.base_shift_seek_bar);
        final TextView baseShiftTextView = (TextView) findViewById(R.id.base_shift_text_view);
        SeekBar rootsSeekBar = (SeekBar) findViewById(R.id.roots_seek_bar);
        final TextView rootsTV = (TextView) findViewById(R.id.roots_tv);
        SeekBar plantsSeekBar = (SeekBar) findViewById(R.id.plants_seek_bar);
        final TextView plantsTV = (TextView) findViewById(R.id.plants_tv);
        glueCheckBox = (CheckBox) findViewById(R.id.glue_CheckBox);
        clipsCheckBox = (CheckBox) findViewById(R.id.clips_CheckBox);

        //SeekBar listeners
        baseShiftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress == 0){
                    baseShiftTextView.setText("None");
                    baseShiftNum = 0;
                }else if(progress == 1){
                    baseShiftTextView.setText("Little");
                    baseShiftNum = 1;
                }else if(progress == 2){
                    baseShiftTextView.setText("A moderate amount");
                    baseShiftNum = 2;
                }else{
                    baseShiftTextView.setText("Lots");
                    baseShiftNum = 3;
                }
            }
        });
        rootsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress == 0){
                    rootsTV.setText("None");
                    rootsNum = 0;
                }else if(progress == 1){
                    rootsTV.setText("Little");
                    rootsNum = 1;
                }else if(progress == 2){
                    rootsTV.setText("A moderate amount");
                    rootsNum = 2;
                }else{
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
                if(progress == 0){
                    plantsTV.setText("None");
                    plantsNum = 0;
                }else if(progress == 1){
                    plantsTV.setText("Little");
                    plantsNum = 1;
                }else if(progress == 2){
                    plantsTV.setText("A moderate amount");
                    plantsNum = 2;
                }else{
                    plantsTV.setText("Lots");
                    plantsNum = 3;
                }
            }
        });
    }

    public void fabClicked(View fab) {
        //start activity (getIntent to save extras)
        Intent intent = getIntent();
        //update class
        intent.setClass(getApplicationContext(), Wall_Rebuilding_Estimation.class);
        //extras--for passing data
        intent.putExtra("baseShiftIndex", baseShiftNum);
        intent.putExtra("rootsNum", rootsNum);
        intent.putExtra("plantsNum", plantsNum);
        intent.putExtra("glueChecked", glueCheckBox.isChecked());
        intent.putExtra("clipsChecked", clipsCheckBox.isChecked());
        startActivity(intent);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
