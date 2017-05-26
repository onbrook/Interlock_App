package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.areViewsValid;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[2];
    private RadioButton curvedRB;
    public static String sizeJobHSt, sizeJobLSt, baseShiftSt, stepsAreSt;
    private EditText jobHeight, jobLength;
    private SeekBar baseShift;
    private TextView baseShiftDisTV;

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
        curvedRB = (RadioButton) findViewById(R.id.curvedRB);
        baseShift = (SeekBar) findViewById(R.id.baseShiftSB);
        jobHeight = (EditText) findViewById(R.id.height_input);
        jobLength = (EditText) findViewById(R.id.length_input);
        baseShiftDisTV = (TextView) findViewById(R.id.baseShiftDisplayTV);

        //adding the spinners to the array
        views[0] = jobHeight;
        views[1] = jobLength;

        baseShiftDisTV.setText("Average");

        //ease of access
        baseShift.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    baseShiftDisTV.setText("None");
                    baseShiftSt = "None";
                }
                else if(progress==1){
                    //
                    baseShiftDisTV.setText("Slight Bit");
                    baseShiftSt = "Slight Bit";
                }
                else if(progress==2){
                    //
                    baseShiftDisTV.setText("Average");
                    baseShiftSt = "Average";
                }
                else if(progress==3){
                    //
                    baseShiftDisTV.setText("Quite a Bit");
                    baseShiftSt = "Quite a Bit";
                }
                else{
                    //must be the last one
                    baseShiftDisTV.setText("A Lot");
                    baseShiftSt = "A Lot";
                }
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            sizeJobHSt = jobHeight.getText().toString();
            sizeJobLSt = jobLength.getText().toString();

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
