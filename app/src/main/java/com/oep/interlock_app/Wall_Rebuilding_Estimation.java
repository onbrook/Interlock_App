package com.oep.interlock_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding_Estimation extends AppCompatActivity {
    EstimationSheet wallRebuildingSheet;
    List<Object> data = new ArrayList<>();
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
        TextView heightOut = (TextView) findViewById(R.id.height_out);
        TextView lengthOut = (TextView) findViewById(R.id.length_out);
        TextView shapeOut = (TextView) findViewById(R.id.shape_out);
        TextView hardLineOut = (TextView) findViewById(R.id.hard_line_out);
        TextView baseShiftOut = (TextView) findViewById(R.id.base_shift_out);
        TextView rootsOut = (TextView) findViewById(R.id.roots_out);
        TextView glueOut = (TextView) findViewById(R.id.glue_out);
        TextView clipsOut = (TextView) findViewById(R.id.clips_out);
        TextView plantsOut = (TextView) findViewById(R.id.plants_out);
        final TextView finalEstimate = (TextView) findViewById(R.id.final_out);
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
        //add data to the data list
        //NOTE: the list must be in the same order as the data in the Google Sheet
        data.add(heightInput);
        data.add(lengthInput);
        data.add(baseShiftIndex);
        data.add(maneuverIndex);
        data.add(accessIndex);
        data.add(locationIndex);
        data.add(straightCurvedNum);
        data.add(rootsNum);
        data.add(plantsNum);
        data.add(lineChecked);
        data.add(glueChecked);
        data.add(clipsChecked);
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
        heightOut.setText(heightInput+"ft");
        lengthOut.setText(lengthInput+"ft");
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

        final Activity activity = this;
        wallRebuildingSheet = new EstimationSheet(EstimationSheet.WALL_REBUILDING_ID, this);
        wallRebuildingSheet.startEstimation(data, new EstimationListener() {
            @Override
            public void whenFinished(boolean success, boolean accurate, Double totalHours) {
                if(success) {
                    int hours = totalHours.intValue();
                    int minute = doubleToInt((totalHours - hours) * 60);
                    if (accurate) {
                        if (minute <= 7)
                            minute = 0;
                        else if (minute <= 22)
                            minute = 15;
                        else if (minute <= 37)
                            minute = 30;
                        else if (minute <= 52)
                            minute = 45;
                        else {
                            minute = 0;
                            hours++;
                        }
                        finalEstimate.setText("Final Estimate: " + hours + " hours and " + minute + " minutes.");
                    } else {
                        if (minute >= 30)
                            hours++;
                        Toast.makeText(activity, "Attention: this estimation is likely not very accurate.", Toast.LENGTH_LONG).show();
                        finalEstimate.setText("Final Estimate: " + hours + " hours.");
                    }
                }
            }
        });

    }

    private int doubleToInt(Double d){
        return d.intValue();
    }

    public void fabClicked(View fab){
        wallRebuildingSheet.startAddingEstimation(data, new AddEstimationListener() {
            @Override
            public void whenFinished(boolean success) {
                startActivity(new Intent(Wall_Rebuilding_Estimation.this, HomeScreen.class));
            }
        });
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wallRebuildingSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, wallRebuildingSheet);
    }
}
