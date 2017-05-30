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

        setContentView(R.layout.activity_home_screen);
        setTitle("Home");
        Button stepBtn = (Button)findViewById(R.id.stepRebuildBtn);
        Button wallBtn = (Button)findViewById(R.id.wallRebuildBtn);
        Button relayBtn = (Button)findViewById(R.id.relayBtn);
        Button jointBtn = (Button)findViewById(R.id.jointFillBtn);
        Button sealBtn = (Button)findViewById(R.id.cleanSealBtn);

        //setting up the buttons
        Button estimateBtn = (Button)findViewById(R.id.estimatePageBtn);


        //moving to the estimate page
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Step_Rebuilding.class));
            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Wall_Rebuilding.class));
            }
        });

        relayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Interlock_Relaying.class));
            }
        });

        jointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Joint_Fill.class));
            }
        });

        sealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, CleaningSealing.class));
            }
        });


    }
}
