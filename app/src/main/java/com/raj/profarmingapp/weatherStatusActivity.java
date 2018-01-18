package com.raj.profarmingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class weatherStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_status);
        Toast.makeText(this,getIntent().getExtras().get("humidity").toString(),Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

