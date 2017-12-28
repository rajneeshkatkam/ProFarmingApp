package com.raj.profarmingapp;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;
    Button loginButton;
    Button dontHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton =(Button) findViewById(R.id.loginButton);
        dontHaveAnAccount =(Button) findViewById(R.id.dontHaveAnAccountButton);

        //mAuth=FirebaseAuth.getInstance();
    }
    void loginClick(View v)
    {
        Log.i("Login","login button pressed");
    }
    void dontHaveAnAccountClick(View v)
    {
        Log.i("Register","Register button pressed");
    }

    /*
@Override
public void onStart(){
super.onStart();

FirebaseUser currentUser=mAuth.getCurrentUser();
updateUI(currentUser);


}
*/


}
