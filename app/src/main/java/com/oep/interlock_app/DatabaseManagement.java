package com.oep.interlock_app;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

import java.util.ArrayList;

public class DatabaseManagement extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_management);

        final ListView estDisplay = (ListView)findViewById(R.id.estDisplay);
        final EditText estSearchInput = (EditText)findViewById(R.id.estSearch);
        final Button searchBtn = (Button)findViewById(R.id.idSearchBtn);

        //code to use the drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList testList = new ArrayList<String>();
                ArrayAdapter adapter;
                if(TextUtils.isEmpty((CharSequence) estSearchInput)){
                    //display all jobs if no id has been entered
                    for(int i = 0; i < 50; i++){
                        testList.add("New Item - " + String.valueOf(i));
                    }

                }else{
                    //display jobs with entered id
                }
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Toast.makeText(DatabaseManagement.this, "About", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, AboutPage.class));
                }
                else if(position==1){
                    Toast.makeText(DatabaseManagement.this, "Home Screen", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, HomeScreen.class));
                }
                else if(position==2){
                    Toast.makeText(DatabaseManagement.this, "Database Entry", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, DatabaseManagement.class));
                }
                else if(position==3){
                    Toast.makeText(DatabaseManagement.this, "Database Removal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, DatabaseRemoval.class));
                }
                else if(position==4){
                    Toast.makeText(DatabaseManagement.this, "Database Setup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){
                    Toast.makeText(DatabaseManagement.this, "Database Permissions", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, AddDatabasePermissionsActivity.class));
                }
                else{
                    Toast.makeText(DatabaseManagement.this, "Help!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseManagement.this, HelpPage.class));
                }
            }
        });
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
