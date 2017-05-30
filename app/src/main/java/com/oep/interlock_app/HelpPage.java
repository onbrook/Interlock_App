package com.oep.interlock_app;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

public class HelpPage extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

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
                    Toast.makeText(HelpPage.this, "About", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, AboutPage.class));
                }
                else if(position==1){
                    Toast.makeText(HelpPage.this, "Home Screen", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, HomeScreen.class));
                }
                else if(position==2){
                    Toast.makeText(HelpPage.this, "Database Entry", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, DatabaseEntry.class));
                }
                else if(position==3){
                    Toast.makeText(HelpPage.this, "Database Removal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, DatabaseRemoval.class));
                }
                else if(position==4){
                    Toast.makeText(HelpPage.this, "Database Setup", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){
                    Toast.makeText(HelpPage.this, "Database Permissions", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, AddDatabasePermissionsActivity.class));
                }
                else{
                    Toast.makeText(HelpPage.this, "Help!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HelpPage.this, HelpPage.class));
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
