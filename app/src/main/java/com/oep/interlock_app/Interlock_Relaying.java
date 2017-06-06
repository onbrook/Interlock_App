package com.oep.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.removeOutline;
import static com.oep.interlock_app.ViewValidity.setupOutline;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Interlock_Relaying extends AppCompatActivity {

    public static String widthOut, lengthOut, ptrnOut, shapeOut, edgeOut;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    public static boolean pageOneCheck(EditText len, EditText wid,
                                       Spinner ptrn, Spinner shape){
        //clearing all outlines
        removeOutline(len);
        removeOutline(wid);
        removeOutline(ptrn);
        removeOutline(shape);
        int falseCheck = 0;

        //checking all elements
        if(!TextUtils.isEmpty(String.valueOf(len.getText()))){
            if(Double.parseDouble(String.valueOf((len.getText()))) == 0){
                setupOutline(len);
                falseCheck = 1;
            }
        }else if(TextUtils.isEmpty(String.valueOf(len.getText()))){
            setupOutline(len);
            falseCheck = 1;
        }

        if(!TextUtils.isEmpty(String.valueOf(wid.getText()))){
            if(Double.parseDouble(String.valueOf(wid.getText())) == 0){
                setupOutline(wid);
                falseCheck = 1;
            }
        }else if(TextUtils.isEmpty(String.valueOf(wid.getText()))){
            setupOutline(wid);
            falseCheck = 1;
        }

        if(String.valueOf(ptrn.getSelectedItem()).equals("Pattern Type")){
            setupOutline(ptrn);
            falseCheck = 1;
        }
        if(String.valueOf(shape.getSelectedItem()).equals("Shape of the job")){
            setupOutline(shape);
            falseCheck = 1;
        }


        if (falseCheck == 1){
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying_layout);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }
        final EditText width = (EditText)findViewById(R.id.widtInput);
        final EditText length = (EditText)findViewById(R.id.lenInput);
        final Spinner patternTypeSpin = (Spinner)findViewById(R.id.ptrnTypeSpin);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);
        final Spinner jobShape = (Spinner)findViewById(R.id.jobShapeSpin);
        final CheckBox edgeCheck = (CheckBox)findViewById(R.id.edgeCheck);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageOneCheck(length, width, patternTypeSpin, jobShape);
                //add function to check for current layout
                if(check == true){
                    //saving all necessary variables
                    widthOut = String.valueOf(width.getText()) + "ft.";
                    lengthOut = String.valueOf(length.getText()) + "ft.";
                    ptrnOut = String.valueOf(patternTypeSpin.getSelectedItem());
                    shapeOut = String.valueOf(jobShape.getSelectedItem());
                    if(edgeCheck.isChecked()){
                        edgeOut = "Edging: Yes";
                    }else{
                        edgeOut = "Edging: No";
                    }
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_Complications.class);

                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
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
                    startActivity(new Intent(Interlock_Relaying.this, AboutPage.class));
                }
                else if(position==1){
                    startActivity(new Intent(Interlock_Relaying.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(Interlock_Relaying.this, HomeScreen.class));
                }
                else if(position==3){
                    startActivity(new Intent(Interlock_Relaying.this, EstimationPage.class));
                }
                else if(position==4){
                    startActivity(new Intent(Interlock_Relaying.this, DatabaseManagement.class));
                }
                else if(position==5){
                    startActivity(new Intent(Interlock_Relaying.this, EnterDatabaseIdActivity.class));
                }
                else if(position==6){//this will only be true if the user is owner
                    startActivity(new Intent(Interlock_Relaying.this, ActivityDatabaseAccounts.class));
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
