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

import org.w3c.dom.Text;

public class Interlock_Relaying_Estimate extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

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
        }/*
        final TextView output = (TextView)findViewById(R.id.output);

        output.setText("Width: " + Interlock_Relaying.widthOut + "\n" +
                "Length: " + Interlock_Relaying.lengthOut + "\n" +
                "Pattern: " + Interlock_Relaying.ptrnOut + "\n" +
                "Shape: " + Interlock_Relaying.shapeOut + "\n" +
                Interlock_Relaying.edgeOut + "\n" +
                "The interlock... " + Interlock_Relaying_Complications.sunkOut + "\n" +
                "The interlock is... " + Interlock_Relaying_Complications.paveSizeOut + "\n" +
                "There are... " + Interlock_Relaying_Complications.weedOut + "\n" +
                "The joint fill is... " + Interlock_Relaying_Complications.fillOut + "\n" +
                Interlock_Relaying_ToJob.locOut + "\n" +
                Interlock_Relaying_ToJob.moveOut + "\n" +
                Interlock_Relaying_ToJob.accOut);
                //estimate here*/

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
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(Interlock_Relaying_Estimate.this, ActivityDatabaseAccounts.class));
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
