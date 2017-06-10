package com.oep.interlock_app;

/* Help Page ViewList animation:
 *    https://www.codeproject.com/Articles/1151814/Android-ExpandablelistView-Tutorial-with-Android-C
 */

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.oep.owenslaptop.interlock_app.R;

public class HelpPage extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private LinkedHashMap<String, HeaderInfo> mySection = new LinkedHashMap<>();
    private ArrayList<HeaderInfo> SectionList = new ArrayList<>();

    private MyListAdapter listAdapter;
    private ExpandableListView expandableListView;

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

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(HelpPage.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(HelpPage.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(HelpPage.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(HelpPage.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(HelpPage.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(HelpPage.this, ActivityDatabaseAccounts.class));
                }
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

        //adding the data
        AddProduct();

        //get reference to the ExpandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.myList);
        //create the adapter by passing your ArrayList data
        listAdapter = new MyListAdapter(HelpPage.this, SectionList);
        //attach the adapter to the list
        expandableListView.setAdapter(listAdapter);

        //listener for child row click
        expandableListView.setOnChildClickListener(myListItemClicked);
    }


    //our child listener
    private OnChildClickListener myListItemClicked =  new OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = SectionList.get(groupPosition);
            //get the child info
            DetailInfo detailInfo =  headerInfo.getProductList().get(childPosition);
            //collapse the one that is open
            expandableListView.collapseGroup(groupPosition);
            return false;
        }
    };

    //load some initial data into out list
    private void AddProduct(){

        //this is where we add the information
        String commonError, createEstimate, setUpNewDa, useExDa, addPermish, dataMan;

        commonError = "Why does it need access to my contacts \n - It uses the contacts to manage the database in your or your employer's Google Drive." +
                "\n\nWhy can’t an estimation be made when there is no data on the database? \n - This app uses only the data from the database to get the estimation. There is no other data that it has to use.";

        createEstimate = "To create a new estimate navigate, you will need to navigate to the “New Estimation” page.  From there you will need to then select the job that you are wanting the time estimate for.  Navigate through the pages that follow, and enter the requested data as accurately as possible.  On the final page you will be provided with all the data you added and the estimated time which is projected from the job history.";

        setUpNewDa = "To setup a new database, go into “Database Setup” and tap the menu (three dots) in the top left action bar. Then there is an option to " +
                "“Create a database”, tap on that and, once you give the app permission to access your Google Drive via contacts, the database will be created. You are then taken " +
                "to a page where you can enter the Gmail addresses of people who you would like to edit your database. It is recommended that only employers create databases and that employees use their employer's database.";

        useExDa = "To use an existing database you will need to be sent an invitation from the database manager (the one who set it up) and " +
                "then enter that id into the text entry box in “Database Setup”.";

        addPermish = "Adding permissions only applies to the database owner.  To allow others to access your database (which you would want for those in the same company)" +
                " go to the permissions page and enter in the email for the person you are wanting to invite.  ";

        dataMan = "To either add the actual time to the database, or to delete an estimate, navigate to the database management page  ??";

        addProduct("Common errors and questions",commonError);
        addProduct("Create an estimate",createEstimate);
        addProduct("Setting up a new database",setUpNewDa);
        addProduct("Using an existing database",useExDa);
        addProduct("Adding permissions",addPermish);
        addProduct("Database management",dataMan);

    }
    //here we maintain our products in various departments
    private void addProduct(String department, String product){

        //check the hash map if the group already exists
        HeaderInfo headerInfo = mySection.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setName(department);
            mySection.put(department, headerInfo);
            SectionList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        //create a new child and add that to the group
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);
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
