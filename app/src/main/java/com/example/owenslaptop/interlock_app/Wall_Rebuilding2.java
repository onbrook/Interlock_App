package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import static com.example.owenslaptop.interlock_app.methods.*;


public class Wall_Rebuilding2 extends AppCompatActivity {
    EditText heightInput;
    EditText lengthInput;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_layout);
        //layout Views
        heightInput = (EditText) findViewById(R.id.height_input);
        lengthInput = (EditText) findViewById(R.id.length_input);
        fab = (FloatingActionButton) findViewById(R.id.wall_rebuilding_FAB);
        //done button listeners
        heightInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean isViewValid = isViewValid(heightInput);//in methods
                //if(isViewValid){
                    heightInput.setBackgroundResource(R.drawable.no_outline_edit_text);
                //}
                return !isViewValid;//keep up keyboard
            }
        });
        heightInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isViewValid(heightInput);
            }
        });
        lengthInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                isViewValid(lengthInput);
                heightInput.setBackgroundResource(R.drawable.red_outline_edit_text);
                return false;//keep up keyboard
            }
        });
        lengthInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isViewValid(lengthInput);
            }
        });
    }

    public void fabClicked(View view){
        startActivity(new Intent(Wall_Rebuilding2.this, Wall_Rebuilding3.class));
    }
}
