package com.example.owenslaptop.interlock_app;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Wall_Rebuilding extends AppCompatActivity {
    private FloatingActionButton fab;
    private Animation fabIn, fabOut;
    // getting to the job layout Views
    private Spinner locationSpinner;
    private Spinner accessibilitySpinner;
    private Spinner maneuverSpinner;
    //layout Views
    private EditText heightInput;
    private EditText lengthInput;
    //complications Views
    private Spinner baseShiftSpinner;
    private int currentLayoutNum;
    private boolean isFabIn = false;
    private List<Boolean> validViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start layout
        setUpLayout0();
        //setup back button in title bar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException npex){
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }catch(NullPointerException ex){
                //back button not supported
            }
        }
        //animations
        fabIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_in);
        fabOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_out);
    }



    public void fabClicked(View view){
        switch(currentLayoutNum){
            case 0:
                setUpLayout1();
                break;
            case 1:
                setUpLayout2();
                break;
            case 2:
                setUpEstimation();
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (currentLayoutNum){
            case 0:
                startActivity(new Intent(Wall_Rebuilding.this, HomeScreen.class));
                break;
            case 1:
                setUpLayout0();
                break;
            case 2:
                setUpLayout1();
                break;
        }
        return true;
    }

    private void setUpLayout0(){
        setContentView(R.layout.wall_rebuilding_tojob);
        currentLayoutNum = 0;
        // getting to the job layout Views
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        accessibilitySpinner = (Spinner) findViewById(R.id.accessibility_spinner);
        maneuverSpinner = (Spinner) findViewById(R.id.maneuver_spinner);

        fab = (FloatingActionButton) findViewById(R.id.wall_rebuilding_FAB);
        //fill validViews
        validViews = new ArrayList<>();
        validViews.add(false);
        validViews.add(false);
        validViews.add(false);

        //spinner listeners
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                updateViewValidity(locationSpinner, 0, findViewById(R.id.location_attention_textView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //will show error icon if empty
                updateViewValidity(locationSpinner, 0, findViewById(R.id.location_attention_textView));

            }
        });
        accessibilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                updateViewValidity(accessibilitySpinner, 1, findViewById(R.id.accessibility_attention_textView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //will show error icon if empty
                updateViewValidity(accessibilitySpinner, 1, findViewById(R.id.accessibility_attention_textView));

            }
        });
        maneuverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                updateViewValidity(maneuverSpinner, 2, findViewById(R.id.room_attention_textView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //will show error icon if empty
                updateViewValidity(maneuverSpinner, 2, findViewById(R.id.room_attention_textView));

            }
        });
    }

    private void setUpLayout1(){
        setContentView(R.layout.wall_rebuilding_layout);
        currentLayoutNum = 1;
        isFabIn = false;
        fab = (FloatingActionButton) findViewById(R.id.wall_rebuilding_FAB);
        //layout Views
        heightInput = (EditText) findViewById(R.id.height_input);
        lengthInput = (EditText) findViewById(R.id.length_input);
        //set up validViews
        validViews = new ArrayList<>();
        validViews.add(false);
        validViews.add(false);
        //done button listeners
        heightInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                updateViewValidity(heightInput, 0, findViewById(R.id.heightInput_attention_textView));
                return false;//hide keyboard
            }
        });
        heightInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(currentLayoutNum == 1)
                    updateViewValidity(heightInput, 0, findViewById(R.id.heightInput_attention_textView));
            }
        });
        lengthInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                updateViewValidity(lengthInput, 1, findViewById(R.id.lengthInput_attention_textView));
                return false;//hide keyboard
            }
        });
        lengthInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View errorEditText = findViewById(R.id.lengthInput_attention_textView);
                if(errorEditText != null)
                    updateViewValidity(lengthInput, 1, errorEditText);
            }
        });
    }

    private void setUpLayout2(){
        setContentView(R.layout.wall_rebuilding_complications);
        currentLayoutNum = 2;
        isFabIn = false;
        fab = (FloatingActionButton) findViewById(R.id.wall_rebuilding_FAB);
        //complications Views
        baseShiftSpinner = (Spinner) findViewById(R.id.baseShift_spinner);
        //setup validViews
        validViews = new ArrayList<>();
        validViews.add(false);
        //item changed listener
        baseShiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                updateViewValidity(baseShiftSpinner, 0, findViewById(R.id.baseShift_attention_textView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //will show error icon if empty
                updateViewValidity(baseShiftSpinner, 0, findViewById(R.id.baseShift_attention_textView));
            }
        });
    }

    private void setUpEstimation(){
        startActivity(new Intent(Wall_Rebuilding.this, HomeScreen.class));
    }

    private void updateViewValidity(View view,int viewNum, View attentionTextView){
        if(view instanceof EditText){
            EditText editText = (EditText) view;
            if(!editText.getText().toString().isEmpty()){
                attentionTextView.setVisibility(View.INVISIBLE);
                validViews.set(viewNum, true);
                System.out.println("are views valid? "+isFabIn);
                if(areViewsValid() && !isFabIn) {
                    fab.startAnimation(fabIn);
                    fab.setVisibility(View.VISIBLE);
                    fab.setEnabled(true);
                    isFabIn = true;
                }
            }else{
                if(isFabIn){
                    System.out.println("fab going out");
                    fab.startAnimation(fabOut);
                    fab.setVisibility(View.INVISIBLE);
                    fab.setEnabled(false);
                    isFabIn = false;
                }
                attentionTextView.setVisibility(View.VISIBLE);
                validViews.set(viewNum, false);
            }
        }else if(view instanceof Spinner){
            Spinner spinner = (Spinner) view;
            if(spinner.getSelectedItemPosition() != 0){
                attentionTextView.setVisibility(View.INVISIBLE);
                validViews.set(viewNum, true);
                if(areViewsValid() && !isFabIn) {
                    fab.startAnimation(fabIn);
                    fab.setVisibility(View.VISIBLE);
                    fab.setEnabled(true);
                    isFabIn = true;
                }
            }else {
                if (isFabIn) {
                    fab.startAnimation(fabOut);
                    fab.setVisibility(View.INVISIBLE);
                    fab.setEnabled(false);
                    isFabIn = false;
                }
                attentionTextView.setVisibility(View.VISIBLE);
                validViews.set(viewNum, false);
            }
            }else{
            System.err.println("ERROR: Wall_Rebuilding.updateViewValidity(View view) was called without being handed an EditText or a Spinner.");
        }
    }

    private boolean areViewsValid(){
        for(boolean valid:validViews){
            if(!valid)
                return false;
        }
        return true;
    }
}
