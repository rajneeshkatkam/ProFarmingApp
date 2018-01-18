package com.raj.profarmingapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class valveStatusActivity extends AppCompatActivity {

    String uid;
    DatabaseReference mdatabaseReference;
    FirebaseAuth mAuth;
    Switch fertilizerPump,nitrogenValve,phosphorousValve,potassiumValve,waterPump;
    Info irrigation, irrigationValve,sensor,soilContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valve_status);

        fertilizerPump=findViewById(R.id.fertilizerPump);
        nitrogenValve=findViewById(R.id.nitrogenValve);
        phosphorousValve=findViewById(R.id.phosphorousValve);
        potassiumValve=findViewById(R.id.potassiumValve);
        waterPump=findViewById(R.id.waterPump);

        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mdatabaseReference= FirebaseDatabase.getInstance().getReference();


        ///Values from the main activity after successful login
        fertilizerPump.setChecked(getIntent().getExtras().getBoolean("fertilizerPump"));
        nitrogenValve.setChecked(getIntent().getExtras().getBoolean("nitrogenValve"));
        phosphorousValve.setChecked(getIntent().getExtras().getBoolean("phosphorousValve"));
        potassiumValve.setChecked(getIntent().getExtras().getBoolean("potassiumValve"));
        waterPump.setChecked(getIntent().getExtras().getBoolean("waterPump"));

        fertilizerPump.setHighlightColor(Color.BLACK);


        ///Listens any changes from the app side and updates the database accordingly
        switchListener(fertilizerPump);
        switchListener(nitrogenValve);
        switchListener(phosphorousValve);
        switchListener(potassiumValve);
        switchListener(waterPump);

        ///observers any changes from the database side and if found any changes, then it updates the switches accordingly
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                irrigation =dataSnapshot.child(uid).child("irrigation").getValue(Info.class);
                irrigationValve =dataSnapshot.child(uid).child("irrigation").child("fertilizerValve").getValue(Info.class);

                fertilizerPump.setChecked(irrigation.fertilizerPump);
                nitrogenValve.setChecked(irrigationValve.nitrogenValve);
                phosphorousValve.setChecked(irrigationValve.phosphorousValve);
                potassiumValve.setChecked(irrigationValve.potassiumValve);
                waterPump.setChecked(irrigation.waterPump);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }



    ///Listener and updater to the firebase database
    public void switchListener(Switch s)
    {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton==fertilizerPump)
                {
                    mdatabaseReference.child(uid).child("irrigation").child("fertilizerPump").setValue(b);
                }
                else if(compoundButton==nitrogenValve)
                {
                    mdatabaseReference.child(uid).child("irrigation").child("fertilizerValve").child("nitrogenValve").setValue(b);
                }
                else if(compoundButton==phosphorousValve)
                {
                    mdatabaseReference.child(uid).child("irrigation").child("fertilizerValve").child("phosphorousValve").setValue(b);
                }
                else if(compoundButton==potassiumValve)
                {
                    mdatabaseReference.child(uid).child("irrigation").child("fertilizerValve").child("potassiumValve").setValue(b);
                }
                else if(compoundButton==waterPump)
                {
                    mdatabaseReference.child(uid).child("irrigation").child("waterPump").setValue(b);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



