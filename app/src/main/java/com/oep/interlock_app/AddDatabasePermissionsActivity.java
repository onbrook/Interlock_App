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
                if (email.toLowerCase().contains("@gmail.")) {

                    emails.add(email);

                    String emailsStr = "Emails:\n";
                    for (String em : emails)
                        emailsStr += em+"\n";

                    outputTextView.setText(emailsStr);
                    emailEditText.setText("");

                }else
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Please use Gmail accounts.", Toast.LENGTH_SHORT)
                            .show();
            }
        });

        okFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
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
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("An error has occurred when trying to give the" +
                                        " requested Google Accounts access to the database. This could" +
                                        " have been caused from the emails which where entered being" +
                                        " non-existent");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                            }
                                        });
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "About", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, AboutPage.class));
                }
                else if(position==1){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Home Screen", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, HomeScreen.class));
                }
                else if(position==2){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Database Entry", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, DatabaseEntry.class));
                }
                else if(position==3){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Database Removal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, DatabaseRemoval.class));
                }
                else if(position==4){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Database Setup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Database Permissions", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, AddDatabasePermissionsActivity.class));
                }
                else{
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Help!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddDatabasePermissionsActivity.this, HelpPage.class));
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
