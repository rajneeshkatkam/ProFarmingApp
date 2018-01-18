package com.raj.profarmingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class usefulInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useful_information_activity);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
