package com.raj.profarmingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class afterLogin extends AppCompatActivity {
    private static final String TAG = "afterLoginActivity";
    private FirebaseAuth mAuth;
    StorageReference mStorage;
    DatabaseReference mdatabaseReference;
    String uid;



    SharedPreferences file;  ///For storing flag value to check the external storage permissions and saving the flag value after the app is being shutdown;
    Button gallery;
    ListView fieldInfoListView;
    int gallery_Intent=1;
    ProgressDialog progressDialog;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;



    Boolean flag,fertilizerPump,nitrogenValve,phosphorousValve,potassiumValve,waterPump;
    Integer humidity,moisture,temperature,nitrogen,pH,phosphorous,potassium;


    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        progressDialog=new ProgressDialog(this);
        fieldInfoListView=(ListView) findViewById(R.id.fieldInfoListView);
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
        navigationView=(NavigationView)findViewById(R.id.navigationView);


        file = getSharedPreferences("save", 0);
        flag = Boolean.valueOf(file.getString("flag", "false"));



        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mStorage= FirebaseStorage.getInstance().getReference();
        mdatabaseReference= FirebaseDatabase.getInstance().getReference();



        if (flag == false)////File permission check
            checkFilePermissions();

        SharedPreferences.Editor edit = file.edit();
        edit.putString("flag", String.valueOf(flag));
        edit.apply();


        ////Real time database update and control
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchValuesFromDatabase(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error updating the data from the Database",Toast.LENGTH_LONG).show();
            }
        });







        /// Navigation Side Bar
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   ///////////////////////




    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //Toast.makeText(getApplicationContext(),"Welcome back",Toast.LENGTH_LONG).show();
            finish();

        }
        // updateUI(currentUser);
    }

    private void checkFilePermissions() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = afterLogin.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += afterLogin.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
            flag = true;
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }




    /////Function for fetching values from the firebase database of the particular authenticated user
    public void fetchValuesFromDatabase(DataSnapshot dataSnapshot)
    {

        /// Irrigation Values
        fertilizerPump=dataSnapshot.child(uid).child("irrigation").child("fertilizerPump").getValue(Boolean.class);
        nitrogenValve=dataSnapshot.child(uid).child("irrigation").child("fertilizerValve").child("nitrogenValve").getValue(Boolean.class);
        phosphorousValve=dataSnapshot.child(uid).child("irrigation").child("fertilizerValve").child("phosphorousValve").getValue(Boolean.class);
        potassiumValve=dataSnapshot.child(uid).child("irrigation").child("fertilizerValve").child("potassiumValve").getValue(Boolean.class);
        waterPump=dataSnapshot.child(uid).child("irrigation").child("waterPump").getValue(Boolean.class);


        /// Sensor Values
        humidity=dataSnapshot.child(uid).child("sensor").child("humidity").getValue(Integer.class);
        moisture=dataSnapshot.child(uid).child("sensor").child("moisture").getValue(Integer.class);
        temperature=dataSnapshot.child(uid).child("sensor").child("temperature").getValue(Integer.class);


        /// Soil Content Values
        nitrogen=dataSnapshot.child(uid).child("soilContents").child("nitrogen").getValue(Integer.class);
        pH=dataSnapshot.child(uid).child("soilContents").child("pH").getValue(Integer.class);
        phosphorous=dataSnapshot.child(uid).child("soilContents").child("phosphorous").getValue(Integer.class);
        potassium=dataSnapshot.child(uid).child("soilContents").child("potassium").getValue(Integer.class);
        listElements();

    }


    // Toast Maker
    public void makeToast(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }


    public void listElements()
    {

        list.clear();
        list.add("                          INFORMATION");
        list.add("Irrigation Values");
        list.add("Fertilizer Pump: "+fertilizerPump.toString());
        list.add("Nitrogen Valve: "+nitrogenValve.toString());
        list.add("Phosphorous Valve: "+phosphorousValve.toString());
        list.add("Potassium Valve: "+potassiumValve.toString());
        list.add("Water Pump: "+waterPump.toString());
        list.add("");
        list.add("Sensor Values");
        list.add("Humidity: "+humidity.toString());
        list.add("Moisture: "+moisture.toString());
        list.add("Temperature: "+temperature.toString());
        list.add("");
        list.add("Soil Content Values");
        list.add("Nitrogen: "+nitrogen.toString());
        list.add("pH: "+pH.toString());
        list.add("Phosphorous: "+phosphorous.toString());
        list.add("Potassium: "+potassium.toString());
        fieldInfoListView.setAdapter(adapter);

    }






    ////For uploading image from the gallery of the phone onto the Firebase storage using button click

    /*void gallery(View v)
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image*//*");
        startActivityForResult(intent,gallery_Intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        if(requestCode==gallery_Intent && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            StorageReference filepath=mStorage.child(mAuth.getCurrentUser().getEmail().toString()).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_LONG).show();
                }
            });



        }
    }
    */














}
