package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;


public class Wall_Rebuilding3 extends AppCompatActivity {
    Spinner baseShiftSpinner;
    CheckBox rootsCheckBox;
    CheckBox glueCheckBox;
    CheckBox clipsCheckBox;
    CheckBox plantsCheckBox;
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
        rootsCheckBox = (CheckBox) findViewById(R.id.roots_CheckBox);
        glueCheckBox = (CheckBox) findViewById(R.id.glue_CheckBox);
        clipsCheckBox = (CheckBox) findViewById(R.id.clips_CheckBox);
        plantsCheckBox = (CheckBox) findViewById(R.id.plants_CheckBox);
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
    }

    public void fabClicked(View fab) {
        if (areViewsValid(views)) {
            //start activity (getIntent to save extras)
            Intent intent = getIntent();
            //update class
            intent.setClass(getApplicationContext(), Wall_Rebuilding_Estimation.class);
            //extras--for passing data
            intent.putExtra("baseShiftIndex", baseShiftSpinner.getSelectedItemPosition());
            intent.putExtra("rootsChecked", rootsCheckBox.isChecked());
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
