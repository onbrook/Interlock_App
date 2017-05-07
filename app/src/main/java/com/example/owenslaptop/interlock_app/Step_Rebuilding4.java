package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding4 extends AppCompatActivity {

    //setting up the textview for display of the input and the output
    private TextView sizeHDisplay, sizeLDisplay, baseShiftDisplay, stepsAreDisplay, locationDisplay, easeAccessDisplay, roomManDisplay,
            stepsGluedDisplay, treeRootsDisplay, clipsDisplay, hardLineDisplay, finalTimeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding4);

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

        //setting up the textview for display of the input and the output
        sizeHDisplay = (TextView) findViewById(R.id.sizeHDisplay);
        sizeLDisplay = (TextView) findViewById(R.id.sizeLDisplay);
        baseShiftDisplay = (TextView) findViewById(R.id.baseShiftDisplay);
        stepsAreDisplay = (TextView) findViewById(R.id.stepsAreDisplay);
        locationDisplay = (TextView) findViewById(R.id.locationDisplay);
        easeAccessDisplay = (TextView) findViewById(R.id.easeAccessDisplay);
        roomManDisplay = (TextView) findViewById(R.id.roomManDisplay);
        stepsGluedDisplay = (TextView) findViewById(R.id.stepsGluedDisplay);
        treeRootsDisplay = (TextView) findViewById(R.id.treeRootsDisplay);
        clipsDisplay = (TextView) findViewById(R.id.clipsDisplay);
        hardLineDisplay = (TextView) findViewById(R.id.hardLineDisplay);
        finalTimeDisplay = (TextView) findViewById(R.id.finalTimeDisplay);

        //adding the variables to the textviews for display
        sizeHDisplay.setText(Step_Rebuilding.sizeJobHSt);
        sizeLDisplay.setText(Step_Rebuilding.sizeJobLSt);
        baseShiftDisplay.setText(Step_Rebuilding.baseShiftSt);
        stepsAreDisplay.setText(Step_Rebuilding.stepsAreSt);
        locationDisplay.setText(Step_Rebuilding2.locationSt);
        easeAccessDisplay.setText(Step_Rebuilding2.easeAccSt);
        roomManDisplay.setText(Step_Rebuilding2.roomManSt);
        stepsGluedDisplay.setText(Step_Rebuilding3.stepsGlueSt);
        treeRootsDisplay.setText(Step_Rebuilding3.rootsSt);
        clipsDisplay.setText(Step_Rebuilding3.clipsSt);
        hardLineDisplay.setText(Step_Rebuilding3.hardLineSt);
        finalTimeDisplay.setText("??");
    }
    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(Step_Rebuilding4.this, Step_Rebuilding3.class));
        return true;
    }
    //when the FAB is clicked
    public void fabClicked(View view){
        //create a new intent (you do not get the current one because we do not need any
        // information from the home screen)
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        //this removes the animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //extras--for passing data
        //start activity
        startActivity(intent);

    }
}
