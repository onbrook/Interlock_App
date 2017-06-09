package com.oep.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: April 30, 2017
 */

public class Wall_Rebuilding3 extends AppCompatActivity {

    CheckBox glueCheckBox;
    CheckBox clipsCheckBox;
    SeekBar baseShiftSeekBar;
    SeekBar rootsSeekBar;
    SeekBar plantsSeekBar;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_complications);
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
        //views
        baseShiftSeekBar = (SeekBar) findViewById(R.id.base_shift_seek_bar);
        final TextView baseShiftTextView = (TextView) findViewById(R.id.base_shift_text_view);
        rootsSeekBar = (SeekBar) findViewById(R.id.roots_seek_bar);
        final TextView rootsTV = (TextView) findViewById(R.id.roots_tv);
        plantsSeekBar = (SeekBar) findViewById(R.id.plants_seek_bar);
        final TextView plantsTV = (TextView) findViewById(R.id.plants_tv);
        glueCheckBox = (CheckBox) findViewById(R.id.glue_CheckBox);
        clipsCheckBox = (CheckBox) findViewById(R.id.clips_CheckBox);

        //SeekBar listeners
        baseShiftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                baseShiftTextView.setText(getResources().getStringArray(R.array.amount_increasing)[progress]);
            }
        });
        rootsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update rootsTV
                rootsTV.setText(getResources().getStringArray(R.array.amount_increasing)[progress]);
            }
        });
        plantsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update plantsTV
                plantsTV.setText(getResources().getStringArray(R.array.amount_increasing)[progress]);
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
                    startActivity(new Intent(Wall_Rebuilding3.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(Wall_Rebuilding3.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Wall_Rebuilding3.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(Wall_Rebuilding3.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(Wall_Rebuilding3.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(Wall_Rebuilding3.this, ActivityDatabaseAccounts.class));
                }
            }
        });
    }

    public void fabClicked(View fab) {
        //start activity (getIntent to save extras)
        Intent intent = getIntent();
        //update class
        intent.setClass(getApplicationContext(), Wall_Rebuilding_Estimation.class);
        //extras--for passing data
        intent.putExtra("baseShiftIndex", baseShiftSeekBar.getProgress());
        intent.putExtra("rootsNum", rootsSeekBar.getProgress());
        intent.putExtra("plantsNum", plantsSeekBar.getProgress());
        intent.putExtra("glueChecked", glueCheckBox.isChecked());
        intent.putExtra("clipsChecked", clipsCheckBox.isChecked());
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
