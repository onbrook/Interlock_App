package com.oep.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.MenuItem;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import org.w3c.dom.Text;

public class Interlock_Relaying_ToJob extends AppCompatActivity {

    public static String accOut, locOut, moveOut;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    public static boolean pageCheck(Spinner location){
        //clearing all outlines
        ViewValidity.removeOutline(location);

        if(String.valueOf(location.getSelectedItem()).equals("Job Location")){
            ViewValidity.setupOutline(location);
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying__to_job);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }
        final SeekBar accSlider = (SeekBar)findViewById(R.id.accSlider);
        final Spinner locationSpin = (Spinner)findViewById(R.id.locationSpin);
        final SeekBar moveSlider = (SeekBar)findViewById(R.id.moveSlider);
        final TextView moveDisplay = (TextView)findViewById(R.id.moveDisplay);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);
        final TextView accDisplay = (TextView)findViewById(R.id.accDisplay);

        accSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0){
                    accDisplay.setText("Very accessible");
                }else if (progress == 1){
                    accDisplay.setText("Moderately accessible");
                }else if (progress == 2){
                    accDisplay.setText("Moderately inaccessible");
                }else{
                    accDisplay.setText("Very inaccessible");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    moveDisplay.setText("Lots of room to move");
                }else if(progress == 1){
                    moveDisplay.setText("A decent amount of room to move");
                }else if(progress == 2){
                    moveDisplay.setText("Not much room to move");
                }else{
                    moveDisplay.setText("Very little room to move");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageCheck(locationSpin);
                if(check == true){
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_Estimate.class);
                    //saving data to extras
                    accOut = "The job is... " + String.valueOf(accDisplay.getText()).toLowerCase();
                    moveOut = "There is... " + String.valueOf(moveDisplay.getText()).toLowerCase();
                    locOut = "Job location: " + String.valueOf(locationSpin.getSelectedItem());
                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
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
                if(position==0){
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(Interlock_Relaying_ToJob.this, ActivityDatabaseAccounts.class));
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
