package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oep.owenslaptop.interlock_app.R;

public class HomeScreen extends AppCompatActivity {

    /*

    Names: Ethan McIntyre, Owen Brook & Peter Lewis

    Description:

    Date Started: April 7th, 2017

     */

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if the user is new and if so, go to correct activity
        EstimationSheet estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if(!estimationSheet.doesUserHaveRole())
            startActivity(new Intent(getApplicationContext(), SetUserTypeActivity.class));
        else if (!estimationSheet.isDatabaseIdSaved())
            if(estimationSheet.isUserOwner())
                estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
                    @Override
                    public void whenFinished(boolean success) {

                    }
                });
            else
                startActivity(new Intent(getApplicationContext(), EnterDatabaseIdActivity.class));


        setContentView(R.layout.activity_home_screen);
        setTitle("Home");

        //setting up the buttons
        Button estimateBtn = (Button)findViewById(R.id.estimatePageBtn);

        //moving to the estimate page
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, EstimationPage.class));
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
                    Toast.makeText(HomeScreen.this, "About", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, AboutPage.class));
                }
                else if(position==1){
                    Toast.makeText(HomeScreen.this, "Home Screen", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, HomeScreen.class));
                }
                else if(position==2){
                    Toast.makeText(HomeScreen.this, "Database Entry", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, DatabaseEntry.class));
                }
                else if(position==3){
                    Toast.makeText(HomeScreen.this, "Database Removal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, DatabaseRemoval.class));
                }
                else if(position==4){
                    Toast.makeText(HomeScreen.this, "Database Setup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){
                    Toast.makeText(HomeScreen.this, "Database Permissions", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, AddDatabasePermissionsActivity.class));
                }
                else{
                    Toast.makeText(HomeScreen.this, "Help!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeScreen.this, HelpPage.class));
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
    }
}
