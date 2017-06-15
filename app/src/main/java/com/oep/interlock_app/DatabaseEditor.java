package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by:
 *     Ethan and Peter
 * Floating Action Button animation from:
 *     https://www.learn2crack.com/2015/10/android-floating-action-button-animations.html
 */

public class DatabaseEditor extends AppCompatActivity {
    private Boolean isFabOpen = false;
    private FloatingActionButton menuFab,deleteFab,timeFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private EstimationSheet estimationSheet;
    private int smallEstimationId;
    private boolean userEditedDatabase = false;
    final static int RESULT_DELETED = 0;
    final static int RESULT_NA = 1;

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_editor);
        //get resources
        menuFab = (FloatingActionButton)findViewById(R.id.menu_fab);
        deleteFab = (FloatingActionButton)findViewById(R.id.delete_fab);
        timeFab = (FloatingActionButton)findViewById(R.id.time_fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_backward);
        final TextView actualTimeTextView = (TextView) findViewById(R.id.actual_time_output);
        TextView estimatedTimeTextView = (TextView) findViewById(R.id.estimated_time_output);
        TextView dateTextView = (TextView) findViewById(R.id.date_out);
        TextView idTextView = (TextView) findViewById(R.id.id_out);
        TextView jobTypeTextView = (TextView) findViewById(R.id.type_out);

        //check if user is new and if so, give directions
        try {
            FileInputStream input = this.openFileInput("userEditedDatabase");
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String edited =  bufferedReader.readLine();
            input.close();
            inputStreamReader.close();
            bufferedReader.close();
            if(edited.equals("true"))
                userEditedDatabase = true;
        } catch (Exception ignored){}

        if(!userEditedDatabase)
            Toast.makeText(this, "Press the \"+\" to edit.", Toast.LENGTH_LONG).show();


        //get info
        Bundle extras = getIntent().getExtras();
        final Object data = extras.get("estimationData");
        if(data == null){
            throw new NullPointerException("DatabaseEditor was called without being handed an extra with the estimation data on it.");
        }
        String fullEstimationId = new ArrayList<>(Arrays.asList((Object[]) data)).get(EstimationSheet.COLUMN_ESTIMATION_ID).toString();
        smallEstimationId = EstimationSheet.getSmallEstimationIdFromFullEstimationId(fullEstimationId);
        String date = new ArrayList<>(Arrays.asList((Object[]) data)).get(EstimationSheet.COLUMN_DATE).toString();
        Double estimatedHours;
        Double actualTime;
        try {
            actualTime = Double.parseDouble((String) (new ArrayList<>(Arrays.asList((Object[]) data)).get(EstimationSheet.COLUMN_ACTUAL_TIME)));
        } catch (NumberFormatException e){
            actualTime = -1.0;
        }
        try{
            estimatedHours = Double.parseDouble((String) (new ArrayList<>(Arrays.asList((Object[]) data)).get(EstimationSheet.COLUMN_ESTIMATED_TIME)));
        } catch (NumberFormatException e){
            estimatedHours = -1.0;
        }
        String type = EstimationSheet.getProperSheetNameFromFullEstimationId(fullEstimationId);

        //fill text views
        Time time = new Time(actualTime);
        actualTimeTextView.setText(time.toString());
        if(estimatedHours == -1.0){
            estimatedTimeTextView.setText("No estimation made.");
        } else{
            time.setHours(estimatedHours);
            estimatedTimeTextView.setText(time.toString());
        }
        dateTextView.setText(date);
        idTextView.setText(fullEstimationId);
        jobTypeTextView.setText(type);

        estimationSheet = new EstimationSheet(EstimationSheet.getSheetIdFromFullEstimationId(fullEstimationId), this);
        final Activity activity = this;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.menu_fab:
                        animateFAB();
                        break;

                    case R.id.delete_fab:
                        animateFAB();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Delete data?");
                        builder.setMessage("Are you sure that you would like to delete this data? It" +
                                " should only be deleted if it is invalid.");
                        builder.setPositiveButton("DELETE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        estimationSheet.startRemovingEstimation(smallEstimationId, new RemoveEstimationListener() {
                                            @Override
                                            public void whenFinished(boolean success) {
                                                setResult(RESULT_DELETED);
                                                finish();
                                            }
                                        });
                                    }
                                });
                        builder.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        break;

                    case R.id.time_fab:
                        animateFAB();
                        final TextView actualTimeTextView = (TextView) findViewById(R.id.actual_time_output);
                        CustomTimePickerDialog b = new CustomTimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                final double hours = hourOfDay+minute/60.0;
                                estimationSheet.startSettingActualTime(smallEstimationId, hours, new SetActualTimeListener() {
                                    @Override
                                    public void whenFinished(boolean success) {
                                        actualTimeTextView.setText(new Time(hours).toString());
                                    }
                                });
                            }
                        });
                        b.show();
                        break;
                }
            }
        };
        menuFab.setOnClickListener(onClickListener);
        deleteFab.setOnClickListener(onClickListener);
        timeFab.setOnClickListener(onClickListener);

        //code to use the drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mActivityTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

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
                    startActivity(new Intent(DatabaseEditor.this, HomeScreen.class));
                }
                else if(position==1){
                    startActivity(new Intent(DatabaseEditor.this, HelpPage.class));
                }
                else if(position==2){
                    startActivity(new Intent(DatabaseEditor.this, EstimationPage.class));
                }
                else if(position==3){
                    startActivity(new Intent(DatabaseEditor.this, DatabaseManagement.class));
                }
                else if(position==4){
                    startActivity(new Intent(DatabaseEditor.this, EnterDatabaseIdActivity.class));
                }
                else if(position==5){//this will only be true if the user is owner
                    startActivity(new Intent(DatabaseEditor.this, ActivityDatabaseAccounts.class));
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
    public void onBackPressed(){
        setResult(RESULT_NA);
        finish();
    }

    public void animateFAB(){

        if(!userEditedDatabase){
            // write to userEditedDatabase
            try {
                FileOutputStream fos = this.openFileOutput("userEditedDatabase", Context.MODE_PRIVATE);
                fos.write("true".getBytes());
                fos.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if(isFabOpen){

            menuFab.startAnimation(rotate_backward);
            deleteFab.startAnimation(fab_close);
            timeFab.startAnimation(fab_close);
            deleteFab.setClickable(false);
            timeFab.setClickable(false);
            deleteFab.setVisibility(View.VISIBLE);
            deleteFab.setVisibility(View.VISIBLE);
            isFabOpen = false;

        } else {

            menuFab.startAnimation(rotate_forward);
            deleteFab.startAnimation(fab_open);
            timeFab.startAnimation(fab_open);
            deleteFab.setClickable(true);
            timeFab.setClickable(true);
            deleteFab.setVisibility(View.INVISIBLE);
            deleteFab.setVisibility(View.INVISIBLE);
            isFabOpen = true;

        }
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

    private class CustomTimePickerDialog extends TimePickerDialog {

        private CustomTimePickerDialog(Context context, OnTimeSetListener listener) {
            super(context, listener, 3, 0, true);
            this.setTitle("Enter the actual time the job took.");
            this.setMessage("3 hours and 0 minutes.");
        }

        @Override
        public void setTitle(CharSequence title) {
            try {
                int min = Integer.parseInt(title.toString().substring(title.length() - 5, title.length() - 3));
                int rawHours = Integer.parseInt(title.toString().substring(0, title.length() - 6));
                int hours;
                if(rawHours == 12 && title.toString().toLowerCase().contains("am"))
                    hours = 0;
                else if (rawHours == 12)
                    hours = 12;
                else {
                    if (title.toString().toLowerCase().contains("pm"))
                        hours = rawHours + 12;
                    else
                        hours = rawHours;
                }

                this.setMessage(hours + " hours and " + min + " minutes.");
            } catch (NumberFormatException ignored){}
            super.setTitle("Enter the actual time the job took.");
        }
    }

    private class Time {

        private Double hours;

        private Time(Double hours){
            this.hours = hours;
        }

        private int getRoundedMinutes(){
            Double doubleMinutes = hours - getIntHours();
            if(doubleMinutes > 0.875)
                return 0;
            else if(doubleMinutes > 0.625)
                return 45;
            else if(doubleMinutes > 0.375)
                return 30;
            else if(doubleMinutes > 0.125)
                return 15;
            else
                return 0;
        }

        private int getIntHours(){
            return hours.intValue();
        }

        private void setHours(Double hours){
            this.hours = hours;
        }


        public String toString(){
            if(hours == -1.0)
                return "Unknown";
            else if (getIntHours() == 1)
                return getIntHours()+" hour, "+getRoundedMinutes()+" minutes";
            else
                return getIntHours()+" hours, "+getRoundedMinutes()+" minutes";
        }
    }
}
