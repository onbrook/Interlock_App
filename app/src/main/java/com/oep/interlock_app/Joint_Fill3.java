package com.oep.interlock_app;

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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill3 extends AppCompatActivity {
    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private CheckBox weedsCB;
    private TextView jointFDisplayTV;
    private SeekBar jointFillSB;
    public static String weedJointSt, hardJointSt;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill3);

        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }

        //setting up the GUI components
        jointFillSB = (SeekBar) findViewById(R.id.jointFillSB);
        jointFDisplayTV = (TextView) findViewById(R.id.jointFDisplayTV);
        weedsCB = (CheckBox) findViewById(R.id.weedsCB);

        //setting the default values
        jointFDisplayTV.setText("Average");
        hardJointSt = "Average";

        //joint fill seekbar
        jointFillSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    jointFDisplayTV.setText("Very Soft");
                    hardJointSt = "Very Soft";
                }
                else if(progress==1){
                    //
                    jointFDisplayTV.setText("Soft");
                    hardJointSt = "Soft";
                }
                else if(progress==2){
                    //
                    jointFDisplayTV.setText("Average");
                    hardJointSt = "Average";
                }
                else if(progress==3){
                    //
                    jointFDisplayTV.setText("Hard");
                    hardJointSt = "Hard";
                }
                else{
                    //must be the last one
                    jointFDisplayTV.setText("Very Hard");
                    hardJointSt = "Very Hard";
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
                    startActivity(new Intent(Joint_Fill3.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(Joint_Fill3.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Joint_Fill3.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(Joint_Fill3.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(Joint_Fill3.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(Joint_Fill3.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(Joint_Fill3.this, ActivityDatabaseAccounts.class));
                }
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        //getting the values
        if(weedsCB.isChecked()){
            weedJointSt = "Yes";
        }
        else{
            weedJointSt = "No";
        }

        //create a new intent (you do not get the current one because we do not need any
        // information from the home screen)
        Intent intent = new Intent(getApplicationContext(), Joint_Fill4.class);
        //this removes the animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //start activity
        startActivity(intent);
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
}
