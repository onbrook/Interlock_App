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
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Step_Rebuilding2 extends AppCompatActivity {

    //setting up the spinners and the array
    private View[] views = new View[1];
    private Spinner locationSp;
    private RadioButton longTRB;
    private SeekBar roomMarSB, easeAccSB;
    public static String locationSt, easeAccSt, roomManSt;
    private TextView roomManDisplayTV, easeDisplayTV;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding2);

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

        //setting up the GUI componenets
        locationSp = (Spinner) findViewById(R.id.locationSp);
        roomMarSB = (SeekBar) findViewById(R.id.roomManSB);
        easeAccSB = (SeekBar) findViewById(R.id.easeSB);
        easeDisplayTV = (TextView) findViewById(R.id.easeDisplayTV);
        roomManDisplayTV = (TextView) findViewById(R.id.roomDisplayTV);

        //setting the default values
        easeDisplayTV.setText("Average");
        roomManDisplayTV.setText("Average");


        //adding the spinners to the array
        views[0] = locationSp;

        //ease of access
        easeAccSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    easeDisplayTV.setText("Easy to Access");
                    easeAccSt = "Easy to Access";
                }
                else if(progress==1){
                    //
                    easeDisplayTV.setText("Average");
                    easeAccSt = "Average";
                }
                else{
                    //must be the last one
                    easeDisplayTV.setText("Hard to Access");
                    easeAccSt = "Hard to Access";
                }
            }
        });

        //room to man seekbar
        roomMarSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    roomManDisplayTV.setText("Almost no Room");
                    roomManSt = "Almost no Room";
                }
                else if(progress==1){
                    //
                    roomManDisplayTV.setText("Bit Tight");
                    roomManSt = "Bit Tight";
                }
                else if(progress==2){
                    //
                    roomManDisplayTV.setText("Average");
                    roomManSt = "Average";
                }
                else if(progress==3){
                    //
                    roomManDisplayTV.setText("Good amount");
                    roomManSt = "Good amount";
                }
                else{
                    //must be the last one
                    roomManDisplayTV.setText("Lots of Space");
                    roomManSt = "Lots of Space";
                }
            }
        });

        //when the size spinner is clicked
        locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(locationSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(locationSp);
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
                    startActivity(new Intent(Step_Rebuilding2.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(Step_Rebuilding2.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Step_Rebuilding2.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(Step_Rebuilding2.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(Step_Rebuilding2.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(Step_Rebuilding2.this, ActivityDatabaseAccounts.class));
                }
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(ViewValidity.areViewsValid(views)) {
            //getting the input from the user
            locationSt = locationSp.getSelectedItem().toString();
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Step_Rebuilding3.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("locationIndex", locationSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            ViewValidity.updateViewValidity(views);
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
