package com.oep.interlock_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.oep.owenslaptop.interlock_app.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityDatabaseAccounts extends AppCompatActivity {

    static final String EMAIL_FILE_NAME = "emails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_accounts);

        final ListView accountsListView = (ListView) findViewById(R.id.accounts_list_view);

        List<String> emailAddresses = new ArrayList<>();

        try {
            FileInputStream input = openFileInput(EMAIL_FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String email =  bufferedReader.readLine();
            while(email != null){
                emailAddresses.add(email);
                email = bufferedReader.readLine();
            }
            input.close();
            inputStreamReader.close();
            bufferedReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emailAddresses);
        accountsListView.setAdapter(adapter);
    }
}
