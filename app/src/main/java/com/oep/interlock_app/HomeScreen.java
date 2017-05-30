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

        //setting up the buttons
        Button estimateBtn = (Button)findViewById(R.id.estimatePageBtn);


        //moving to the estimate page
        estimateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Step_Rebuilding.class));
            }
        });


    }
}
