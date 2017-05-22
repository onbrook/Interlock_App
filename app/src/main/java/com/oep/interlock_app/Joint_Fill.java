package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private View[] views = new View[4];
    private EditText lenInputET, widInputET;
    private Spinner sizeJointSp, patternSp;
    public static String lenInputSt, widInputSt, sizeJointSt, patternSt;


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
        lenInputET = (EditText) findViewById(R.id.lenInput);
        widInputET = (EditText) findViewById(R.id.widInput);
        sizeJointSp = (Spinner) findViewById(R.id.sizeJointSp);
        patternSp = (Spinner) findViewById(R.id.patternSp);

        //adding the spinners to the array
        views[0] = sizeJointSp;
        views[1] = patternSp;
        views[2] = lenInputET;
        views[3] = widInputET;

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

        //when the size spinner is clicked
        sizeJointSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
                int index = sizeJointSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJointSp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                int index = sizeJointSp.getSelectedItemPosition();
                if (index != 0)
                    updateViewValidity(sizeJointSp);
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
    }

    //when the FAB is clicked
    public void fabClicked(View view){
        if(areViewsValid(views)) {
            //getting the values
            widInputSt = widInputET.getText().toString();
            lenInputSt = lenInputET.getText().toString();
            sizeJointSt = sizeJointSp.getSelectedItem().toString();
            patternSt = patternSp.getSelectedItem().toString();
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), Joint_Fill2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("pattenIndex", patternSp.getSelectedItemPosition());
            intent.putExtra("jointIndex", sizeJointSp.getSelectedItemPosition());
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
