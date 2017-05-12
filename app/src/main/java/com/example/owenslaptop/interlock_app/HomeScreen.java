package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        Button btn = (Button)findViewById(R.id.stepRebuildBtn);
        Button btn2 = (Button)findViewById(R.id.wallRebuildBtn);
        Button btn3 = (Button)findViewById(R.id.relayBtn);
        Button jointFill = (Button)findViewById(R.id.jointFillBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Step_Rebuilding.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Wall_Rebuilding.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Interlock_Relaying.class));
            }
        });

        jointFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Joint_Fill.class));
            }
        });


    }

    public void cleaningSealingBtnClicked(View v){
        startActivity(new Intent(HomeScreen.this, CleaningSealing.class));
    }

}
