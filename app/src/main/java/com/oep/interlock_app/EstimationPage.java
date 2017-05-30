package com.oep.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oep.owenslaptop.interlock_app.R;

public class EstimationPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation_page);

        Button stepBtn = (Button)findViewById(R.id.stepRebuildBtn);
        Button wallBtn = (Button)findViewById(R.id.wallRebuildBtn);
        Button relayBtn = (Button)findViewById(R.id.relayBtn);
        Button jointBtn = (Button)findViewById(R.id.jointFillBtn);
        Button sealBtn = (Button)findViewById(R.id.cleanSealBtn);
        Button homeBtn = (Button) findViewById(R.id.homeScreenBtn);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, HomeScreen.class));
            }
        });

        stepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, Step_Rebuilding.class));
            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, Wall_Rebuilding.class));
            }
        });

        relayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, Interlock_Relaying.class));
            }
        });

        jointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, Joint_Fill.class));
            }
        });

        sealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EstimationPage.this, CleaningSealing.class));
            }
        });
    }
}
