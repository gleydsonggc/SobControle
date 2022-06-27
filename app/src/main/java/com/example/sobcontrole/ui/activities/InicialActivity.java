package com.example.sobcontrole.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sobcontrole.R;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.google.firebase.auth.FirebaseAuth;

public class InicialActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getSupportActionBar().hide();

        fbAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuthListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }

    public void entrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }
}