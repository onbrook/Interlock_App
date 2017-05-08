package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import static com.example.owenslaptop.interlock_app.ViewValidity.*;


public class Wall_Rebuilding2 extends AppCompatActivity {
    EditText heightInput;
    EditText lengthInput;
    CheckBox lineCheckBox;
    RadioButton straightRadioButton;
    RadioButton curvedRadioButton;
    View[] views = new View[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_layout);
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
        //layout Views
        heightInput = (EditText) findViewById(R.id.height_input);
        lengthInput = (EditText) findViewById(R.id.length_input);
        lineCheckBox = (CheckBox) findViewById(R.id.hard_line_CheckBox);
        straightRadioButton = (RadioButton) findViewById(R.id.wallStraight_radioButton);
        curvedRadioButton = (RadioButton) findViewById(R.id.wallCurved_radioButton);

        views[0] = heightInput;
        views[1] = lengthInput;
        //EditText listeners
        heightInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        heightInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });
        lengthInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                removeOutline(v);
                return !isViewValid(v);//keep up keyboard
            }
        });
        lengthInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                removeOutline(v);
            }
        });
    }

    public void fabClicked(View view){
        if (areViewsValid(views)) {
            //start activity (getIntent to save extras)
            Intent intent = getIntent();
            //update class
            intent.setClass(getApplicationContext(), Wall_Rebuilding3.class);
            //extras--for passing data
            intent.putExtra("heightInput", Double.parseDouble(heightInput.getText().toString()));
            intent.putExtra("lengthInput", Double.parseDouble(lengthInput.getText().toString()));
            intent.putExtra("lineChecked", lineCheckBox.isChecked());
            if(straightRadioButton.isChecked())
                intent.putExtra("straightCurvedNum", 0);
            else
                intent.putExtra("straightCurvedNum", 1);
            startActivity(intent);
        }else
            updateViewValidity(views);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
