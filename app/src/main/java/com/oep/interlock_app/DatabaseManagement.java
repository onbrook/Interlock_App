package com.oep.interlock_app;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class DatabaseManagement extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    EstimationSheet estimationSheet;
    //string to be used to display information on job on edit page
    private List<List<Object>> allData = new ArrayList<>();
    private List<List<Object>> visibleData = new ArrayList<>();
    private int oldSearchLength = 0;
    SimpleAdapter adapter;
    private static final int REQUEST_EDIT_DATABASE = 0;
    private boolean getTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_management);
        final ListView estDisplay = (ListView)findViewById(R.id.estDisplay);
        final EditText estSearchInput = (EditText)findViewById(R.id.estSearch);
        final TextView title = (TextView) findViewById(R.id.title);
        try {
            getTime = getIntent().getExtras().getBoolean("getTime");
        } catch (NullPointerException ignore){
            getTime = false;
        }

        final Activity activity = this;

        estSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldSearchLength = estSearchInput.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {/* Do nothing */}

            @Override
            public void afterTextChanged(Editable s) {
                String date = estSearchInput.getText().toString();
                if (date.length() == 2) {
                    if(oldSearchLength == 3) {//backspace pressed
                        // remove "/" and last number
                        estSearchInput.setText(date.substring(0, date.length() - 1));
                        estSearchInput.setSelection(date.length() - 1);
                    } else {
                        //put in "/"
                        estSearchInput.setText(date + "/");
                        estSearchInput.setSelection(date.length() + 1);
                    }
                } else if (date.length() == 5) {
                    if(oldSearchLength == 6) {//backspace pressed
                        // remove "/" and last number
                        estSearchInput.setText(date.substring(0, date.length() - 1));
                        estSearchInput.setSelection(date.length() - 1);
                    } else {
                        //put in "/"
                        estSearchInput.setText(date + "/");
                        estSearchInput.setSelection(date.length() + 1);
                    }
                } else if (date.length() > 10) {
                    //prevent user from typing more; remove last char
                    estSearchInput.setText(date.substring(0, date.length() - 1));
                    estSearchInput.setSelection(date.length() - 1);
                }
            }
        });

        estSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean valid = v.getText().toString().length() == 10;
                boolean empty = v.getText().toString().length() == 0;
                boolean noResults = true;
                if (valid){
                    // search for data
                    visibleData.clear();
                    List<Map<String, String>> data = new ArrayList<>();
                    for (List<Object> estimation : allData) {
                        if(estimation.get(EstimationSheet.COLUMN_DATE).toString().equals(v.getText().toString())) {
                            visibleData.add(estimation);
                            Map<String, String> datum = new HashMap<>(2);
                            datum.put("date", (String) estimation.get(EstimationSheet.COLUMN_DATE));
                            datum.put("id", (String) estimation.get(EstimationSheet.COLUMN_ESTIMATION_ID));
                            data.add(datum);
                            noResults = false;
                        }
                    }

                    if(!noResults) {
                        adapter = new SimpleAdapter(activity, data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"date", "id"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        estDisplay.setAdapter(adapter);
                    } else {
                        Toast.makeText(activity, "Date not found.", Toast.LENGTH_SHORT).show();
                    }
                    return false; // keep up keyboard? : false
                } else if (!empty) {
                    Toast.makeText(activity, "Invalid date.", Toast.LENGTH_SHORT).show();
                    return true; // keep up keyboard? : true
                } else {
                    return false; // keep up keyboard? : false
                }
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // get data
        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        estimationSheet.startGettingAllData(new GetDataListener() {
            @Override
            public void whenFinished(boolean success, List<List<Object>> output) {
                allData = output;
                if(success) {
                    //show views
                    title.setVisibility(View.VISIBLE);
                    estSearchInput.setVisibility(View.VISIBLE);

                    if(getTime) { // show only the data with no time entered; coming from notification

                        // put data in ListView
                        List<Map<String, String>> data = new ArrayList<>();
                        for (List<Object> estimation : output) {
                            if(estimation.get(EstimationSheet.COLUMN_ACTUAL_TIME).toString().equals("")) { // if no time is entered...
                                visibleData.add(estimation);
                                Map<String, String> datum = new HashMap<>(2);
                                datum.put("date", (String) estimation.get(EstimationSheet.COLUMN_DATE));
                                datum.put("id", (String) estimation.get(EstimationSheet.COLUMN_ESTIMATION_ID));
                                data.add(datum);
                            }
                        }
                        adapter = new SimpleAdapter(activity, data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"date", "id"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        estDisplay.setAdapter(adapter);

                    } else { // show all data
                        visibleData = output;

                        // put data in ListView
                        List<Map<String, String>> data = new ArrayList<>();
                        for (List<Object> estimation : output) {
                            Map<String, String> datum = new HashMap<>(2);
                            datum.put("date", (String) estimation.get(EstimationSheet.COLUMN_DATE));
                            datum.put("id", (String) estimation.get(EstimationSheet.COLUMN_ESTIMATION_ID));
                            data.add(datum);
                        }
                        adapter = new SimpleAdapter(activity, data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"date", "id"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        estDisplay.setAdapter(adapter);
                    }
                } else {
                    title.setVisibility(View.VISIBLE);
                    title.setText("There are no estimations.");
                }


            }
        });

        estDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get selected item id + location, take user to next page to edit selected job
                Intent editPage = new Intent(getApplicationContext(), DatabaseEditor.class);
                editPage.putExtra("estimationData", visibleData.get(position).toArray());
                startActivityForResult(editPage, REQUEST_EDIT_DATABASE);
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
                    startActivity(new Intent(DatabaseManagement.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(DatabaseManagement.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(DatabaseManagement.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(DatabaseManagement.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(DatabaseManagement.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(DatabaseManagement.this, ActivityDatabaseAccounts.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EDIT_DATABASE) {
            //restart activity
            finish();
            startActivity(getIntent());
        } else {
            estimationSheet.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, estimationSheet);
    }
}
