package com.oep.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.oep.owenslaptop.interlock_app.R;

public class Interlock_Relaying_ToJob extends AppCompatActivity {
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
        final SeekBar accSlider = (SeekBar)findViewById(R.id.accSlider);
        final Spinner locationSpin = (Spinner)findViewById(R.id.locationSpin);
        final SeekBar moveSlider = (SeekBar)findViewById(R.id.moveSlider);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageCheck(locationSpin);
                if(check == true){
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_Estimate.class);
                    //save info to file here
                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
                }
            }
        });
    }
}
