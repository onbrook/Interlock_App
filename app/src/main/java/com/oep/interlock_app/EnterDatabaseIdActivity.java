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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                final String input = editText.getText().toString();
                if(input.equals(""))
                    Toast.makeText(EnterDatabaseIdActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                else{
                    // Check if id is valid
                    saveId(input);
                    estimationSheet.startCheckingDatabaseIdValidity(input, new CheckDatabaseIdValidityListener() {
                        @Override
                        public void whenFinished(boolean validId) {
                            if (validId) { // ID is correct
                                saveId(input);
                                if(estimationSheet.isUserOwner())
                                    startActivityForResult(new Intent(getApplicationContext(), AddDatabasePermissionsActivity.class), EstimationSheet.REQUEST_ADD_PERMISSIONS);
                                else
                                    finish(); // Go back to where you where before coming here
                            } else {
                                showDialog("Invalid ID", "You do not have access to this database or it does not exist.");
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
                        Toast.makeText(EnterDatabaseIdActivity.this, "About", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, AboutPage.class));
                    }
                    else if(position==1){
                        Toast.makeText(EnterDatabaseIdActivity.this, "Home Screen", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, HomeScreen.class));
                    }
                    else if(position==2){
                        Toast.makeText(EnterDatabaseIdActivity.this, "Database Entry", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, DatabaseEntry.class));
                    }
                    else if(position==3){
                        Toast.makeText(EnterDatabaseIdActivity.this, "Database Removal", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, DatabaseRemoval.class));
                    }
                    else if(position==4){
                        Toast.makeText(EnterDatabaseIdActivity.this, "Database Setup", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, EnterDatabaseIdActivity.class));
                    }
                    else if(position==5){
                        Toast.makeText(EnterDatabaseIdActivity.this, "Database Permissions", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, AddDatabasePermissionsActivity.class));
                    }
                    else{
                        Toast.makeText(EnterDatabaseIdActivity.this, "Help!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EnterDatabaseIdActivity.this, HelpPage.class));
                    }
                }
            });
        }
        else{
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //create new database
        estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
            @Override
            public void whenFinished(boolean success) {
                if(success){
                    startActivityForResult(getIntent().setClass(getApplicationContext(), AddDatabasePermissionsActivity.class), EstimationSheet.REQUEST_ADD_PERMISSIONS);
                }
            }
        });
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
        if(requestCode == EstimationSheet.REQUEST_ADD_PERMISSIONS)
            finish();
        else
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
        String[] osArray = { "About", "Home Screen", "Database Entry", "Database Removal", "Database Setup", "Database Permissions", "Help!"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
    }
}
