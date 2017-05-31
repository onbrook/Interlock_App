package com.oep.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.MenuItem;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import org.w3c.dom.Text;

public class Interlock_Relaying_ToJob extends AppCompatActivity {

    public static String accOut, locOut, moveOut;

    public static boolean pageCheck(Spinner location){
        //clearing all outlines
        ViewValidity.removeOutline(location);

        if(String.valueOf(location.getSelectedItem()).equals("Job Location")){
            ViewValidity.setupOutline(location);
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying__to_job);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }
        final SeekBar accSlider = (SeekBar)findViewById(R.id.accSlider);
        final Spinner locationSpin = (Spinner)findViewById(R.id.locationSpin);
        final SeekBar moveSlider = (SeekBar)findViewById(R.id.moveSlider);
        final TextView moveDisplay = (TextView)findViewById(R.id.moveDisplay);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);
        final TextView accDisplay = (TextView)findViewById(R.id.accDisplay);

        accSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0){
                    accDisplay.setText("Very accessible");
                }else if (progress == 1){
                    accDisplay.setText("Moderately accessible");
                }else if (progress == 2){
                    accDisplay.setText("Moderately inaccessible");
                }else{
                    accDisplay.setText("Very inaccessible");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    moveDisplay.setText("Lots of room to move");
                }else if(progress == 1){
                    moveDisplay.setText("A decent amount of room to move");
                }else if(progress == 2){
                    moveDisplay.setText("Not much room to move");
                }else{
                    moveDisplay.setText("Very little room to move");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageCheck(locationSpin);
                if(check == true){
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_Estimate.class);
                    //saving data to extras
                    accOut = "The job is... " + String.valueOf(accDisplay.getText()).toLowerCase();
                    moveOut = "There is... " + String.valueOf(moveDisplay.getText()).toLowerCase();
                    locOut = "Job location: " + String.valueOf(locationSpin.getSelectedItem());
                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
