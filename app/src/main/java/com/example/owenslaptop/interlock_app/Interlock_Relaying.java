package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import static com.example.owenslaptop.interlock_app.ViewValidity.removeOutline;
import static com.example.owenslaptop.interlock_app.ViewValidity.setupOutline;
import static com.example.owenslaptop.interlock_app.ViewValidity.updateViewValidity;

public class Interlock_Relaying extends AppCompatActivity {

    public static boolean pageOneCheck(EditText len, EditText wid,
                                       Spinner ptrn, Spinner shape){
        //clearing all outlines
        removeOutline(len);
        removeOutline(wid);
        removeOutline(ptrn);
        removeOutline(shape);
        int falseCheck = 0;

        //checking all elements
        if(!TextUtils.isEmpty(String.valueOf(len.getText()))){
            if(Double.parseDouble(String.valueOf((len.getText()))) == 0){
                setupOutline(len);
                falseCheck = 1;
            }
        }else if(TextUtils.isEmpty(String.valueOf(len.getText()))){
            setupOutline(len);
            falseCheck = 1;
        }

        if(!TextUtils.isEmpty(String.valueOf(wid.getText()))){
            if(Double.parseDouble(String.valueOf(wid.getText())) == 0){
                setupOutline(wid);
                falseCheck = 1;
            }
        }else if(TextUtils.isEmpty(String.valueOf(wid.getText()))){
            setupOutline(wid);
            falseCheck = 1;
        }

        if(String.valueOf(ptrn.getSelectedItem()).equals("Pattern Type")){
            setupOutline(ptrn);
            falseCheck = 1;
        }
        if(String.valueOf(shape.getSelectedItem()).equals("Shape of the job")){
            setupOutline(shape);
            falseCheck = 1;
        }


        if (falseCheck == 1){
            return false;
        }else{
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying_layout);
        final EditText length = (EditText)findViewById(R.id.lenInput);
        final EditText width = (EditText)findViewById(R.id.widInput);
        final Spinner patternTypeSpin = (Spinner)findViewById(R.id.ptrnTypeSpin);
        final FloatingActionButton nextBtn = (FloatingActionButton)findViewById(R.id.nextBtn);
        final Spinner jobShape = (Spinner)findViewById(R.id.jobShapeSpin);
        final CheckBox edgeCheck = (CheckBox)findViewById(R.id.edgeCheck);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = pageOneCheck(length, width, patternTypeSpin, jobShape);
                //add function to check for current layout
                if(check == true){
                    Intent nextPg = getIntent();
                    nextPg.setClass(getApplicationContext(), Interlock_Relaying_Complications.class);
                    //save info to file here
                    startActivity(nextPg);
                }else{
                    System.out.print("Error");
                }
            }
        });
    }


}
