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

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealingEstimation extends AppCompatActivity {

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private List<Object> data = new ArrayList<>();
    private EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing_estimation);
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

        estimationSheet = new EstimationSheet(EstimationSheet.CLEANING_SEALING_ID, this);

        // get TextViews
        TextView dimensionsOut = (TextView) findViewById(R.id.dimensions_out);
        TextView angleOut = (TextView) findViewById(R.id.angle_out);
        TextView stainOut = (TextView) findViewById(R.id.stain_out);
        TextView stainTypeOut = (TextView) findViewById(R.id.stain_type_out);
        TextView stainPercentOut = (TextView) findViewById(R.id.stain_percent_out);
        TextView ageOut = (TextView) findViewById(R.id.age_out);
        TextView otherCompOut = (TextView) findViewById(R.id.comp_out);
        // get the data that was filled out in the layouts and put it in the TextViews
        Bundle extras = getIntent().getExtras();
        double heightDouble = extras.getDouble("height_double");
        double lengthDouble = extras.getDouble("length_double");
        dimensionsOut.setText(heightDouble+"ft x "+lengthDouble+"ft");
        int angleId = extras.getInt("angle_id");
        if(angleId==R.id.angle_horizontal_radio_button)
            angleOut.setText(getString(R.string.horizontal));
        else if(angleId == R.id.angle_vertical_radio_button)
            angleOut.setText(getString(R.string.vertical));
        else
            System.err.println("invalid angle id. the angle id is "+angleId);
        boolean stainChecked = extras.getBoolean("stain_checked");
        int stainTypeIndex = extras.getInt("stain_type_index");
        String stainTypeStr = extras.getString("stain_type_str");
        int stainAmount = extras.getInt("stain_amount");
        if(stainChecked) {
            stainOut.setText("Yes");
            stainTypeOut.setText(stainTypeStr);
            stainPercentOut.setText(getResources().getStringArray(R.array.amount_increasing)[stainAmount]);
        }else {// stain not checked
            stainOut.setText("No");
            stainTypeOut.setText("--");
            stainPercentOut.setText("--");
        }
        boolean old = extras.getBoolean("old");
        if(old) {
            ageOut.setText("Yes");
        }else {
            ageOut.setText("No");
        }
        int otherCompNum = extras.getInt("other_comp_num");
        otherCompOut.setText(getResources().getStringArray(R.array.amount_increasing)[otherCompNum]);

        //package data for estimation
        data.add(heightDouble);
        data.add(lengthDouble);
        data.add(angleId);
        data.add(stainChecked);
        data.add(stainTypeIndex);
        data.add(stainAmount);
        data.add(old);
        data.add(otherCompNum);

        // make estimation
        estimationSheet.startEstimation(data, new EstimationListener() {
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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position==0){
                    intent = new Intent(getApplicationContext(), HomeScreen.class);
                }
                else if(position==1){
                    intent = new Intent(getApplicationContext(), HelpPage.class);
                }
                else if(position==2){
                    intent = new Intent(getApplicationContext(), EstimationPage.class);
                }
                else if(position==3){
                    intent = new Intent(getApplicationContext(), DatabaseManagement.class);
                }
                else if(position==4){
                    intent = new Intent(getApplicationContext(), EnterDatabaseIdActivity.class);
                }
                else if(position==5){//this will only be true if the user is owner
                    intent = new Intent(getApplicationContext(), ActivityDatabaseAccounts.class);
                }
                ILDialog.showExitDialogSave(activity, intent, estimationSheet, data);
            }
        });
    }

    public void fabClicked(View fab){
        estimationSheet.startAddingEstimation(data, new AddEstimationListener() {
            @Override
            public void whenFinished(boolean success) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        } else {
            String[] osArray = { "Home Screen", "Help!",  "New Estimation", "Database Management", "Database Setup" };
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
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
