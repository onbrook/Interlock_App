package com.oep.interlock_app;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealing extends AppCompatActivity {
    // getting to the job layout Views
    private EditText heightEditText;
    private EditText lengthEditText;
    private RadioGroup angleRadioGroup;
    private View[] views = new View[2];
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing);
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
        heightEditText = (EditText) findViewById(R.id.height_input);
        lengthEditText = (EditText) findViewById(R.id.length_input);
        angleRadioGroup = (RadioGroup) findViewById(R.id.angle_rg);
        //setup views[]
        views[0] = heightEditText;
        views[1] = lengthEditText;
        //EditText listeners
        heightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(ViewValidity.isViewValid(heightEditText))
                    ViewValidity.removeOutline(v);
                return !ViewValidity.isViewValid(v);//keep up keyboard
            }
        });
        lengthEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(ViewValidity.isViewValid(heightEditText))
                    ViewValidity.removeOutline(v);
                return !ViewValidity.isViewValid(v);//keep up keyboard
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
                    startActivity(new Intent(CleaningSealing.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(CleaningSealing.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(CleaningSealing.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(CleaningSealing.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(CleaningSealing.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(CleaningSealing.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(CleaningSealing.this, ActivityDatabaseAccounts.class));
                }
            }
        });
    }

    public void fabClicked(View view){
        if(ViewValidity.areViewsValid(views)) {
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), CleaningSealing2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("height_double", Double.parseDouble(heightEditText.getText().toString()));
            intent.putExtra("length_double", Double.parseDouble(lengthEditText.getText().toString()));
            intent.putExtra("angle_id", angleRadioGroup.getCheckedRadioButtonId());
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
