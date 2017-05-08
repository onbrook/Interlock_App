package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */
    //setting up the spinners and the array
    private View[] views = new View[3];
    private Spinner sizeJobSp;
    private Spinner sizeJob2Sp;
    private Spinner amountShiftSp;
    private RadioButton straightRB, curvedRB;
    public static String sizeJobHSt, sizeJobLSt, baseShiftSt, stepsAreSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding);

        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }

        //setting up the GUI components
        straightRB = (RadioButton) findViewById(R.id.straightRB);
        curvedRB = (RadioButton) findViewById(R.id.curvedRB);
        sizeJobSp = (Spinner) findViewById(R.id.sizeOfJobSp);
        sizeJob2Sp = (Spinner) findViewById(R.id.sizeOfJob2Sp);
        amountShiftSp = (Spinner) findViewById(R.id.amountBaseShiftSp);

        //creating the arrays to hold the spinner objects
        final String[] sizeArr = {"Size of the job (l)", "Shorter", "Average", "Longer"};
        final String[] size2Arr = {"Size of the job (h)", "Lower", "Average", "Higher", "Over 4 feet"};
        final String[] baseShiftArr = {"Amount of base shift", "None", "Lower"};

        //adding the spinners to the array
        views[0] = sizeJobSp;
        views[1] = sizeJob2Sp;
        views[2] = amountShiftSp;

        //setting the options to the spinners
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sizeArr);
        sizeJobSp.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, size2Arr);
        sizeJob2Sp.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, baseShiftArr);
        amountShiftSp.setAdapter(adapter3);

        //when the size spinner is clicked
        sizeJobSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = sizeJobSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJobSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = sizeJobSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJobSp);
            }
        });

        //when the second size spinner is clicked
        sizeJob2Sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = sizeJob2Sp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJob2Sp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = sizeJob2Sp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJob2Sp);
            }
        });

        //when the amount shift is clicked
        amountShiftSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = amountShiftSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(amountShiftSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = amountShiftSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(amountShiftSp);
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            sizeJobHSt = sizeJobSp.getSelectedItem().toString();
            sizeJobLSt = sizeJob2Sp.getSelectedItem().toString();
            baseShiftSt = amountShiftSp.getSelectedItem().toString();
            boolean curvedB =  curvedRB.isSelected();
            if(curvedB){
                stepsAreSt = "Curved";
            }
            else{
                //must be straight
                stepsAreSt = "Straight";
            }
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Step_Rebuilding2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("sizeIndex", sizeJobSp.getSelectedItemPosition());
            intent.putExtra("size2Index", sizeJob2Sp.getSelectedItemPosition());
            intent.putExtra("shiftIndex", amountShiftSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(views);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }
}
