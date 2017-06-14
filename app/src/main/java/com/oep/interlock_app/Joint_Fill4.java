package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Joint_Fill4 extends AppCompatActivity {

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private EstimationSheet estimationSheet;
    final List<Object> dataSet = new ArrayList<>();

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

        //package data for estimation
        dataSet.add(Double.parseDouble(Joint_Fill.widInputSt));
        dataSet.add(Double.parseDouble(Joint_Fill.lenInputSt));
        dataSet.add(Joint_Fill.sizeJointSt);
        dataSet.add(Joint_Fill.patternSt);
        dataSet.add(Joint_Fill2.locationSt);
        dataSet.add(Joint_Fill2.easeSt);
        dataSet.add(Joint_Fill2.roomManSt);
        dataSet.add(Joint_Fill2.whatArrSt);
        dataSet.add(Joint_Fill3.weedJointSt);
        dataSet.add(Joint_Fill3.hardJointSt);

        estimationSheet = new EstimationSheet(EstimationSheet.JOINT_FILL_ID, this);

        // make estimation
        estimationSheet.startEstimation(dataSet, new EstimationListener() {
            @Override
            public void whenFinished(boolean success, boolean accurate, Double estimatedHours) {
                if(success) {
                    int hours = estimatedHours.intValue();
                    Double doubleMin = ((estimatedHours - hours) * 60);
                    int minute = doubleMin.intValue();
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
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Final Estimate: " + hours + " "+hoursStr+" and " + minute + " minutes.", Snackbar.LENGTH_INDEFINITE);
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
                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Final Estimate: " + hours + " "+hoursStr+".", Snackbar.LENGTH_INDEFINITE);
                        View mView = snackbar.getView();
                        TextView textView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        else
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        snackbar.show();
                    }
                } else {
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "An estimation could not be made.", Snackbar.LENGTH_INDEFINITE);
                    View mView = snackbar.getView();
                    TextView textView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
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

        final Activity activity = this;

        estimationSheet = new EstimationSheet(EstimationSheet.JOINT_FILL_ID, this);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position==0){
                    intent = new Intent(Joint_Fill4.this, HomeScreen.class);
                }
                else if(position==1){
                    intent = new Intent(Joint_Fill4.this, HelpPage.class);
                }
                else if(position==2){
                    intent = new Intent(Joint_Fill4.this, EstimationPage.class);
                }
                else if(position==3){
                    intent = new Intent(Joint_Fill4.this, DatabaseManagement.class);
                }
                else if(position==4){
                    intent = new Intent(Joint_Fill4.this, EnterDatabaseIdActivity.class);
                }
                else if(position==5){//this will only be true if the user is owner
                    intent = new Intent(Joint_Fill4.this, ActivityDatabaseAccounts.class);
                }
                // Check if the user is sure that they want to leave before saving
                ILDialog.showExitDialogSave(activity, intent, estimationSheet, dataSet);
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
        ArrayAdapter<String> mAdapter;
        if(estimationSheet.isUserOwner()) {
            String[] osArray = {"Home Screen", "Help!",  "New Estimation", "Database Management", "Database Setup", "Database Permissions"};
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        } else {
            String[] osArray = { "Home Screen", "Help!",  "New Estimation", "Database Management", "Database Setup" };
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

    //when the FAB is clicked
    public void fabClicked(View view) {
        estimationSheet.startAddingEstimation(dataSet, new AddEstimationListener() {
            @Override
            public void whenFinished(boolean success) {
                //create a new intent (you do not get the current one because we do not need any
                // information from the home screen)
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                //start activity
                startActivity(intent);
                if(success){
                    //add alarm for in 7 days
                    Calendar alarm = Calendar.getInstance();
                    alarm.add(Calendar.DATE, 7);

                    Intent alarmIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 001, alarmIntent, 0);

                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        estimationSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, estimationSheet);
    }
}
