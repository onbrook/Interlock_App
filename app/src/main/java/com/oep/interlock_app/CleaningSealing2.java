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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.isViewValid;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealing2 extends AppCompatActivity {

    private CheckBox stainCheckBox;
    private Spinner stainTypeSpinner;
    private SeekBar stainAmountSeekBar;
    private SeekBar otherCompSeekBar;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private CheckBox oldCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing2);
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
        //get views
        stainCheckBox = (CheckBox) findViewById(R.id.stain_checkBox);
        stainTypeSpinner = (Spinner) findViewById(R.id.stain_spinner);
        stainAmountSeekBar = (SeekBar) findViewById(R.id.percent_stain_seekBar);
        otherCompSeekBar = (SeekBar) findViewById(R.id.other_comp_seekBar);
        oldCheckBox = (CheckBox) findViewById(R.id.old_check_box);
        final TextView percentStainTitleTextView = (TextView) findViewById(R.id.stains_percent_headline_tv);
        final TextView percentStainTextView = (TextView) findViewById(R.id.percent_stain_tv);
        final TextView otherCompTextView = (TextView) findViewById(R.id.other_comp_tv);
        //disable unused views (for stains)
        stainTypeSpinner.setEnabled(false);
        stainAmountSeekBar.setEnabled(false);
        percentStainTitleTextView.setEnabled(false);
        percentStainTextView.setEnabled(false);
        //listener for when stainCheckBox is pressed
        stainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stainTypeSpinner.setEnabled(isChecked);
                stainAmountSeekBar.setEnabled(isChecked);
                percentStainTitleTextView.setEnabled(isChecked);
                percentStainTextView.setEnabled(isChecked);
                if(!isChecked)
                    stainTypeSpinner.setBackgroundResource(R.drawable.no_outline_spinner);
            }
        });
        //listener for when seek bars are moved
        stainAmountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update percentStainTextView
                percentStainTextView.setText(getResources().getStringArray(R.array.amount_increasing)[progress]);
            }
        });
        otherCompSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                otherCompTextView.setText(getResources().getStringArray(R.array.amount_increasing)[progress]);
            }
        });
        //Spinner listener
        stainTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = stainTypeSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(stainTypeSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = stainTypeSpinner.getSelectedItemPosition();
                if(index != 0)
                    updateViewValidity(stainTypeSpinner);
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

    public void fabClicked(View view){
        if(isViewValid(stainTypeSpinner) && stainCheckBox.isChecked()) {
            //get the intent and reset the class
            Intent intent = getIntent();
            intent.setClass(getApplicationContext(), CleaningSealingEstimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("stain_checked", stainCheckBox.isChecked());
            intent.putExtra("stain_type_index", stainTypeSpinner.getSelectedItemPosition());
            intent.putExtra("stain_type_str", stainTypeSpinner.getSelectedItem().toString());
            intent.putExtra("stain_amount", stainAmountSeekBar.getProgress());
            intent.putExtra("old", oldCheckBox.isChecked());
            intent.putExtra("other_comp_num", otherCompSeekBar.getProgress());
            //start activity
            startActivity(intent);
        }else if(!stainCheckBox.isChecked()){
            //get the intent and reset the class
            Intent intent = getIntent();
            intent.setClass(getApplicationContext(), CleaningSealingEstimation.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("stain_checked", stainCheckBox.isChecked());
            intent.putExtra("stain_type_index", -1);
            intent.putExtra("stain_type_str", "none");
            intent.putExtra("stain_amount", -1);
            intent.putExtra("old", oldCheckBox.isChecked());
            intent.putExtra("other_comp_num", otherCompSeekBar.getProgress());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(stainTypeSpinner);
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
