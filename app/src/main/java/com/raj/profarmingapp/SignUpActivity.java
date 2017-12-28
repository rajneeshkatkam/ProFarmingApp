package com.raj.profarmingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText emailId;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailId=(EditText) findViewById(R.id.emailId);
        password=(EditText) findViewById(R.id.password);
    }

    void signupClick(View v)
    {
        String emailId1=emailId.getText().toString();
        String password1=password.getText().toString();

        if(Objects.equals(emailId1, ""))
        {
            Toast.makeText(this,"Please enter your email id",Toast.LENGTH_SHORT).show();
        }
        if(Objects.equals(password1, ""))
        {
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }

        if(password1.length()<6)
        {
            Toast.makeText(this,"Minimum Password Length is 6",Toast.LENGTH_SHORT).show();
        }
    }

    void alreadyHaveAnAccountClick(View v)
    {
        startActivity(new Intent(this,MainActivity.class));
    }
}

