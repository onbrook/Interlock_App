package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class Wall_Rebuilding_Estimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_rebuilding_estimation);
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
        //get the data that was filled out in the layouts
        Bundle extras = getIntent().getExtras();
        int locationIndex = extras.getInt("locationIndex");
        int accessIndex = extras.getInt("accessIndex");
        int maneuverIndex = extras.getInt("maneuverIndex");
        double heightInput = extras.getDouble("heightInput");
        double lengthInput = extras.getDouble("lengthInput");
        boolean lineChecked = extras.getBoolean("lineChecked");
        int straightCurvedNum = extras.getInt("straightCurvedNum");
        int baseShiftIndex = extras.getInt("baseShiftIndex");
        boolean rootsChecked = extras.getBoolean("rootsChecked");
        boolean glueChecked = extras.getBoolean("glueChecked");
        boolean clipsChecked = extras.getBoolean("clipsChecked");
    }

    public void fabClicked(View fab){
        startActivity(new Intent(Wall_Rebuilding_Estimation.this, HomeScreen.class));
    }

    //called when the back button in the title bas is pressed
    public boolean onOptionsItemSelected(MenuItem item){
        goBack();
        return true;
    }

    //for android back button (the one on the bottom of the screen)
    public void onBackPressed(){
        goBack();
    }

    private void goBack(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(Wall_Rebuilding_Estimation.this, Wall_Rebuilding3.class);
        startActivity(intent);
    }
}
