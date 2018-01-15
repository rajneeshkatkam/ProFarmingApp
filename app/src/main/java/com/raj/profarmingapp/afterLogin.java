package com.raj.profarmingapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    PieChart pieChart;
    int nitrogenPercent,pHPercent,potassiumPercent,phosphorousPercent;




    SharedPreferences file;  ///For storing flag value to check the external storage permissions and saving the flag value after the app is being shutdown;
    Button logout,backChartButton;
    Button displayChart;
    RelativeLayout chartRelativeLayout;
    ListView fieldInfoListView;
    LinearLayout buttonLayout;
    //int gallery_Intent=1;
    ProgressDialog progressDialog;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ArrayList<PieEntry> yvalues;




    Boolean flag;
    Info irrigation, irrigationValve,sensor,soilContent;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        chartRelativeLayout=(RelativeLayout)findViewById(R.id.chartRelativeLayout);
        buttonLayout=(LinearLayout)findViewById(R.id.buttonLayout);
        progressDialog=new ProgressDialog(this);
        fieldInfoListView=(ListView) findViewById(R.id.fieldInfoListView);
        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
        logout=(Button) findViewById(R.id.logout);
        displayChart=(Button)findViewById(R.id.displayChart);
        backChartButton=(Button)findViewById(R.id.backChartButton);
        displayChart.setEnabled(false);
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        yvalues = new ArrayList<PieEntry>();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().isHideOnContentScrollEnabled();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_reorder_white_24dp);




        ///Storage of flag value after the app is killed or shutdown
        file = getSharedPreferences("save", 0);
        flag = Boolean.valueOf(file.getString("flag", "false"));
        if (flag == false)////File permission check
            checkFilePermissions();

        SharedPreferences.Editor edit = file.edit();
        edit.putString("flag", String.valueOf(flag));
        edit.apply();



        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mStorage= FirebaseStorage.getInstance().getReference();
        mdatabaseReference= FirebaseDatabase.getInstance().getReference();



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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbarmenu, menu);
        return true;

    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        displayChart.setEnabled(true);
        drawChart();


    }


    public void logout(View v)
    {
        Log.i("Signout","Logged out");
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(),navgationBarActivity.class));
        finish();
        makeToast("Logged out");
    }

    public void percentage()
    {
        int total =soilContent.nitrogen+soilContent.pH+soilContent.phosphorous+soilContent.potassium;
        nitrogenPercent=soilContent.nitrogen*100/total;
        pHPercent=soilContent.pH*100/total;
        potassiumPercent=soilContent.potassium*100/total;
        phosphorousPercent=soilContent.phosphorous*100/total;

    }


    public void drawChart()
    {
        percentage();
        yvalues.clear();
        yvalues.add(new PieEntry(nitrogenPercent, "Nitrogen",0));
        yvalues.add(new PieEntry(pHPercent, "pH Value",1));
        yvalues.add(new PieEntry(potassiumPercent, "Potassium",2));
        yvalues.add(new PieEntry(phosphorousPercent, "Phosphorous",3));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.invalidate();
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        Description description=new Description();
        description.setText("Soil nutrients Information");
        description.setTextSize(12f);
        pieChart.setDescription(description);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelColor(Color.BLACK);
        data.setValueTextSize(20f);
        pieChart.setDrawHoleEnabled(false);





    }


    void displayChart(View v)
    {

        chartRelativeLayout.setVisibility(View.VISIBLE);
        fieldInfoListView.setVisibility(View.INVISIBLE);
        buttonLayout.setVisibility(View.INVISIBLE);

    }

    void backChart(View v)
    {
        chartRelativeLayout.setVisibility(View.INVISIBLE);
        fieldInfoListView.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);

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
