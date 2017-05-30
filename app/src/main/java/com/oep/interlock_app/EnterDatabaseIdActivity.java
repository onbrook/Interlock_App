package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

import java.io.FileOutputStream;

import pub.devrel.easypermissions.EasyPermissions;

public class EnterDatabaseIdActivity extends AppCompatActivity {

    private EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_database_id);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ok_fab);
        final EditText editText = (EditText) findViewById(R.id.edit_text);
        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                final String input = editText.getText().toString();
                if(input.equals(""))
                    Toast.makeText(EnterDatabaseIdActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                else{
                    // Check if id is valid
                    estimationSheet.startCheckingDatabaseIdValidity(input, new CheckDatabaseIdValidityListener() {
                        @Override
                        public void whenFinished(boolean success, Boolean validId) {
                            if(!success)
                                showDialog("Error", "An error has occurred. This could be caused from the database not existing.");
                            else if(!validId)
                                showDialog("Invalid ID", "The database ID which was entered you do not have access to.");
                            else { // ID is correct
                                saveId(input);
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveId(String databaseId){
        try {
            FileOutputStream fos = openFileOutput(EstimationSheet.DATABASE_ID_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(databaseId.getBytes());
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDialog(String title, String body){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(body);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        estimationSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, estimationSheet);
    }
}
