package com.example.looklost;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class LookLostApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase once for the whole app
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
}
