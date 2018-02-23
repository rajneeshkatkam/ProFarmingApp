package com.raj.profarmingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class weatherStatusActivity extends AppCompatActivity {

    DatabaseReference mdatabaseReference;
    FirebaseAuth mAuth;
    String uid;
    Info sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_status);

        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mdatabaseReference=FirebaseDatabase.getInstance().getReference();

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sensor=dataSnapshot.child(uid).child("sensor").getValue(Info.class);
                weatherDisplay();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void weatherDisplay() {

        Toast.makeText(this, sensor.humidity.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, sensor.temperature.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, sensor.moisture.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

