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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.areViewsValid;
import static com.oep.interlock_app.ViewValidity.isViewValid;
import static com.oep.interlock_app.ViewValidity.removeOutline;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[3];
    private EditText lenInputET, widInputET;
    private Spinner patternSp;
    public static String lenInputSt, widInputSt, sizeJointSt, patternSt;
    private TextView sizeDisplayTV;
    private SeekBar sizeSB;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill);

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
        lenInputET = (EditText) findViewById(R.id.widtInput);
        widInputET = (EditText) findViewById(R.id.lenInput);
        patternSp = (Spinner) findViewById(R.id.patternSp);
        sizeDisplayTV = (TextView) findViewById(R.id.sizeDisplayTV);
        sizeSB = (SeekBar) findViewById(R.id.sizeJointsSB);

        //adding the spinners to the array
        views[0] = widInputET;
        views[1] = patternSp;
        views[2] = lenInputET;

        //setting the default value
        sizeDisplayTV.setText("Average");
        sizeJointSt = "Average";

        //EditText listeners
        lenInputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        lenInputET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });
        widInputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        widInputET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });


        //when the second size spinner is clicked
        patternSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = patternSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(patternSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = patternSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(patternSp);
            }
        });

        sizeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    sizeDisplayTV.setText("Very Small");
                    sizeJointSt = "Very Small";
                }
                else if(progress==1){
                    sizeDisplayTV.setText("Small");
                    sizeJointSt = "Small";
                }
                else if(progress==2){
                    sizeDisplayTV.setText("Average");
                    sizeJointSt = "Average";
                }
                else if(progress==3){
                    sizeDisplayTV.setText("Large");
                    sizeJointSt = "Large";
                }
                else{
                    //must be the last one
                    sizeDisplayTV.setText("Very Large");
                    sizeJointSt = "Very Large";
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
                    startActivity(new Intent(Joint_Fill.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(Joint_Fill.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Joint_Fill.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(Joint_Fill.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(Joint_Fill.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(Joint_Fill.this, ActivityDatabaseAccounts.class));
                }
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            widInputSt = widInputET.getText().toString();
            lenInputSt = lenInputET.getText().toString();
            patternSt = patternSp.getSelectedItem().toString();
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Joint_Fill2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("pattenIndex", patternSp.getSelectedItemPosition());
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
