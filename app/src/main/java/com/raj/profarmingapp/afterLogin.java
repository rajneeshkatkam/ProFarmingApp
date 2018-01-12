package com.raj.profarmingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class afterLogin extends AppCompatActivity {
    private static final String TAG = "afterLoginActivity";
    private FirebaseAuth mAuth;
    StorageReference mStorage;
    DatabaseReference mdatabaseReference;
    String uid;



    SharedPreferences file;  ///For storing flag value to check the external storage permissions and saving the flag value after the app is being shutdown;
    Button logout;
    Button displayChart;
    ListView fieldInfoListView;
    //int gallery_Intent=1;
    ProgressDialog progressDialog;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;




    Boolean flag;
    Info irrigation, irrigationValve,sensor,soilContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);


        progressDialog=new ProgressDialog(this);
        fieldInfoListView=(ListView) findViewById(R.id.fieldInfoListView);
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
        logout=(Button) findViewById(R.id.logout);
        displayChart=(Button)findViewById(R.id.displayChart);


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
            }
        });

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
        irrigation =dataSnapshot.child(uid).child("irrigation").getValue(Info.class);
        irrigationValve =dataSnapshot.child(uid).child("irrigation").child("fertilizerValve").getValue(Info.class);

        /// Sensor Values
        sensor=dataSnapshot.child(uid).child("sensor").getValue(Info.class);

        /// Soil Content Values
        soilContent=dataSnapshot.child(uid).child("soilContents").getValue(Info.class);
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
        list.add("Fertilizer Pump: "+ irrigation.fertilizerPump.toString());
        list.add("Nitrogen Valve: "+ irrigationValve.nitrogenValve.toString());
        list.add("Phosphorous Valve: "+ irrigationValve.phosphorousValve.toString());
        list.add("Potassium Valve: "+ irrigationValve.potassiumValve.toString());
        list.add("Water Pump: "+ irrigation.waterPump.toString());
        list.add("");
        list.add("Sensor Values");
        list.add("Humidity: "+sensor.humidity.toString());
        list.add("Moisture: "+sensor.moisture.toString());
        list.add("Temperature: "+sensor.temperature.toString());
        list.add("");
        list.add("Soil Content Values");
        list.add("Nitrogen: "+soilContent.nitrogen.toString());
        list.add("pH: "+soilContent.pH.toString());
        list.add("Phosphorous: "+soilContent.phosphorous.toString());
        list.add("Potassium: "+soilContent.potassium.toString());
        fieldInfoListView.setAdapter(adapter);

    }


    public void logout(View v)
    {
        Log.i("Signout","Logged out");
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        makeToast("Logged out");
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
