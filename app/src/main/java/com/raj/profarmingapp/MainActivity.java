package com.raj.profarmingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mAuth=FirebaseAuth.getInstance();
    }
    void loginClick(View v)
    {
        Log.i("Login","login button pressed");
    }
    void dontHaveAnAccountClick(View v)
    {
        Log.i("Register","Register button pressed");
        startActivity(new Intent(this,SignUpActivity.class));
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
