package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Step_Rebuilding4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding4);

        //setting up the GUI components
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);


        //setting up the button to get and check input when clicked
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //this will get and store the variables

                //moving to the next page
                startActivity(new Intent(Step_Rebuilding4.this, HomeScreen.class));

            }
        });
    }
}
