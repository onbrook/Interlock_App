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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.oep.interlock_app.ViewValidity.areViewsValid;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[2];
    private RadioButton curvedRB;
    public static String sizeJobHSt, sizeJobLSt, baseShiftSt, stepsAreSt;
    private EditText jobHeight, jobLength;
    private SeekBar baseShift;
    private TextView baseShiftDisTV;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding);

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
        curvedRB = (RadioButton) findViewById(R.id.curvedRB);
        baseShift = (SeekBar) findViewById(R.id.baseShiftSB);
        jobHeight = (EditText) findViewById(R.id.height_input);
        jobLength = (EditText) findViewById(R.id.length_input);
        baseShiftDisTV = (TextView) findViewById(R.id.baseShiftDisplayTV);

        //adding the spinners to the array
        views[0] = jobHeight;
        views[1] = jobLength;

        baseShiftDisTV.setText("Average");

        //ease of access
        baseShift.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    baseShiftDisTV.setText("None");
                    baseShiftSt = "None";
                }
                else if(progress==1){
                    //
                    baseShiftDisTV.setText("Slight Bit");
                    baseShiftSt = "Slight Bit";
                }
                else if(progress==2){
                    //
                    baseShiftDisTV.setText("Average");
                    baseShiftSt = "Average";
                }
                else if(progress==3){
                    //
                    baseShiftDisTV.setText("Quite a Bit");
                    baseShiftSt = "Quite a Bit";
                }
                else{
                    //must be the last one
                    baseShiftDisTV.setText("A Lot");
                    baseShiftSt = "A Lot";
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
                ILDialog.showExitDialogWarning(activity, intent);
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            sizeJobHSt = jobHeight.getText().toString();
            sizeJobLSt = jobLength.getText().toString();

            boolean curvedB =  curvedRB.isSelected();
            if(curvedB){
                stepsAreSt = "Curved";
            }
            else{
                //must be straight
                stepsAreSt = "Straight";
            }
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Step_Rebuilding2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(views);
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
