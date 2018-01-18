package com.raj.profarmingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class weatherStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_status);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
