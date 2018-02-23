package com.raj.profarmingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class plantDisease extends AppCompatActivity {

    int gallery_Intent=1;
    ProgressDialog progressDialog;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    String uid;
    TextView plantDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_disease);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mStorage= FirebaseStorage.getInstance().getReference();
        plantDisease=findViewById(R.id.plantDisease);



    }

    public void plantDisease(View v)
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,gallery_Intent);
    }

    int objectScan=1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.setMessage("Uploading..");
        mDatabaseReference.child(uid).child("plantDisease").child("count").setValue(objectScan);
        progressDialog.show();
        if(requestCode==gallery_Intent && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            StorageReference filepath=mStorage.child(String.valueOf(objectScan));
            Log.i("Uri","Done");
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_LONG).show();
                    mDatabaseReference.child(uid).child("plantDisease").child("emailId").setValue(mAuth.getCurrentUser().getEmail());
                    objectScan++;
                }
            });

        }


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s=dataSnapshot.child(uid).child("plantDisease").child("diseaseDetected").getValue(String.class);
                /*for(int i=1;i<=total;i++)
                {
                    s=s+"Object "+String.valueOf(i)+": "+dataSnapshot.child(uid).child("Object").child(String.valueOf(i)).getValue(String.class)+"\n";
                }*/
                plantDisease.setText(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
