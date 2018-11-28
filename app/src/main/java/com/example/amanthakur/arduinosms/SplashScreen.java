package com.example.amanthakur.arduinosms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        final Boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);
        Thread background = new Thread(){
            public void run(){
                try{
                    sleep(5*1000);
                    if (isLoggedIn){
                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreen.this,PhoneAuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(SplashScreen.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        background.start();

    }
}