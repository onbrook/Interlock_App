package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.areViewsValid;
import static com.oep.interlock_app.ViewValidity.isViewValid;
import static com.oep.interlock_app.ViewValidity.removeOutline;
import static com.oep.interlock_app.ViewValidity.updateViewValidity;

public class Joint_Fill extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    //setting up the spinners and the array
    private View[] views = new View[3];
    private EditText lenInputET, widInputET;
    private Spinner patternSp;
    public static String lenInputSt, widInputSt, sizeJointSt, patternSt;
    private TextView sizeDisplayTV;
    private SeekBar sizeSB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint__fill);

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
        lenInputET = (EditText) findViewById(R.id.widtInput);
        widInputET = (EditText) findViewById(R.id.lenInput);
        patternSp = (Spinner) findViewById(R.id.patternSp);
        sizeDisplayTV = (TextView) findViewById(R.id.sizeDisplayTV);
        sizeSB = (SeekBar) findViewById(R.id.sizeJointsSB);

        //adding the spinners to the array
        views[0] = widInputET;
        views[1] = patternSp;
        views[2] = lenInputET;

        //setting the default value
        sizeDisplayTV.setText("Average");
        sizeJointSt = "Average";

        //EditText listeners
        lenInputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        lenInputET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });
        widInputET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        widInputET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });


        //when the second size spinner is clicked
        patternSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = patternSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(patternSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = patternSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(patternSp);
            }
        });

        sizeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){
                    //no roots
                    sizeDisplayTV.setText("Very Small");
                    sizeJointSt = "Very Small";
                }
                else if(progress==1){
                    //
                    sizeDisplayTV.setText("Small");
                    sizeJointSt = "Small";
                }
                else if(progress==2){
                    //
                    sizeDisplayTV.setText("Average");
                    sizeJointSt = "Average";
                }
                else if(progress==3){
                    //
                    sizeDisplayTV.setText("Large");
                    sizeJointSt = "Large";
                }
                else{
                    //must be the last one
                    sizeDisplayTV.setText("Very Large");
                    sizeJointSt = "Very Large";
                }
            }
        });
    }



    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            widInputSt = widInputET.getText().toString();
            lenInputSt = lenInputET.getText().toString();
            patternSt = patternSp.getSelectedItem().toString();
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Joint_Fill2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("pattenIndex", patternSp.getSelectedItemPosition());
            //start activity
            startActivity(intent);
        }else
            updateViewValidity(views);
    }

    //called when the back button in the title ba is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }


}
