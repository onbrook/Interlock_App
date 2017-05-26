package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding3 extends AppCompatActivity {


    //setting up the spinners and the array
    private View[] views = new View[1];
    private Spinner stepsGluedSp;
    private TextView rootsDisTV;
    public static String stepsGlueSt, rootsSt, clipsSt, hardLineSt;
    private SeekBar rootsSB;
    private CheckBox hardLineCB, clipsCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding3);

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

        //setting up the GUI componenets
        stepsGluedSp = (Spinner) findViewById(R.id.stepsGluedSp);
        rootsDisTV = (TextView) findViewById(R.id.rootsValueDisplayTV);
        rootsSB = (SeekBar) findViewById(R.id.treeRootsSB);
        hardLineCB = (CheckBox) findViewById(R.id.hardLineCB);
        clipsCB = (CheckBox) findViewById(R.id.clipsCB);

        //setting the default values as output
        rootsDisTV.setText("Average");


        //adding the spinners to the array
        views[0] = stepsGluedSp;

        rootsSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    rootsDisTV.setText("None");
                    rootsSt = "None";
                }
                else if(progress==1){
                    //
                    rootsDisTV.setText("Average");
                    rootsSt = "Average";
                }
                else{
                    //must be the last one
                    rootsDisTV.setText("A lot");
                    rootsSt = "A lot";
                }
            }
        });

        //when the second size spinner is clicked
        stepsGluedSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = stepsGluedSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(stepsGluedSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = stepsGluedSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(stepsGluedSp);
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(ViewValidity.areViewsValid(views)) {
            stepsGlueSt = stepsGluedSp.getSelectedItem().toString();
            if(clipsCB.isChecked()){
                clipsSt = "Yes";
            }
            else{
                clipsSt = "No";
            }
            if(hardLineCB.isChecked()){
                hardLineSt = "Yes";
            }
            else{
                hardLineSt = "No";
            }

            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Step_Rebuilding4.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("gluedIndex", stepsGluedSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            ViewValidity.updateViewValidity(views);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(Step_Rebuilding3.this, Step_Rebuilding2.class));
        return true;
    }
}
