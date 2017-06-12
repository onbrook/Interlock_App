package com.oep.interlock_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

public class DatabaseEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_editor);
        final TextView displayData = (TextView)findViewById(R.id.dataDisplay);
        displayData.setText(DatabaseManagement.editorJobData);
    }
}
