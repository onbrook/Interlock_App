package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.isViewValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealing2 extends AppCompatActivity {

    private CheckBox stainCheckBox;
    private Spinner stainTypeSpinner;
    private SeekBar stainAmountSeekBar;
    private SeekBar ageSeekBar;
    private SeekBar otherCompSeekBar;
    private int otherCompNum = 0;
    private int ageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing2);
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
        //get views
        stainCheckBox = (CheckBox) findViewById(R.id.stain_checkBox);
        stainTypeSpinner = (Spinner) findViewById(R.id.stain_spinner);
        stainAmountSeekBar = (SeekBar) findViewById(R.id.percent_stain_seekBar);
        ageSeekBar = (SeekBar) findViewById(R.id.age_seekBar);
        otherCompSeekBar = (SeekBar) findViewById(R.id.other_comp_seekBar);
        final TextView percentStainTitleTextView = (TextView) findViewById(R.id.stains_percent_headline_tv);
        final TextView percentStainTextView = (TextView) findViewById(R.id.percent_stain_tv);
        final TextView ageTextView = (TextView) findViewById(R.id.age_tv);
        final TextView otherCompTextView = (TextView) findViewById(R.id.other_comp_tv);
        //disable unused views (for stains)
        stainTypeSpinner.setEnabled(false);
        stainAmountSeekBar.setEnabled(false);
        percentStainTitleTextView.setEnabled(false);
        percentStainTextView.setEnabled(false);
        //listener for when stainCheckBox is pressed
        stainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stainTypeSpinner.setEnabled(isChecked);
                stainAmountSeekBar.setEnabled(isChecked);
                percentStainTitleTextView.setEnabled(isChecked);
                percentStainTextView.setEnabled(isChecked);
                if(!isChecked)
                    stainTypeSpinner.setBackgroundResource(R.drawable.no_outline_spinner);
            }
        });
        //listener for when seek bars are moved
        stainAmountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update percentStainTextView
                String percent = progress+"%";
                percentStainTextView.setText(percent);
            }
        });
        ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress<=17){
                    seekBar.setProgress(0);
                    ageTextView.setText("Less than six months old");
                    ageNum = 0;
                }else if(progress<=33){
                    seekBar.setProgress(20);
                    ageTextView.setText("Six months old");
                    ageNum = 1;
                }else if(progress<=50){
                    seekBar.setProgress(40);
                    ageTextView.setText("One year old");
                    ageNum = 2;
                }else if(progress<=67){
                    seekBar.setProgress(60);
                    ageTextView.setText("One and a half years old");
                    ageNum = 3;
                }else if(progress<=83){
                    seekBar.setProgress(80);
                    ageTextView.setText("Two years old");
                    ageNum = 4;
                }else if(progress<=100){
                    seekBar.setProgress(100);
                    ageTextView.setText("More than two years old");
                    ageNum = 5;
                }
            }
        });
        otherCompSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress<=12){
                    seekBar.setProgress(0);
                    otherCompTextView.setText("No other complications");
                    otherCompNum = 0;
                }else if(progress<=50){
                    seekBar.setProgress(33);
                    otherCompTextView.setText("Some");
                    otherCompNum = 1;
                }else if(progress<=88){
                    seekBar.setProgress(66);
                    otherCompTextView.setText("A moderate amount");
                    otherCompNum = 2;
                }else{
                    seekBar.setProgress(100);
                    otherCompTextView.setText("Lots more");
                    otherCompNum = 3;
                }
            }
        });
        //Spinner listener
        stainTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = stainTypeSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(stainTypeSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = stainTypeSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(stainTypeSpinner);
            }
        });
    }

    public void fabClicked(View view){
        if(isViewValid(stainTypeSpinner) && stainCheckBox.isChecked()) {
            //get the intent and reset the class
            Intent intent = getIntent();
            intent.setClass(getApplicationContext(), CleaningSealingEstimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("stain_checked", stainCheckBox.isChecked());
            intent.putExtra("stain_type_index", stainTypeSpinner.getSelectedItemPosition());
            intent.putExtra("stain_type_str", stainTypeSpinner.getSelectedItem().toString());
            intent.putExtra("stain_percent", stainAmountSeekBar.getProgress());
            intent.putExtra("age_num", ageNum);
            intent.putExtra("other_comp_num", otherCompNum);
            //start activity
            startActivity(intent);
        }else if(!stainCheckBox.isChecked()){
            //get the intent and reset the class
            Intent intent = getIntent();
            intent.setClass(getApplicationContext(), CleaningSealingEstimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("stainCheckBoxChecked", stainCheckBox.isChecked());
            intent.putExtra("age", ageSeekBar.getProgress()/50);//make max is 2
            intent.putExtra("otherCompProgress", otherCompSeekBar.getProgress());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(stainTypeSpinner);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }


}
