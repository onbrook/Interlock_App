package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import static com.oep.interlock_app.ViewValidity.updateViewValidity;

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealing extends AppCompatActivity {
    // getting to the job layout Views
    private EditText heightEditText;
    private EditText lengthEditText;
    private SeekBar angleSeekBar;
    private View[] views = new View[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing);
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
        //get views
        heightEditText = (EditText) findViewById(R.id.height_input);
        lengthEditText = (EditText) findViewById(R.id.length_input);
        angleSeekBar = (SeekBar) findViewById(R.id.angle_seek_bar);
        final TextView angleTV = (TextView) findViewById(R.id.angle_tv);
        //setup views[]
        views[0] = heightEditText;
        views[1] = lengthEditText;
        //SeekBar listener
        angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/* do nothing*/}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // update angleTV
                if(progress==0){
                    angleTV.setText("Horizontal");
                }else if(progress<=55 && progress>=45){//range - to make it "sticky" when it is near 50%
                    seekBar.setProgress(50);
                    angleTV.setText("Vertical");
                }else {
                    String angle = String.format("%.2f", progress*1.8) + "°";
                    angleTV.setText(angle);
                }
            }
        });
        //EditText listeners
        heightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(ViewValidity.isViewValid(heightEditText))
                    ViewValidity.removeOutline(v);
                return !ViewValidity.isViewValid(v);//keep up keyboard
            }
        });
        lengthEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(ViewValidity.isViewValid(heightEditText))
                    ViewValidity.removeOutline(v);
                return !ViewValidity.isViewValid(v);//keep up keyboard
            }
        });
    }

    public void fabClicked(View view){
        if(ViewValidity.areViewsValid(views)) {
            //create a new intent (you do not get the current one because we do not need any
            // information from the home screen)
            Intent intent = new Intent(getApplicationContext(), CleaningSealing2.class);
            //this removes the animation
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //extras--for passing data
            intent.putExtra("height_double", Double.parseDouble(heightEditText.getText().toString()));
            intent.putExtra("length_double", Double.parseDouble(lengthEditText.getText().toString()));
            intent.putExtra("angle_double", Double.parseDouble(String.format("%.2f", angleSeekBar.getProgress()*1.8)));
            //start activity
            startActivity(intent);
        }else
            ViewValidity.updateViewValidity(views);
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
