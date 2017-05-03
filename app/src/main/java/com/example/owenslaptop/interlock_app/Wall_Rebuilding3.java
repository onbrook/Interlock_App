package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static com.example.owenslaptop.interlock_app.methods.areViewsValid;
import static com.example.owenslaptop.interlock_app.methods.updateViewValidity;


public class Wall_Rebuilding3 extends AppCompatActivity {
    Spinner baseShiftSpinner;
    View[] views = new View[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_complications);
        baseShiftSpinner = (Spinner) findViewById(R.id.baseShift_spinner);
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
                if(index != 1)
                    updateViewValidity(views);
            }
        });
    }

    public void fabClicked(View fab) {
        if (areViewsValid(views))
            startActivity(new Intent(Wall_Rebuilding3.this, HomeScreen.class));
        else{
            updateViewValidity(views);
        }
    }
}
