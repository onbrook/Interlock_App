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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.drive.model.Permission;
import com.oep.owenslaptop.interlock_app.R;

import java.util.ArrayList;
import java.util.List;



import pub.devrel.easypermissions.EasyPermissions;

public class ActivityDatabaseAccounts extends AppCompatActivity {

    private EstimationSheet estimationSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_accounts);

        final FloatingActionButton addFAB = (FloatingActionButton) findViewById(R.id.add_fab);

        estimationSheet = new EstimationSheet(EstimationSheet.ID_NOT_APPLICABLE, this);
        showEmails();

        addFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                startActivity(new Intent(getApplicationContext(), AddDatabasePermissionsActivity.class));
            }
        });
    }

    private void showEmails(){
        final Activity activity = this;
        final TextView textView = (TextView) findViewById(R.id.no_accounts_text_view);
        final ListView accountsListView = (ListView) findViewById(R.id.accounts_list_view);
        estimationSheet.startGettingPermissions(new GetPermissionsListener() {
            @Override
            public void whenFinished(boolean success, final List<Permission> permissions) {
                if(success) {
                    List<String> emailAddresses = new ArrayList<>();
                    for (Permission permission : permissions)
                        emailAddresses.add(permission.getEmailAddress());

                    if(emailAddresses.size() == 0)
                        textView.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, emailAddresses);
                    accountsListView.setAdapter(adapter);
                    accountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            //show alert dialog; "would you like to remove the permissions that this account has?"
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Remove permissions");
                            builder.setMessage("would you like to remove the permissions that" +
                                    " the account "+permissions.get(position).getEmailAddress()+"" +
                                    " has to access the database?");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            estimationSheet.startRemovingPermission(permissions.get(position), new RemovePermissionListener() {
                                                @Override
                                                public void whenFinished(boolean success) {
                                                    showEmails();
                                                }
                                            });
                                        }
                                    });
                            builder.setNegativeButton("CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                } else
                    textView.setVisibility(View.VISIBLE);

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
