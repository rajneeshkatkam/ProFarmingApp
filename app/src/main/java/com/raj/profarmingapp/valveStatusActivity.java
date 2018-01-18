package com.raj.profarmingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class valveStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valve_status);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



