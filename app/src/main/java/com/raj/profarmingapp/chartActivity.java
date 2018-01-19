package com.raj.profarmingapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chartActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    int nitrogen,pH,potassium,phosphorous;
    PieChart pieChart;
    float nitrogenPercent,pHPercent,potassiumPercent,phosphorousPercent;
    ArrayList<PieEntry> yvalues;
    Info soilContent;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mAuth= FirebaseAuth.getInstance();
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        uid=mAuth.getCurrentUser().getUid();
        yvalues = new ArrayList<PieEntry>();

        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                soilContent=dataSnapshot.child(uid).child("soilContents").getValue(Info.class);
                drawChart();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    public void percentage()
    {
        float total =soilContent.nitrogen+soilContent.pH+soilContent.phosphorous+soilContent.potassium;
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
        description.setText("Soil Nutrients Information");
        description.setTextSize(12f);
        pieChart.setDescription(description);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setEntryLabelColor(Color.BLACK);
        data.setValueTextSize(20f);
        pieChart.setDrawHoleEnabled(false);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
