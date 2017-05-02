package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Step_Rebuilding2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding2);

        //setting up the GUI componenets
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);
        final Spinner locationSp = (Spinner) findViewById(R.id.locationSp);
        final Spinner roomSp = (Spinner) findViewById(R.id.roomSp);
        final RadioButton longTRB = (RadioButton) findViewById(R.id.lTRB);
        final RadioButton skinnyGRB = (RadioButton) findViewById(R.id.sGRB);
        final TextView errorTV = (TextView) findViewById(R.id.errorTV);

        //creating the arrays to hold the spinner objects
        final String[] locationArr = {"Location", "Front-yard", "Back-yard"};
        final String[] roomArr = {"Amount of room", "Average", "Less than average", "Very little"};

        //setting the options to the spinners
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locationArr);
        locationSp.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, roomArr);
        roomSp.setAdapter(adapter2);

        //setting up the button to get and check input when clicked
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //this will get and store the variables
                boolean longT, skinnyG;
                String locationVal, roomVal;

                //getting the input
                longT = longTRB.isChecked();
                skinnyG = skinnyGRB.isChecked();
                locationVal = locationSp.getSelectedItem().toString();
                roomVal = roomSp.getSelectedItem().toString();

                //the default values
                String roomSt, locationSt;
                roomSt = roomArr[0];
                locationSt = locationArr[0];

                //seeing if we have the input
                if(locationVal.equals(locationSt)){
                    //we dont have a location value
                    errorTV.setText("Please select a location value");
                }
                else if (longT && skinnyG) {
                    //both are checked
                    errorTV.setText("Both check boxes cannot be selected");
                }
                else if(!longT && !skinnyG){
                    //both are not checked
                    errorTV.setText("Please check one of the check boxes");
                }
                else if(roomVal.equals(roomSt)){
                    //we dont have a room value
                    errorTV.setText("Please select an amount of room value");
                }
                else{
                    //we are good to go on to the next page
                    errorTV.setText("");

                    //moving to the next page
                    startActivity(new Intent(Step_Rebuilding2.this, Step_Rebuilding3.class));
                }
            }
        });
    }
}
