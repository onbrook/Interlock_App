package com.example.owenslaptop.interlock_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Wall_Rebuilding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall__rebuilding);
    }

    public void homeButtonClicked(View view){
        startActivity(new Intent(this, HomeScreen.class));
    }
    public static void create(){
        new Wall_Rebuilding().h();
    }
    private void h(){

    }
}
