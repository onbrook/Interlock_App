package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill3 extends AppCompatActivity {
    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private RadioButton wJYes, hJYes;
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
        wJYes = (RadioButton) findViewById(R.id.weedY);
        hJYes = (RadioButton) findViewById(R.id.jointY);
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        //getting the values
        boolean weedJoint = wJYes.isSelected();
        boolean hardJoint = hJYes.isSelected();
        if(weedJoint){
            weedJointSt = "Yes";
        }
        else{
            weedJointSt = "No";
        }
        if(hardJoint){
            hardJointSt = "Yes";
        }
        else{
            hardJointSt = "No";
        }
        //create a new intent (you do not get the current one because we do not need any
        // information from the home screen)
        Intent intent = new Intent(getApplicationContext(), Joint_Fill4.class);
        //this removes the animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //start activity
        startActivity(intent);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }
}
