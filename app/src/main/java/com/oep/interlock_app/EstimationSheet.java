package com.oep.interlock_app;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by: Peter Lewis
 * Date: 5/21/17
 */

public class EstimationSheet extends Activity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential googleAccountCredential;
    ProgressDialog progressDialog;

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private int sheetId;
    private Activity activity;

    public static final int WALL_REBUILDING_ID = 0;
    public static final int CLEANING_SEALING_ID = 1;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };

    /**
     *
     * @param sheetId The ID of the Google Sheet. Use the ID's in this file (like CLEANING_SEALING_ID)
     * @param activity The Activity that uses the sheet. Used for dialogs etc. You can usually use 'this'
     */

    public EstimationSheet(int sheetId, Activity activity){
        this.sheetId = sheetId;
        this.activity = activity;
        //set up progress dialog
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Collecting data for estimation...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        // Initialize credentials and service object.
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                this.activity, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public void getTimeEstimation(){
        System.out.println(getResultsFromApi());
    }

    public void addEstimation(){

    }

    public void removeEstimation(int estimationId){

    }

    /**
     *
     * @param estimationId Id of the estimation that is to have the time changed.
     * @param time How long the job took in hours.
     */
    public void setEstimationTime(int estimationId, double time){

    }

    private boolean toBoolean(String str){
        if(str.toLowerCase().equals("true"))
            return true;
        else if (str.toLowerCase().equals("false"))
            return false;
        else
            throw new IllegalArgumentException("'"+str+"' cannot be converted to a boolean.");
    }

    private void showErrorDialog(String error){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private List<List<Object>> getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()){
            acquireGooglePlayServices();
            return null;
        }
        else if (googleAccountCredential.getSelectedAccountName() == null) {
            chooseAccount();
            getResultsFromApi();
        }
        else if (! isDeviceOnline()) {
            showErrorDialog("No network connection available.");
            return null;
        }
        else{
            progressDialog.show();
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.sheets.v4.Sheets service = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, googleAccountCredential)
                    .build();
            try {
                String spreadsheetId = "1zOQSqnzIM7Ku_JhcgMQH4Kp_sLrZRw1SDzl2nyDEosc";
                String range = "wall_rebuilding!A2:N";
                ValueRange response = service.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute();
                progressDialog.hide();
                return response.getValues();
            } catch (Exception e) {
                progressDialog.hide();
                if (e instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) e)
                                    .getConnectionStatusCode());
                } else if (e instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) e).getIntent(),
                            gsheet_test.REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog("The following error occurred:\n"
                            + e.getMessage());
                }
                progressDialog.hide();
                return null;
            }
        }
        return null;
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                activity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = activity.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                googleAccountCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                activity.startActivityForResult(
                        googleAccountCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    showErrorDialog(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        googleAccountCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, activity);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode))
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            String spreadsheetId = "1zOQSqnzIM7Ku_JhcgMQH4Kp_sLrZRw1SDzl2nyDEosc";
            String range = "wall_rebuilding!A2:N";
            List<String> results = new ArrayList<String>();
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<Sheet> sheets = this.mService.spreadsheets().get(spreadsheetId).setIncludeGridData (true)
                    .execute().getSheets();
            Sheet cleaningSealingSheet = sheets.get(0);
            Sheet wallRebuildingSheet = sheets.get(1);
            List<List<Object>> values = response.getValues();
            if (values != null) {
                int index = 0;
                for (List row : values) {
                    String rowStr = "";
                    for (Object item : values.get(index))
                        rowStr += item.toString()+", ";
                    results.add(rowStr);
                    index++;
                }
            }
            return results;
        }



        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            progressDialog.hide();
            if (output == null || output.size() == 0)
                showErrorDialog("There is no data entered into the Google Sheets yet. To make an estimation," +
                    " this app requires there to be some data entered.");
            else {
                output.add(0, "Data retrieved using the Google Sheets API:");
                //mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            progressDialog.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            gsheet_test.REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Toast.makeText(activity, "Request cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
