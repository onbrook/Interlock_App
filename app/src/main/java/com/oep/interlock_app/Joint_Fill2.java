package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill2 extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[1];
    private CheckBox playCB, deckCB, poolCB;
    public static String locationSt, roomManSt, easeSt, whatArrSt;
    private Spinner locationSp;
    public static boolean playB, deckB, poolB;
    private SeekBar roomMarSB, easeAccSB;
    private TextView roomManDisplayTV, easeDisplayTV;


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
        roomMarSB = (SeekBar) findViewById(R.id.roomManSB);
        easeAccSB = (SeekBar) findViewById(R.id.easeSB);
        easeDisplayTV = (TextView) findViewById(R.id.easeDisplayTV);
        roomManDisplayTV = (TextView) findViewById(R.id.roomDisplayTV);
        locationSp = (Spinner) findViewById(R.id.locationSp);

        views[0] = locationSp;


        //setting the default values
        easeDisplayTV.setText("Average");
        easeSt = "Average";
        roomManDisplayTV.setText("Average");
        roomManSt = "Average";



        //ease of access
        easeAccSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    //no roots
                    easeDisplayTV.setText("Easy to Access");
                    easeSt = "Easy to Access";
                }
                else if (progress == 1) {
                    //
                    easeDisplayTV.setText("Average");
                    easeSt = "Average";
                }
                else {
                    //must be the last one
                    easeDisplayTV.setText("Hard to Access");
                    easeSt = "Hard to Access";
                }
            }
        });

        //room to man seekbar
        roomMarSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    //no roots
                    roomManDisplayTV.setText("Almost no Room");
                    roomManSt = "Almost no Room";
                }
                else if (progress == 1) {
                    //
                    roomManDisplayTV.setText("Bit Tight");
                    roomManSt = "Bit Tight";
                }
                else if (progress == 2) {
                    //
                    roomManDisplayTV.setText("Average");
                    roomManSt = "Average";
                }
                else if (progress == 3) {
                    //
                    roomManDisplayTV.setText("Good amount");
                    roomManSt = "Good amount";
                }
                else {
                    //must be the last one
                    roomManDisplayTV.setText("Lots of Space");
                    roomManSt = "Lots of Space";
                }
            }
        });

        //when the size spinner is clicked
        locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(locationSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = locationSp.getSelectedItemPosition();
                if (index != 0)
                    ViewValidity.updateViewValidity(locationSp);
            }
        });

    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(ViewValidity.areViewsValid(views)) {
            //getting the values
            whatArrSt = "";
            playB = playCB.isChecked();
            deckB = deckCB.isChecked();
            poolB = poolCB.isChecked();
            locationSt = locationSp.getSelectedItem().toString();
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
            //getting the input from the user
            locationSt = locationSp.getSelectedItem().toString();
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Joint_Fill3.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("locationIndex", locationSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            ViewValidity.updateViewValidity(views);
    }

    //called when the back button in the title ba is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
