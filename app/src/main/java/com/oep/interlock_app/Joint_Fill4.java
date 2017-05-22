package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

public class Joint_Fill4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill4);

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
        TextView sizeJobWTV = (TextView) findViewById(R.id.sizeJobWTV);
        TextView sizeJobLTV = (TextView) findViewById(R.id.sizeJobLTV);
        TextView sizeJointTV = (TextView) findViewById(R.id.sizeJointsTV);
        TextView patternTV = (TextView) findViewById(R.id.patternTV);
        TextView locationTV = (TextView) findViewById(R.id.locationTV);
        TextView easeTV = (TextView) findViewById(R.id.easeTV);
        TextView manRoomTV = (TextView) findViewById(R.id.manRoomTV);
        TextView whatArrTV = (TextView) findViewById(R.id.whatArrTV);
        TextView weedJointTV = (TextView) findViewById(R.id.weedJointTV);
        TextView hardJointTV = (TextView) findViewById(R.id.hardJointTV);
        TextView finalEstTV = (TextView) findViewById(R.id.finalEstTV);

        //adding the variables to the textviews for display
        sizeJobWTV.setText(Joint_Fill.widInputSt);
        sizeJobLTV.setText(Joint_Fill.lenInputSt);
        sizeJointTV.setText(Joint_Fill.sizeJointSt);
        patternTV.setText(Joint_Fill.patternSt);
        locationTV.setText(Joint_Fill2.locationSt);
        easeTV.setText(Joint_Fill2.easeSt);
        manRoomTV.setText(Joint_Fill2.roomManSt);
        whatArrTV.setText(Joint_Fill2.whatArrSt);
        weedJointTV.setText(Joint_Fill3.weedJointSt);
        hardJointTV.setText(Joint_Fill3.hardJointSt);
        finalEstTV.setText("??");
    }
    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(Joint_Fill4.this, Step_Rebuilding3.class));
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
