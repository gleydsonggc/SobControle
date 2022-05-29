package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sobcontrole.R;

public class InicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getSupportActionBar().hide();
    }

    public void entrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }
}