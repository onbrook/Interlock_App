package com.oep.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.oep.owenslaptop.interlock_app.R;

public class Interlock_Relaying_Complications extends AppCompatActivity {

    public static boolean pageCheck(Spinner size){
        //clearing all outlines
        ViewValidity.removeOutline(size);

        if(String.valueOf(size.getSelectedItem()).equals("Size of pavers")){
            ViewValidity.setupOutline(size);
            return false;
        }else{
            return true;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock_relaying_complications);
        final SeekBar sunkAmtSlider = (SeekBar)findViewById(R.id.sunkAmtSlider);
        final Spinner paverSize = (Spinner)findViewById(R.id.paverSizeSpin);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageCheck(paverSize);
                //add function to check for current layout
                if(check == true){
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_ToJob.class);
                    //save info to file here
                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
                }
            }
        });
    }
}
