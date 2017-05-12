package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing2);
        //get views
        stainCheckBox = (CheckBox) findViewById(R.id.stain_checkBox);
        stainTypeSpinner = (Spinner) findViewById(R.id.stain_spinner);
        stainAmountSeekBar = (SeekBar) findViewById(R.id.percent_stain_seekBar);
        ageSeekBar = (SeekBar) findViewById(R.id.age_seekBar);
        otherCompSeekBar = (SeekBar) findViewById(R.id.other_comp_seekBar);
        final TextView percentStainTextView = (TextView) findViewById(R.id.percent_stain_tv);
        final TextView ageTextView = (TextView) findViewById(R.id.age_tv);
        final TextView otherCompTextView = (TextView) findViewById(R.id.other_comp_tv);
        //disable unused views (for stains)
        stainTypeSpinner.setEnabled(false);
        stainAmountSeekBar.setEnabled(false);
        //listener for when stainCheckBox is pressed
        stainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stainTypeSpinner.setEnabled(isChecked);
                stainAmountSeekBar.setEnabled(isChecked);
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
                }else if(progress<=33){
                    seekBar.setProgress(20);
                    ageTextView.setText("Six months old");
                }else if(progress<=50){
                    seekBar.setProgress(40);
                    ageTextView.setText("One year old");
                }else if(progress<=67){
                    seekBar.setProgress(60);
                    ageTextView.setText("One and a half years old");
                }else if(progress<=83){
                    seekBar.setProgress(80);
                    ageTextView.setText("Two years old");
                }else if(progress<=100){
                    seekBar.setProgress(100);
                    ageTextView.setText("More than two years old");
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
                }else if(progress<=50){
                    seekBar.setProgress(33);
                    otherCompTextView.setText("Some");
                }else if(progress<=88){
                    seekBar.setProgress(66);
                    otherCompTextView.setText("A moderate amount");
                }else{
                    seekBar.setProgress(100);
                    otherCompTextView.setText("Lots more");
                }
            }
        });
    }

    public void fabClicked(View view){
        if(isViewValid(stainTypeSpinner) && stainCheckBox.isChecked()) {
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), CleaningSealingEstimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("stainCheckBoxChecked", stainCheckBox.isChecked());
            intent.putExtra("stainTypeIndex", stainTypeSpinner.getSelectedItemPosition());
            intent.putExtra("stainAmountProgress", stainAmountSeekBar.getProgress());
            intent.putExtra("age", ageSeekBar.getProgress()/50);//make max 2
            intent.putExtra("otherCompProgress", otherCompSeekBar.getProgress());
            //start activity
            startActivity(intent);
        }else if(!stainCheckBox.isChecked()){
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), CleaningSealingEstimation.class);
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
