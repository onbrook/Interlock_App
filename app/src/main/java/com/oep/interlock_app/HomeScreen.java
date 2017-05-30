package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oep.owenslaptop.interlock_app.R;

public class HomeScreen extends AppCompatActivity {

    /*

    Names: Ethan McIntyre, Owen Brook & Peter Lewis

    Description:

    Date Started: April 7th, 2017

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setTitle("Home");
/*
        //check if the user is new and if so, go to correct activity
        EstimationSheet estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if(!estimationSheet.doesUserHaveRole())
            startActivity(new Intent(getApplicationContext(), SetUserTypeActivity.class));
        else if (!estimationSheet.isDatabaseIdSaved())
            if(estimationSheet.isUserOwner())
                estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
                    @Override
                    public void whenFinished(boolean success) {

                    }
                });
            else
                startActivity(new Intent(getApplicationContext(), EnterDatabaseIdActivity.class));*/

        //setting up the buttons
        Button estimateBtn = (Button)findViewById(R.id.estimationBtn);

        //moving to the estimate page
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Step_Rebuilding.class));
            }
        });
    }
}
