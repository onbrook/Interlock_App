package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

/*
 *By: Peter Lewis
 *Date: May 11, 2017
 */

public class CleaningSealingEstimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_sealing_estimation);
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
        // get TextViews
        TextView dimensionsOut = (TextView) findViewById(R.id.dimensions_out);
        TextView angleOut = (TextView) findViewById(R.id.angle_out);
        TextView stainOut = (TextView) findViewById(R.id.stain_out);
        TextView stainTypeOut = (TextView) findViewById(R.id.stain_type_out);
        TextView stainPercentOut = (TextView) findViewById(R.id.stain_percent_out);
        TextView ageOut = (TextView) findViewById(R.id.age_out);
        TextView otherCompOut = (TextView) findViewById(R.id.comp_out);
        TextView finalOut = (TextView) findViewById(R.id.final_out);
        // get the data that was filled out in the layouts and put it in the TextViews
        Bundle extras = getIntent().getExtras();
        double heightDouble = extras.getDouble("height_double");
        double lengthDouble = extras.getDouble("length_double");
        dimensionsOut.setText(heightDouble+"ft x "+lengthDouble+"ft");
        double angleDouble = extras.getDouble("angle_double");
        if(angleDouble==0 || angleDouble==180)
            angleOut.setText("Horizontal");
        else if(angleDouble==90)
            angleOut.setText("Vertical");
        else
            angleOut.setText(angleDouble+"°");
        boolean stainChecked = extras.getBoolean("stain_checked");
        if(stainChecked) {
            stainOut.setText("Yes");
            String stainTypeStr = extras.getString("stain_type_str");
            stainTypeOut.setText(stainTypeStr);
            int stainPercent = extras.getInt("stain_percent");
            stainPercentOut.setText(stainPercent+"%");
        }else {// stain not checked
            stainOut.setText("No");
            stainTypeOut.setText("--");
            stainPercentOut.setText("--");
        }
        int ageNum = extras.getInt("age_num");
        switch(ageNum){
            case 0:
                ageOut.setText("Less than 6 months");
                break;
            case 1:
                ageOut.setText("6 months");
                break;
            case 2:
                ageOut.setText("1 year");
                break;
            case 3:
                ageOut.setText("1.5 years");
                break;
            case 4:
                ageOut.setText("2 years");
                break;
            case 5:
                ageOut.setText("more than 2 years");
        }
        int otherCompNum = extras.getInt("other_comp_num");
        switch(otherCompNum){
            case 0:
                otherCompOut.setText("None");
                break;
            case 1:
                otherCompOut.setText("Some");
                break;
            case 2:
                otherCompOut.setText("Moderate");
                break;
            case 3:
                otherCompOut.setText("Lots more");
                break;
        }
        finalOut.setText("Final Estimate: 0 hours and 0 minuets");
    }

    public void fabClicked(View fab){
        startActivity(new Intent(getApplicationContext(), gsheet_test.class));
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
