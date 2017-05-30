package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill3 extends AppCompatActivity {
    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private CheckBox weedsCB;
    private TextView jointFDisplayTV;
    private SeekBar jointFillSB;
    public static String weedJointSt, hardJointSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill3);

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
        jointFillSB = (SeekBar) findViewById(R.id.jointFillSB);
        jointFDisplayTV = (TextView) findViewById(R.id.jointFDisplayTV);
        weedsCB = (CheckBox) findViewById(R.id.weedsCB);

        //setting the default values
        jointFDisplayTV.setText("Average");
        hardJointSt = "Average";

        //joint fill seekbar
        jointFillSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    jointFDisplayTV.setText("Very Soft");
                    hardJointSt = "Very Soft";
                }
                else if(progress==1){
                    //
                    jointFDisplayTV.setText("Soft");
                    hardJointSt = "Soft";
                }
                else if(progress==2){
                    //
                    jointFDisplayTV.setText("Average");
                    hardJointSt = "Average";
                }
                else if(progress==3){
                    //
                    jointFDisplayTV.setText("Hard");
                    hardJointSt = "Hard";
                }
                else{
                    //must be the last one
                    jointFDisplayTV.setText("Very Hard");
                    hardJointSt = "Very Hard";
                }
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        //getting the values
        if(weedsCB.isChecked()){
            weedJointSt = "Yes";
        }
        else{
            weedJointSt = "No";
        }

        //create a new intent (you do not get the current one because we do not need any
        // information from the home screen)
        Intent intent = new Intent(getApplicationContext(), Joint_Fill4.class);
        //this removes the animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //start activity
        startActivity(intent);
    }

    //called when the back button in the title ba is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
