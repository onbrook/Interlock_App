package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddDatabasePermissionsActivity extends AppCompatActivity {

    EstimationSheet estimationSheet;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_database_permissions);

        final EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
        Button addButton = (Button) findViewById(R.id.add_button);
        final TextView outputTextView = (TextView) findViewById(R.id.output_tv);
        FloatingActionButton okFab = (FloatingActionButton) findViewById(R.id.ok_fab);

        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);

        final List<String> emails = new ArrayList<>();

        final Activity activity = this;

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                String email = emailEditText.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Please use Gmail accounts.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    emails.add(email);

                    String emailsStr = "Emails:\n";
                    for (String em : emails)
                        emailsStr += em+"\n";

                    outputTextView.setText(emailsStr);
                    emailEditText.setText("");

                }
            }
        });

        okFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //add email to List of emails
                String email = emailEditText.getText().toString();
                if(!email.equals(""))
                    emails.add(email);

                // Process emails
                if(emails.size() == 0){
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                } else
                    estimationSheet.startAddingPermissions(emails, new AddPermissionsListener() {
                        @Override
                        public void whenFinished(boolean success) {
                            if(success) {
                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                alertDialog.setTitle("Success");
                                alertDialog.setMessage("The accounts have successfully been given " +
                                        "permission to access the database. Email notifications have " +
                                        "been sent to the requested emails.");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                            }
                                        });
                                alertDialog.show();
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                builder.setTitle("Error");
                                builder.setMessage("An error has occurred when trying to give the" +
                                        " requested Google Accounts access to the database. This could" +
                                        " have been caused from some of the emails which where entered being" +
                                        " non-existent. You can see which accounts have access by" +
                                        " going to Database Permissions.");

                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                            }
                                        });
                                builder.setNeutralButton("TAKE ME THERE",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), ActivityDatabaseAccounts.class));
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }


                        }
                    });
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
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, ActivityDatabaseAccounts.class));
                }
            }
        });

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
