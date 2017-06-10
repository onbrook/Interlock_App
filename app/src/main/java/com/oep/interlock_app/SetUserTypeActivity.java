package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.oep.owenslaptop.interlock_app.R;

import pub.devrel.easypermissions.EasyPermissions;

public class SetUserTypeActivity extends AppCompatActivity {

    private EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_type);
        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        if (! estimationSheet.isGooglePlayServicesAvailable())
            estimationSheet.acquireGooglePlayServices();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.next_fab);
        final RadioButton ownerRadioButton = (RadioButton) findViewById(R.id.owner_radio_button);
        final Activity activity = this;
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                if(ownerRadioButton.isChecked()) {
                    estimationSheet.startCreatingDatabase(new CreateDatabaseListener() {
                        @Override
                        public void whenFinished(boolean success, int errorId) {
                            if (success) {
                                //save user type
                                estimationSheet.setUserType(EstimationSheet.USER_TYPE_OWNER);
                                //go to add permissions activity
                                startActivity(new Intent(getApplicationContext(), AddDatabasePermissionsActivity.class));
                            } else{
                                if(errorId != EstimationSheet.NO_GOOGLE_PLAY_SERVICES_ERROR) {
                                    //show error dialog
                                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage("An error has occurred when trying to create " +
                                            "the database.");

                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                                }
                                            });
                                    alertDialog.show();
                                }
                            }
                        }
                    });
                }else { // Use an existing one is selected
                    startActivityForResult(new Intent(getApplicationContext(), EnterDatabaseIdActivity.class), EstimationSheet.REQUEST_GET_DATABASE_ID);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EstimationSheet.REQUEST_GET_DATABASE_ID && resultCode != RESULT_CANCELED)
            startActivity(new Intent(getApplicationContext(), HomeScreen.class)); // Go back to Home
        else {
            super.onActivityResult(requestCode, resultCode, data);
            estimationSheet.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed(){/* Do nothing */}

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, estimationSheet);
    }
}
