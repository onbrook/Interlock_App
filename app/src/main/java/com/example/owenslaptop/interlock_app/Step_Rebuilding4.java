package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Step_Rebuilding4 extends AppCompatActivity {

    //NEED TO SETUP XML AND ADD THE OUTPUT FOR THE VARIABLE
    //ADD FUNCTION TO ALLOW FOR TIME OUTPUT - WAIT ON WHAT TO OUTPUT?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding4);

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

        //setting up the GUI components
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);



    }
    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(Step_Rebuilding4.this, Step_Rebuilding3.class));
        return true;
    }
}
