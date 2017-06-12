package com.oep.interlock_app;

import android.app.Activity;
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
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Interlock_Relaying_Estimate extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private List<Object> data = new ArrayList<>();
    private EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying__estimate);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }

        estimationSheet = new EstimationSheet(EstimationSheet.INTERLOCK_RELAYING_ID, this);

        //get views
        TextView heightOut = (TextView) findViewById(R.id.height_out);
        TextView lengthOut = (TextView) findViewById(R.id.length_out);
        TextView patternOut = (TextView) findViewById(R.id.pattern_out);
        TextView shapeOut = (TextView) findViewById(R.id.shape_out);
        TextView edgingOut =(TextView) findViewById(R.id.edging_out);
        TextView sunkAmtOut = (TextView) findViewById(R.id.sunk_amt_out);
        TextView jointHardnessOut = (TextView) findViewById(R.id.joint_hardness_out);
        TextView paverSizeOut = (TextView) findViewById(R.id.paver_size_out);
        TextView weedsOut = (TextView) findViewById(R.id.weeds_out);
        TextView accessibilityOut = (TextView) findViewById(R.id.accessibility_out);
        TextView locationOut = (TextView) findViewById(R.id.location_out);
        TextView roomOut = (TextView) findViewById(R.id.room_out);

        //fill views
        heightOut.setText(Interlock_Relaying.widthOut + "ft");
        lengthOut.setText(Interlock_Relaying.lengthOut + "ft");
        patternOut.setText(Interlock_Relaying.ptrnOut);
        shapeOut.setText(Interlock_Relaying.shapeOut);
        edgingOut.setText(Interlock_Relaying.edgeOut);
        sunkAmtOut.setText(Interlock_Relaying_Complications.sunkOut);
        jointHardnessOut.setText(Interlock_Relaying_Complications.fillOut);
        paverSizeOut.setText(Interlock_Relaying_Complications.paveSizeOut);
        weedsOut.setText(Interlock_Relaying_Complications.weedOut);
        accessibilityOut.setText(Interlock_Relaying_ToJob.accOut);
        locationOut.setText(Interlock_Relaying_ToJob.locOut);
        roomOut.setText(Interlock_Relaying_ToJob.moveOut);

        // package data for estimation
        data.add(Double.parseDouble(Interlock_Relaying.widthOut));
        data.add(Double.parseDouble(Interlock_Relaying.lengthOut));
        data.add(Interlock_Relaying.ptrnOut);
        data.add(Interlock_Relaying.shapeOut);
        data.add(Interlock_Relaying.edgeOut);
        data.add(Interlock_Relaying_Complications.sunkOut);
        data.add(Interlock_Relaying_Complications.fillOut);
        data.add(Interlock_Relaying_Complications.paveSizeOut);
        data.add(Interlock_Relaying_Complications.weedOut);
        data.add(Interlock_Relaying_ToJob.accOut);
        data.add(Interlock_Relaying_ToJob.locOut);
        data.add(Interlock_Relaying_ToJob.moveOut);

        //make estimation
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

    public void fabClicked(View view){
        estimationSheet.startAddingEstimation(data, new AddEstimationListener() {
            @Override
            public void whenFinished(boolean success) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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
