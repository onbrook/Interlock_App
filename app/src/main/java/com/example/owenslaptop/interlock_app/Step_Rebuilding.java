package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class Step_Rebuilding extends AppCompatActivity {

    /*

    Coded by: Owen Brook

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__rebuilding);

        //setting up the GUI components
        final Button nextBtn = (Button) findViewById(R.id.nextBtn);
        final CheckBox straightCB = (CheckBox) findViewById(R.id.straightCB);
        final CheckBox curvedCB = (CheckBox) findViewById(R.id.curvedCB);
        final Spinner sizeJobSp = (Spinner) findViewById(R.id.sizeOfJobSp);
        final Spinner sizeJob2Sp = (Spinner) findViewById(R.id.sizeOfJob2Sp);
        final Spinner amountShiftSp = (Spinner) findViewById(R.id.amountBaseShiftSp);
        final TextView errorTV = (TextView) findViewById(R.id.errorTV);

        //creating the arrays to hold the spinner objects
        final String[] sizeArr = {"Size of the job (l)", "Shorter", "Average", "Longer"};
        final String[] size2Arr = {"Size of the job (h)", "Lower", "Average", "Higher", "Over 4 feet"};
        final String[] baseShiftArr = {"Amount of base shift", "None", "Lower"};

        //setting the options to the spinners
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sizeArr);
        sizeJobSp.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, size2Arr);
        sizeJob2Sp.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, baseShiftArr);
        amountShiftSp.setAdapter(adapter3);

        //setting up the button to get and check input when clicked
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //this will get and store the variables

                //setting up the variables for storing the input
                boolean straight, curved;
                String size, size2, amountShift;

                //seeing if we have input from the spinners
                size = sizeJobSp.getSelectedItem().toString();
                size2 = sizeJob2Sp.getSelectedItem().toString();
                amountShift = amountShiftSp.getSelectedItem().toString();

                String sizeVal = sizeArr[0].toString();
                String size2Val = size2Arr[0].toString();
                String amountShiftVal = baseShiftArr[0].toString();
                straight = straightCB.isChecked();
                curved = curvedCB.isChecked();

                if(size.equals(sizeVal)){
                    //one of the inputs are incorrect
                    errorTV.setText("Please select a option from the 'Size of the job (l)' drop down");
                }
                else if(size2.equals(size2Val)){
                    //one of the inputs are incorrect
                    errorTV.setText("Please select a option from the 'Size of the job (h)' drop down");
                }
                else if(amountShift.equals(amountShiftVal)){
                    //one of the inputs are incorrect
                    errorTV.setText("Please select an option from the 'Amount of base Shift' drop down");
                }
                else if (straight && curved) {
                    //both are checked
                    errorTV.setText("Both check boxes cannot be selected");
                }
                else if(!straight && !curved){
                    //both are not checked
                    errorTV.setText("Please check one of the check boxes");
                }
                else{
                    //we are good to go on to the next page
                    errorTV.setText("");

                    //moving to the next page
                    startActivity(new Intent(Step_Rebuilding.this, Step_Rebuilding2.class));
                }
            }
        });
    }
}
