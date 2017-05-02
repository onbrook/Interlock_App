package com.example.owenslaptop.interlock_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Interlock_Relaying extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying_layout);
        final EditText length = (EditText)findViewById(R.id.lenInput);
        final EditText width = (EditText)findViewById(R.id.widInput);
    }
}
