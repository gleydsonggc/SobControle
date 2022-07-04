package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sobcontrole.R;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        authListener = new FirebaseAuthListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            FirebaseUtil.getFbAuth().addAuthStateListener(authListener);
        }, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseUtil.getFbAuth().removeAuthStateListener(authListener);
    }
}