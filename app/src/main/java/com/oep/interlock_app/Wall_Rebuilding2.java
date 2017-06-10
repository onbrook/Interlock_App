package com.oep.interlock_app;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.*;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding2 extends AppCompatActivity {
    // getting to the job layout Views
    private Spinner locationSpinner;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    SeekBar accessibilitySeekBar;
    SeekBar maneuverSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wall_rebuilding_tojob);
        // getting to the job layout Views
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        accessibilitySeekBar = (SeekBar) findViewById(R.id.accessibility_seek_bar);
        final TextView accessibilityTextView = (TextView) findViewById(R.id.accessibility_tv);
        maneuverSeekBar = (SeekBar) findViewById(R.id.maneuver_seek_bar);
        final TextView maneuverTextView = (TextView) findViewById(R.id.maneuver_tv);

        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }catch(NullPointerException ex){
                //back button not supported
            }
        }
        //spinner listeners
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = locationSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(locationSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = locationSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(locationSpinner);
            }
        });
        //Seek Bar Listeners
        accessibilitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                accessibilityTextView.setText(getResources().getStringArray(R.array.accessibility)[progress]);
            }
        });
        maneuverSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                maneuverTextView.setText(getResources().getStringArray(R.array.maneuver)[progress]);
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
                    intent = new Intent(Wall_Rebuilding2.this, HomeScreen.class);
                }
                else if(position==1){
                    intent = new Intent(Wall_Rebuilding2.this, HelpPage.class);
                }
                else if(position==2){
                    intent = new Intent(Wall_Rebuilding2.this, EstimationPage.class);
                }
                else if(position==3){
                    intent = new Intent(Wall_Rebuilding2.this, DatabaseManagement.class);
                }
                else if(position==4){
                    intent = new Intent(Wall_Rebuilding2.this, EnterDatabaseIdActivity.class);
                }
                else if(position==5){//this will only be true if the user is owner
                    intent = new Intent(Wall_Rebuilding2.this, ActivityDatabaseAccounts.class);
                }
                ILDialog.showExitDialogWarning(activity, intent);
            }
        });
    }

    public void fabClicked(View view){
        if(isViewValid(locationSpinner)) {
            //start activity (getIntent to save extras)
            Intent intent = getIntent();
            //update class
            intent.setClass(getApplicationContext(), Wall_Rebuilding_Estimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("locationIndex", locationSpinner.getSelectedItemPosition());
            intent.putExtra("accessIndex", accessibilitySeekBar.getProgress());
            intent.putExtra("maneuverIndex", maneuverSeekBar.getProgress());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(locationSpinner);
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
}
