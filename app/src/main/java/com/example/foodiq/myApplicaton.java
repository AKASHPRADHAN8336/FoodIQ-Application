package com.example.foodiq;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class myApplicaton extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
