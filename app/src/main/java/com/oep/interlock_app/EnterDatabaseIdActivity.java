package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.oep.owenslaptop.interlock_app.R;

import java.io.FileOutputStream;

import pub.devrel.easypermissions.EasyPermissions;

public class EnterDatabaseIdActivity extends AppCompatActivity {

    private EstimationSheet estimationSheet;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_database_id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ok_fab);
        final EditText editText = (EditText) findViewById(R.id.edit_text);
        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        final Activity activity = this;
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                final String input = editText.getText().toString();
                if(input.equals(""))
                    Toast.makeText(EnterDatabaseIdActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                else{
                    // Check if id is valid
                    estimationSheet.startCheckingDatabaseIdValidity(input, new CheckDatabaseIdValidityListener() {
                        @Override
                        public void whenFinished(boolean validId, int errorId) {
                            if (validId) { // ID is correct
                                saveId(input);
                                if(estimationSheet.isUserOwner())
                                    startActivity(new Intent(getApplicationContext(), AddDatabasePermissionsActivity.class));
                                else
                                    // Go back to home
                                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                            } else {
                                if(errorId != EstimationSheet.NO_GOOGLE_PLAY_SERVICES_ERROR)
                                    showDialog("Invalid ID", "You do not have access to this database or it does not exist.");
                                // else caught in TaskCheckDatabaseIdValidity or startCheckingDatabaseIdValidity
                            }
                        }
                    });
                }
            }
        });

        //code to use the drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        if(estimationSheet.isDatabaseIdSaved()){
            addDrawerItems();
            setupDrawer();
            mDrawerToggle.setDrawerIndicatorEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        startActivity(new Intent(EnterDatabaseIdActivity.this, HomeScreen.class));
                    }
                    else if(position==1){
                        startActivity(new Intent(EnterDatabaseIdActivity.this, HelpPage.class));
                    }
                    else if(position==2){
                        startActivity(new Intent(EnterDatabaseIdActivity.this, EstimationPage.class));
                    }
                    else if(position==3){
                        startActivity(new Intent(EnterDatabaseIdActivity.this, DatabaseManagement.class));
                    }
                    else if(position==4){
                        startActivity(new Intent(EnterDatabaseIdActivity.this, EnterDatabaseIdActivity.class));
                    }
                    else if(position==5){//this will only be true if the user is owner
                        startActivity(new Intent(EnterDatabaseIdActivity.this, ActivityDatabaseAccounts.class));
                    }
                }
            });
        }
        else{
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_database, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //create new database
        final Activity activity = this;
        if(item.getItemId() == R.id.new_database)
            estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
                @Override
                public void whenFinished(boolean success, int errorId) {
                    if(success){
                        startActivity(getIntent().setClass(getApplicationContext(), AddDatabasePermissionsActivity.class));
                    } else
                        if(errorId != EstimationSheet.NO_GOOGLE_PLAY_SERVICES_ERROR) {
                            //show error dialog
                            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("An error has occurred when trying to create " +
                                    "the database.");

                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                        }
                                    });
                            alertDialog.show();
                        }
                }
            });
        else
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        else
            mDrawerLayout.openDrawer(Gravity.LEFT);

        return true;
    }

    private void saveId(String databaseId){
        try {
            FileOutputStream fos = openFileOutput(EstimationSheet.DATABASE_ID_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(databaseId.getBytes());
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDialog(String title, String body){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(body);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        estimationSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, estimationSheet);
    }

    private void addDrawerItems(){
        // Only have the "Database Permissions" if the user owns the database
        EstimationSheet estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if(estimationSheet.doesUserHaveRole() && estimationSheet.isUserOwner()) {
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
