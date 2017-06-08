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

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealingEstimation extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

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
        // get TextViews
        TextView dimensionsOut = (TextView) findViewById(R.id.dimensions_out);
        TextView angleOut = (TextView) findViewById(R.id.angle_out);
        TextView stainOut = (TextView) findViewById(R.id.stain_out);
        TextView stainTypeOut = (TextView) findViewById(R.id.stain_type_out);
        TextView stainPercentOut = (TextView) findViewById(R.id.stain_percent_out);
        TextView ageOut = (TextView) findViewById(R.id.age_out);
        TextView otherCompOut = (TextView) findViewById(R.id.comp_out);
        TextView finalOut = (TextView) findViewById(R.id.final_out);
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
        if(stainChecked) {
            stainOut.setText("Yes");
            String stainTypeStr = extras.getString("stain_type_str");
            stainTypeOut.setText(stainTypeStr);
            int stainAmount = extras.getInt("stain_amount");
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
        finalOut.setText("Final Estimate: 0 hours and 0 minuets");

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
                    startActivity(new Intent(CleaningSealingEstimation.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(CleaningSealingEstimation.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(CleaningSealingEstimation.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(CleaningSealingEstimation.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(CleaningSealingEstimation.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(CleaningSealingEstimation.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(CleaningSealingEstimation.this, ActivityDatabaseAccounts.class));
                }
            }
        });

    }

    public void fabClicked(View fab){
        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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
