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
import android.widget.Button;
import android.widget.ListView;

import com.oep.owenslaptop.interlock_app.R;

public class HomeScreen extends AppCompatActivity {

    /*

    Names: Ethan McIntyre, Owen Brook & Peter Lewis

    Description:

    Date Started: April 7th, 2017

     */

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private boolean isUserOwner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if the user is new and if so, go to correct activity
        final EstimationSheet estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if(!estimationSheet.doesUserHaveRole()) {
            startActivityForResult(new Intent(getApplicationContext(), SetUserTypeActivity.class), EstimationSheet.REQUEST_GET_DATABASE_ID);
        } else if (!estimationSheet.isDatabaseIdSaved()) {
            isUserOwner = estimationSheet.isUserOwner();
            if (isUserOwner) {
                estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
                    @Override
                    public void whenFinished(boolean success) {/* do nothing*/}
                });
            } else {
                startActivityForResult(new Intent(getApplicationContext(), EnterDatabaseIdActivity.class), EstimationSheet.REQUEST_GET_DATABASE_ID);
            }
        }else
            isUserOwner = estimationSheet.isUserOwner();

        setContentView(R.layout.activity_home_screen);
        setTitle("Home");

        //setting up the buttons
        Button estimateBtn = (Button)findViewById(R.id.estimatePageBtn);

        //moving to the estimate page
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, EstimationPage.class));
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
                    startActivity(new Intent(HomeScreen.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(HomeScreen.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(HomeScreen.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(HomeScreen.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(HomeScreen.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(HomeScreen.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(HomeScreen.this, ActivityDatabaseAccounts.class));
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
        ArrayAdapter<String> mAdapter;
        if(isUserOwner) {
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
