package com.raj.profarmingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText emailId;
    EditText password;
    Pattern p;
    Matcher m;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailId=(EditText) findViewById(R.id.emailId);
        password=(EditText) findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("AlreadyLogin","You are already logged in");
       // updateUI(currentUser);
    }


    void signupClick(View v)
    {


        String emailId1=emailId.getText().toString();
        String password1=password.getText().toString();
        p=Pattern.compile("@(.*?).com");
        m=p.matcher(emailId1);

        ///Email ID
        if(Objects.equals(emailId1, ""))
        {
            Toast.makeText(this,"Please enter your email id",Toast.LENGTH_SHORT).show();
        }
        else if(!m.find())
        {
            Toast.makeText(this,"Enter a valid Email Address",Toast.LENGTH_SHORT).show();
        }


        ///Password
        else if(Objects.equals(password1, ""))
        {
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }
        else if(password1.length()<6)
        {
            Toast.makeText(this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
        }

        ////New User SignUp
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailId1, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Registeration Successfull", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registeration Unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    void alreadyHaveAnAccountClick(View v)
    {
        startActivity(new Intent(this,MainActivity.class));
    }
}

