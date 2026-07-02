package com.example.looklost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

                Intent intent;
                if (isLoggedIn) {
                    // If the user has already logged in, we can try to go to Home. 
                    // Usually login activity handles the redirection if user is already authenticated in Firebase.
                    intent = new Intent(splash.this, login.class);
                } else {
                    // The user has not logged in yet, so launch the MainActivity (landing page).
                    intent = new Intent(splash.this, MainActivity.class);
                }
                
                startActivity(intent);
                // Finish the splash screen Activity to remove it from the back stack.
                finish();
            }
        }, 3000);
    }
}
