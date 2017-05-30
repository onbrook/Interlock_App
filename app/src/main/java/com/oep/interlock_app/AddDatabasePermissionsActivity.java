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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oep.owenslaptop.interlock_app.R;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddDatabasePermissionsActivity extends AppCompatActivity {

    EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_database_permissions);

        final EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
        Button addButton = (Button) findViewById(R.id.add_button);
        final TextView outputTextView = (TextView) findViewById(R.id.output_tv);
        FloatingActionButton okFab = (FloatingActionButton) findViewById(R.id.ok_fab);

        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);

        final List<String> emails = new ArrayList<>();

        final Activity activity = this;

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                String email = emailEditText.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(AddDatabasePermissionsActivity.this, "Please use Gmail accounts.", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    emails.add(email);

                    String emailsStr = "Emails:\n";
                    for (String em : emails)
                        emailsStr += em+"\n";

                    outputTextView.setText(emailsStr);
                    emailEditText.setText("");

                }
            }
        });

        okFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //add email to List of emails
                String email = emailEditText.getText().toString();
                if(!email.equals(""))
                    emails.add(email);

                // Process emails
                if(emails.size() == 0){
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                } else
                    estimationSheet.startAddingPermissions(emails, new AddPermissionsListener() {
                        @Override
                        public void whenFinished(boolean success) {
                            if(success) {
                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                alertDialog.setTitle("Success");
                                alertDialog.setMessage("The accounts have successfully been given " +
                                        "permission to access the database. Email notifications have " +
                                        "been sent to the requested emails.");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                            }
                                        });
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("An error has occurred when trying to give the" +
                                        " requested Google Accounts access to the database. This could" +
                                        " have been caused from some of the emails which where entered being" +
                                        " non-existent");

                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                            }
                                        });
                            }


                        }
                    });
            }
        });

    }

    private void appendEmail(String email, List<String> emails){

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
