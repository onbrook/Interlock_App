package com.oep.interlock_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by: Peter Lewis
 * Date: 6/8/17
 */

public class ILDialog {
    public final static int EXIT_DIALOG_SAVE = 0;
    public final static int EXIT_DIALOG_WARNING = 1;
    public static void showExitDialogWarning(final Activity activity, final Intent nextActivityIntent){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Delete data?");
            builder.setMessage("If you leave this page all of the data you have entered here will" +
                    " be lost. Are you sure you would like to leave?");
            builder.setPositiveButton("CONTINUE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            activity.startActivity(nextActivityIntent);
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

    public static void showExitDialogSave(final Activity activity, final Intent nextActivityIntent, final EstimationSheet estimationSheet, final List<Object> newData){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete data?");
        builder.setMessage("If you leave this page none of the data that you have entered here" +
                " will be saved. Saving data helps improve estimations. Are you sure that you" +
                " would like to delete it?");
        builder.setNeutralButton("CONTINUE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.startActivity(nextActivityIntent);
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        estimationSheet.startAddingEstimation(newData, new AddEstimationListener() {
                            @Override
                            public void whenFinished(boolean success) {
                                if(success)
                                    activity.startActivity(nextActivityIntent);
                                else{
                                    final AlertDialog errorDialog = new AlertDialog.Builder(activity).create();
                                    errorDialog.setMessage("An error has occured when trying to save data.");
                                    errorDialog.setTitle("Error");
                                    errorDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            errorDialog.dismiss();
                                            activity.startActivity(nextActivityIntent);
                                        }
                                    });
                                    errorDialog.show();
                                }
                            }
                        });
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
