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

class EstimationSheet {
    private GoogleAccountCredential googleAccountCredential;
    private ProgressDialog progressDialog;

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private int sheetId;
    private String sheetName;
    private String properSheetName;
    private Activity activity;
    private ProcessListener currentProcessListener;
    private boolean runningTask = false;
    private int variableNum;

    private static final int OK_ACTION = 0;
    private static final int RETRY_ACTION = 1;

    static final int WALL_REBUILDING_ID = 0;
    static final int CLEANING_SEALING_ID = 1;
    static final int STEP_REBUILDING_ID = 2;
    static final int JOINT_FILL_ID = 3;
    static final int INTERLOCK_RELAYING_ID = 4;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };

    /**
     *
     * @param sheetId The ID of the Google Sheet. Use the ID's in this file (<SHEET_NAME>_ID)
     * @param activity The Activity that uses the sheet. Used for dialogs etc. You can usually use 'this'
     */

    EstimationSheet(int sheetId, Activity activity){
        this.sheetId = sheetId;
        this.activity = activity;

        switch (sheetId){
            case WALL_REBUILDING_ID:
                sheetName = "wall_rebuilding";
                properSheetName = "Wall Rebuilding";
                variableNum = 11;
                break;
            case CLEANING_SEALING_ID:
                sheetName = "cleaning_sealing";
                properSheetName = "Cleaning and Sealing";
                variableNum = 7;
                break;
            case STEP_REBUILDING_ID:
                sheetName = "step_rebuilding";
                properSheetName = "Step Rebuilding";
                //TODO change this to be the actual number of variables
                variableNum = -1;
                break;
            case JOINT_FILL_ID:
                sheetName = "joint_fill";
                properSheetName = "Joint Fill";
                //TODO change this to be the actual number of variables
                variableNum = -1;
                break;
            case INTERLOCK_RELAYING_ID:
                sheetName = "interlock_relaying";
                properSheetName = "Interlock Relaying";
                //TODO change this to be the actual number of variables
                variableNum = -1;
                break;
            default:
                throw new IllegalArgumentException("'"+sheetId+"' is an invalid sheet ID. Use one of the given IDs with 'EstimationSheet.<SHEET_NAME>_ID'.");
        }

        //set up progress dialog
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Collecting data for estimation...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Initialize credentials and service object.
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                this.activity.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    /**
     *
     * @param data the data to make estimation with
     * @param estimationProcessListener ProcessListener for what to do when finished getting estimation
     */
    void startEstimation(final List<Object> data, final ProcessListener estimationProcessListener){
        if(runningTask)
            throw new IllegalStateException("Cannot be running multiple tasks at once. Please do not start this task until the previous task is done.");
        runningTask = true;
        try {
            ProcessListener getDataProcessListener = new ProcessListener() {
                @Override
                public void whenProcessFinished(boolean success, boolean a, Object output) {
                    //finished getting data from Sheets
                    runningTask = false;
                    //update progressDialog message
                    progressDialog.setMessage("Making estimation...");
                    boolean accurate;
                    double estimatedTime;
                    if(!success)
                        //abort startEstimation; an error has occurred
                        estimationProcessListener.whenProcessFinished(false, false, null);
                    else{
                        List<List<Object>> pastEstimations = (ArrayList) output;
                        List<List<Object>> dataSets = new ArrayList<>();
                        for(List<Object> estimation : pastEstimations){
                            if(!estimation.get(2).equals(""))//a time has been entered
                                dataSets.add(estimation);
                        }
                        if(dataSets.size() < variableNum){
                            //use only the aria to find estimated time
                            accurate = false;
                            double sumAlphas = 0;
                            int numAlphas = 0;
                            for(List<Object> dataSet : dataSets){
                                double actualTime = Double.parseDouble((String) dataSet.get(2));
                                double aria = Double.parseDouble((String) dataSet.get(3));
                                sumAlphas += actualTime/aria;
                                numAlphas ++;
                            }
                            double alpha = sumAlphas/numAlphas;//average of all alphas
                            estimatedTime = alpha*(double)data.get(0);
                        }else{
                            //TODO fix startEstimation() for when estimation should be accurate
                            //use all data
                            accurate = true;
                            estimatedTime = 0;
                        }
                        estimationProcessListener.whenProcessFinished(true, accurate, estimatedTime);
                        progressDialog.hide();
                    }
                }
            };
            currentProcessListener = getDataProcessListener;
            startGettingData(getDataProcessListener);
        }catch(Exception e){
            System.err.println(e.getMessage());
            runningTask = false;
        }
    }

    /**
     * Adds an estimation to the Google Sheet.
     * @param data The data which was collected for the estimation
     */
    void startAddingEstimation(List<Object> data,  final ProcessListener addingProcessListener){
        if(runningTask)
            throw new IllegalStateException("Cannot be running multiple tasks at once. Please do not start this task until the previous task is done.");
        runningTask = true;
    }

    /**
     * Removes the given estimation from the Google Sheet.
     * @param estimationId the ID of the estimation to remove
     */
    void removeEstimation(int estimationId){

    }

    /**
     * Sets the actual time taken for a job
     * @param estimationId Id of the estimation that is to have the time changed.
     * @param time How long the job took in hours.
     */
    void setEstimationTime(int estimationId, double time){

    }

    private List<List<Object>> getDataFromSheet() {
        if (! isGooglePlayServicesAvailable()){
            acquireGooglePlayServices();
            throw new IllegalStateException("Google Play Services is not available on this device.");
        }
        else if (googleAccountCredential.getSelectedAccountName() == null) {
            System.err.println("No Google account selected.");
            //return chooseAccount();
        }
        else if (! isDeviceOnline()) {
            showErrorDialog("No network connection available. Interlock App requires there to be an internet connection to get an estimation.", RETRY_ACTION);
            throw new IllegalStateException("No network connection available.");
        }
        else{
            //setup
            System.out.println("Getting data...");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.sheets.v4.Sheets service = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, googleAccountCredential)
                    .build();
            List<List<Object>> output = new ArrayList<>();
            //try to get data from sheet
            try {
                progressDialog.show();
                String spreadsheetId = "1zOQSqnzIM7Ku_JhcgMQH4Kp_sLrZRw1SDzl2nyDEosc";
                String range = sheetName+"!A2:Z";
                ValueRange response = service.spreadsheets().values()
                        .get(spreadsheetId, range)
                        .execute();
                output = response.getValues();
                progressDialog.hide();
                System.out.println("COMPLETE: the data has successfully been got");
            } catch (Exception e) {
                System.err.println("FAILED: the following error occurred: ");
                e.printStackTrace();
                progressDialog.hide();
                if (e instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) e)
                                    .getConnectionStatusCode());
                } else if (e instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) e).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog("The following error occurred while trying to get data from the "+properSheetName+" Google Sheet:\n"
                            + e.getMessage(), OK_ACTION);
                }
            }

            if (output == null || output.size() <= 10) {
                showErrorDialog("There is not enough data entered into the "+ properSheetName +" Google Sheet for an accurate estimation to be made.", OK_ACTION);
                throw new IllegalStateException("The Google Sheet '"+sheetName+"' does not have enough data on it to make an estimation.");
            }else
                return output;
        }
        return null;
    }

    private int getEstimationRowNum(int estimationId, List<List<Object>> data){
        return -1;
    }

    private boolean toBoolean(String str){
        if(str.toLowerCase().equals("true"))
            return true;
        else if (str.toLowerCase().equals("false"))
            return false;
        else
            throw new IllegalArgumentException("'"+str+"' cannot be converted to a boolean.");
    }

    private void showErrorDialog(String error, int action){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(error);
        if(action == OK_ACTION)
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        else if(action == RETRY_ACTION) {
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "RETRY",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startGettingData(currentProcessListener);
                        }
                    });
        }
        else
            throw new IllegalArgumentException("'"+action+"' is an invalid action. Please use one of the given actions which are in the format <ACTION>_ACTION.");

        alertDialog.show();
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void startGettingData(ProcessListener processListener) {
        if (! isGooglePlayServicesAvailable()){
            acquireGooglePlayServices();
            processListener.whenProcessFinished(false, false, null);
        }
        else if (googleAccountCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
        else if (! isDeviceOnline()) {
            showErrorDialog("No network connection available. Interlock App requires there to be an internet connection to get an estimation.", RETRY_ACTION);
        }
        else{

            TaskGetData taskGetData = new TaskGetData(processListener);
            taskGetData.execute();
        }
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
                startGettingData(currentProcessListener);
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
    void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {
                    showErrorDialog(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.", OK_ACTION);
                } else {
                    startGettingData(currentProcessListener);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null &&
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
                        startGettingData(currentProcessListener);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    startGettingData(currentProcessListener);
                }
                break;
        }
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
    private void showGooglePlayServicesAvailabilityErrorDialog(
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
    private class TaskGetData extends AsyncTask<Void, Void, List<List<Object>>> {

        private com.google.api.services.sheets.v4.Sheets service = null;
        private Exception lastError = null;
        private ProcessListener processListener = null;

        TaskGetData(ProcessListener processListener) {
            this.processListener = processListener;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            service = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, googleAccountCredential)
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<List<Object>> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                lastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         */
        private List<List<Object>> getDataFromApi() throws IOException {
            String spreadsheetId = "1zOQSqnzIM7Ku_JhcgMQH4Kp_sLrZRw1SDzl2nyDEosc";
            String range = sheetName+"!A2:N";
            ValueRange response = this.service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            return response.getValues();
        }



        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<List<Object>> output) {
            if (output == null || output.size() < 1) {
                showErrorDialog("There has not been any estimations made for this job yet.", OK_ACTION);
                processListener.whenProcessFinished(false, false, null);
            }else
                processListener.whenProcessFinished(true, true, output);
        }

        @Override
        protected void onCancelled() {
            if (lastError != null) {
                if (lastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) lastError)
                                    .getConnectionStatusCode());
                } else if (lastError instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) lastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog("The following error occurred while trying to get data from the "+properSheetName+" Google Sheet:\n"
                            + lastError.getMessage(), OK_ACTION);
                }
            } else {
                Toast.makeText(activity, "Request cancelled.", Toast.LENGTH_SHORT).show();
            }
            processListener.whenProcessFinished(false, false, null);
        }
    }
}

interface ProcessListener {
    /**
     * Called when the currently running process if finished.
     * @param success whether the process was successful or not.
     * @param accurate whether the output is very accurate (used for startEstimation).
     * @param output the results of the process.
     */
    void whenProcessFinished(boolean success, boolean accurate, Object output);
}