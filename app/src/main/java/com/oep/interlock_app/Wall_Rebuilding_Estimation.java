package com.oep.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding_Estimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_estimation);
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
        //get TextViews
        TextView locationOut = (TextView) findViewById(R.id.location_out);
        TextView accessibilityOut = (TextView) findViewById(R.id.accessibility_out);
        TextView roomOut = (TextView) findViewById(R.id.room_out);
        TextView dimensionsOut = (TextView) findViewById(R.id.dimensions_out);
        TextView shapeOut = (TextView) findViewById(R.id.shape_out);
        TextView hardLineOut = (TextView) findViewById(R.id.hard_line_out);
        TextView baseShiftOut = (TextView) findViewById(R.id.base_shift_out);
        TextView rootsOut = (TextView) findViewById(R.id.roots_out);
        TextView glueOut = (TextView) findViewById(R.id.glue_out);
        TextView clipsOut = (TextView) findViewById(R.id.clips_out);
        TextView plantsOut = (TextView) findViewById(R.id.plants_out);
        TextView finalEstimate = (TextView) findViewById(R.id.final_out);
        //get the data that was filled out in the layouts
        Bundle extras = getIntent().getExtras();
        int locationIndex = extras.getInt("locationIndex");
        int accessIndex = extras.getInt("accessIndex");
        int maneuverIndex = extras.getInt("maneuverIndex");
        double heightInput = extras.getDouble("heightInput");
        double lengthInput = extras.getDouble("lengthInput");
        boolean lineChecked = extras.getBoolean("lineChecked");
        int straightCurvedNum = extras.getInt("straightCurvedNum");
        int baseShiftIndex = extras.getInt("baseShiftIndex");
        int rootsNum = extras.getInt("rootsNum");
        int plantsNum = extras.getInt("plantsNum");
        boolean glueChecked = extras.getBoolean("glueChecked");
        boolean clipsChecked = extras.getBoolean("clipsChecked");
        //put data into the TextViews
        switch (locationIndex){
            case 1:
                locationOut.setText(getResources().getStringArray(R.array.location)[1]);
                break;
            case 2:
                locationOut.setText(getResources().getStringArray(R.array.location)[2]);
                break;
            case 3:
                locationOut.setText(getResources().getStringArray(R.array.location)[3]);
                break;
        }
        switch (accessIndex){
            case 1:
                accessibilityOut.setText(getResources().getStringArray(R.array.accessibility)[1]);
                break;
            case 2:
                accessibilityOut.setText(getResources().getStringArray(R.array.accessibility)[2]);
                break;
            case 3:
                accessibilityOut.setText(getResources().getStringArray(R.array.accessibility)[3]);
                break;
        }
        switch (maneuverIndex){
            case 1:
                roomOut.setText(getResources().getStringArray(R.array.accessibility)[1]);
                break;
            case 2:
                roomOut.setText(getResources().getStringArray(R.array.accessibility)[2]);
                break;
            case 3:
                roomOut.setText(getResources().getStringArray(R.array.accessibility)[3]);
                break;
        }
        dimensionsOut.setText(heightInput+"ft x "+lengthInput+"ft");
        switch (straightCurvedNum){
            case 0:
                shapeOut.setText("Curved");
                break;
            case 1:
                shapeOut.setText("Straight");
                break;
        }
        if(lineChecked)
            hardLineOut.setText("Yes");
        else
            hardLineOut.setText("No");
        switch (baseShiftIndex){
            case 1:
                baseShiftOut.setText(getResources().getStringArray(R.array.base_shift)[1]);
                break;
            case 2:
                baseShiftOut.setText(getResources().getStringArray(R.array.base_shift)[2]);
                break;
            case 3:
                baseShiftOut.setText(getResources().getStringArray(R.array.base_shift)[3]);
                break;
            case 4:
                baseShiftOut.setText(getResources().getStringArray(R.array.base_shift)[4]);
                break;
        }
        switch (rootsNum){
            case 0:
                rootsOut.setText("None");
                break;
            case 1:
                roomOut.setText("Some");
                break;
            case 2:
                rootsOut.setText("Moderately");
                break;
            case 3:
                rootsOut.setText("Lots");
                break;
        }
        switch (plantsNum){
            case 0:
                plantsOut.setText("None");
                break;
            case 1:
                plantsOut.setText("Some");
                break;
            case 2:
                plantsOut.setText("Moderate");
                break;
            case 3:
                plantsOut.setText("Lots");
                break;
        }
        if (glueChecked)
            glueOut.setText("Yes");
        else
            glueOut.setText("No");
        if (clipsChecked)
            clipsOut.setText("Yes");
        else
            clipsOut.setText("No");
        finalEstimate.setText("Final Estimate:\n      0 hours and 0 minuets");
        EstimationSheet wallRebuildingSheet = new EstimationSheet(EstimationSheet.WALL_REBUILDING_ID, this);
        wallRebuildingSheet.getTimeEstimation();
    }

    public void fabClicked(View fab){
        startActivity(new Intent(Wall_Rebuilding_Estimation.this, HomeScreen.class));
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
