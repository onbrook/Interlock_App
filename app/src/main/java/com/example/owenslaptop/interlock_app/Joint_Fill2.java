package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import static com.example.owenslaptop.interlock_app.ViewValidity.areViewsValid;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill2 extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[2];
    private CheckBox playCB, deckCB, poolCB;
    private Spinner locationSp, roomManSp;
    private RadioButton skinnyGRB, longThroRB;
    public static String locationSt, roomManSt, easeSt, whatArrSt;
    public static boolean playB, deckB, poolB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill2);

        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }

        //setting up the GUI components
        playCB = (CheckBox) findViewById(R.id.playStrucCB);
        deckCB = (CheckBox) findViewById(R.id.deckCB);
        poolCB = (CheckBox) findViewById(R.id.poolCB);
        skinnyGRB = (RadioButton) findViewById(R.id.sGRB);
        longThroRB = (RadioButton) findViewById(R.id.lTRB);
        locationSp = (Spinner) findViewById(R.id.locationSp);
        roomManSp = (Spinner) findViewById(R.id.roomSp);

        //adding the spinners to the array
        views[0] = locationSp;
        views[1] = roomManSp;

        //when the size spinner is clicked
        locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(locationSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(locationSp);
            }
        });

        //when the second size spinner is clicked
        roomManSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = roomManSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(roomManSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = roomManSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(roomManSp);
            }
        });
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            whatArrSt = "";
            boolean skinnyGate = skinnyGRB.isSelected();
            playB = playCB.isChecked();
            deckB = deckCB.isChecked();
            poolB = poolCB.isChecked();
            roomManSt = roomManSp.getSelectedItem().toString();
            locationSt = locationSp.getSelectedItem().toString();
            if(skinnyGate){
                easeSt = "Skinny Gate";
            }
            else{
                easeSt = "Long Thoroughfare";
            }
            //seeing what buttons are checked
            if(playB && deckB && poolB){
                //all three are checked
                whatArrSt = "Play Structure, Deck & Pool";
            }
            else{
                //its not all three
                if(playB && deckB){
                    //these two
                    whatArrSt = "Play Structure & Deck";
                }
                else if(playB && poolB){
                    //these two
                    whatArrSt = "Play Structure & Pool";
                }
                else if (deckB && poolB){
                    //these two
                    whatArrSt = "Deck & Pool";
                }
                else{
                    //its just one
                    if(playB){
                        whatArrSt = whatArrSt + "Play Structure ";
                    }
                    else if(deckB){
                        whatArrSt = whatArrSt + "Deck ";
                    }
                    else if(poolB){
                        whatArrSt = whatArrSt + "Pool";
                    }
                    else{
                        whatArrSt = "";
                    }
                }
            }
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Joint_Fill3.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("roomIndex", roomManSp.getSelectedItemPosition());
            intent.putExtra("locationIndex", locationSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(views);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }

}
