package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        etEmail = findViewById(R.id.activity_login_et_email);
        etSenha = findViewById(R.id.activity_login_et_senha);

        authListener = new FirebaseAuthListener(this);
    }

    public void entrar(View view) {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        FirebaseUtil.fazerLogin(email, senha).addOnCompleteListener(this, task -> {
            String msg = task.isSuccessful() ? "Login realizado com sucesso." : "Falha ao tentar o login.";
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUtil.getFbAuth().addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseUtil.getFbAuth().removeAuthStateListener(authListener);
    }
}