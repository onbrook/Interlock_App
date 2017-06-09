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
import android.widget.ListView;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding4 extends AppCompatActivity {

    //setting up the textview for display of the input and the output
    private TextView sizeHDisplay, sizeLDisplay, baseShiftDisplay, stepsAreDisplay, locationDisplay, easeAccessDisplay, roomManDisplay,
            stepsGluedDisplay, treeRootsDisplay, clipsDisplay, hardLineDisplay, finalTimeDisplay;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding4);

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
        sizeHDisplay = (TextView) findViewById(R.id.sizeHDisplay);
        sizeLDisplay = (TextView) findViewById(R.id.sizeLDisplay);
        baseShiftDisplay = (TextView) findViewById(R.id.baseShiftDisplay);
        stepsAreDisplay = (TextView) findViewById(R.id.stepsAreDisplay);
        locationDisplay = (TextView) findViewById(R.id.locationDisplay);
        easeAccessDisplay = (TextView) findViewById(R.id.easeAccessDisplay);
        roomManDisplay = (TextView) findViewById(R.id.roomManDisplay);
        stepsGluedDisplay = (TextView) findViewById(R.id.stepsGluedDisplay);
        treeRootsDisplay = (TextView) findViewById(R.id.treeRootsDisplay);
        clipsDisplay = (TextView) findViewById(R.id.clipsDisplay);
        hardLineDisplay = (TextView) findViewById(R.id.hardLineDisplay);
        finalTimeDisplay = (TextView) findViewById(R.id.finalTimeDisplay);

        //adding the variables to the textviews for display
        sizeHDisplay.setText(Step_Rebuilding.sizeJobHSt);
        sizeLDisplay.setText(Step_Rebuilding.sizeJobLSt);
        baseShiftDisplay.setText(Step_Rebuilding.baseShiftSt);
        stepsAreDisplay.setText(Step_Rebuilding.stepsAreSt);
        locationDisplay.setText(Step_Rebuilding2.locationSt);
        easeAccessDisplay.setText(Step_Rebuilding2.easeAccSt);
        roomManDisplay.setText(Step_Rebuilding2.roomManSt);
        stepsGluedDisplay.setText(Step_Rebuilding3.stepsGlueSt);
        treeRootsDisplay.setText(Step_Rebuilding3.rootsSt);
        clipsDisplay.setText(Step_Rebuilding3.clipsSt);
        hardLineDisplay.setText(Step_Rebuilding3.hardLineSt);
        finalTimeDisplay.setText("??");

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
                    startActivity(new Intent(Step_Rebuilding4.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(Step_Rebuilding4.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Step_Rebuilding4.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(Step_Rebuilding4.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(Step_Rebuilding4.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(Step_Rebuilding4.this, ActivityDatabaseAccounts.class));
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

    //when the FAB is clicked
    public void fabClicked(View view){
        //create a new intent (you do not get the current one because we do not need any
        // information from the home screen)
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        //this removes the animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //extras--for passing data
        //start activity
        startActivity(intent);

    }
}
