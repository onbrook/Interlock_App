package com.oep.interlock_app;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

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
        locationOut.setText(getResources().getStringArray(R.array.location)[locationIndex]);
        accessibilityOut.setText(getResources().getStringArray(R.array.accessibility)[accessIndex]);
        roomOut.setText(getResources().getStringArray(R.array.maneuver)[maneuverIndex]);
        heightOut.setText(heightInput+"ft");
        lengthOut.setText(lengthInput+"ft");
        switch (straightCurvedNum){
            case 0:
                shapeOut.setText(getString(R.string.curved));
                break;
            case 1:
                shapeOut.setText(getString(R.string.straight));
                break;
        }
        if(lineChecked)
            hardLineOut.setText("Yes");
        else
            hardLineOut.setText("No");
        baseShiftOut.setText(getResources().getStringArray(R.array.amount_increasing)[baseShiftIndex]);

        switch (rootsNum){
            case 0:
                rootsOut.setText("None");
                break;
            case 1:
                rootsOut.setText("Some");
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
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.home_FAB);
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
                        String hoursStr;
                        switch(hours){
                            case 1:
                                hoursStr = "hour";
                                break;
                            default:
                                hoursStr = "hours";
                                break;
                        }
                        Snackbar snackbar = Snackbar.make(fab, "Final Estimate: " + hours + " "+hoursStr+" and " + minute + " minutes.", Snackbar.LENGTH_INDEFINITE);
                        View mView = snackbar.getView();
                        TextView textView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        else
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        snackbar.show();
                    } else {
                        if (minute >= 30)
                            hours++;
                        String hoursStr;
                        switch(hours){
                            case 1:
                                hoursStr = "hour";
                                break;
                            default:
                                hoursStr = "hours";
                                break;
                        }
                        Snackbar snackbar = Snackbar.make(fab, "Final Estimate: " + hours + " "+hoursStr+".", Snackbar.LENGTH_INDEFINITE);
                        View mView = snackbar.getView();
                        TextView textView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        else
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        snackbar.show();
                    }
                }
            }
        });

        //code to use the drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position==0){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, AboutPage.class);
                }
                else if(position==1){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, HelpPage.class);
                }
                else if(position==2){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, HomeScreen.class);
                }
                else if(position==3){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, EstimationPage.class);
                }
                else if(position==4){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, DatabaseManagement.class);
                }
                else if(position==5){
                    intent = new Intent(Wall_Rebuilding_Estimation.this, EnterDatabaseIdActivity.class);
                }
                else if(position==6){//this will only be true if the user is owner
                    intent = new Intent(Wall_Rebuilding_Estimation.this, ActivityDatabaseAccounts.class);
                }
                ILDialog.showExitDialogSave(activity, intent, wallRebuildingSheet, data);
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
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        else
            mDrawerLayout.openDrawer(Gravity.LEFT);
        return true;
    }


    private void addDrawerItems(){
        // Only have the "Database Permissions" if the user owns the database
        EstimationSheet estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if(estimationSheet.isUserOwner()) {
            String[] osArray = { "About", "Help!", "Home Screen", "New Estimation", "Database Management", "Database Setup", "Database Permissions"};
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        } else {
            String[] osArray = { "About", "Help!", "Home Screen", "New Estimation", "Database Management", "Database Setup" };
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        }
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.syncState();
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
