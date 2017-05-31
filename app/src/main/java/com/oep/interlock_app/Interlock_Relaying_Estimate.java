package com.oep.interlock_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.oep.owenslaptop.interlock_app.R;

import org.w3c.dom.Text;

public class Interlock_Relaying_Estimate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlock__relaying__estimate);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npex) {
            try {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException ex) {
                //back button not supported
            }
        }
        final TextView output = (TextView)findViewById(R.id.output);

        output.setText("Width: " + Interlock_Relaying.widthOut + "\n" +
                "Length: " + Interlock_Relaying.lengthOut + "\n" +
                "Pattern: " + Interlock_Relaying.ptrnOut + "\n" +
                "Shape: " + Interlock_Relaying.shapeOut + "\n" +
                Interlock_Relaying.edgeOut + "\n" +
                "The interlock... " + Interlock_Relaying_Complications.sunkOut + "\n" +
                "The interlock is... " + Interlock_Relaying_Complications.paveSizeOut + "\n" +
                "There are... " + Interlock_Relaying_Complications.weedOut + "\n" +
                "The joint fill is... " + Interlock_Relaying_Complications.fillOut + "\n" +
                Interlock_Relaying_ToJob.locOut + "\n" +
                Interlock_Relaying_ToJob.moveOut + "\n" +
                Interlock_Relaying_ToJob.accOut);
                //estimate here
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
