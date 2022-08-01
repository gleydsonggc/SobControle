package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;
import com.example.sobcontrole.util.LoadingUtil;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

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
        boolean validacaoFalhou = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("E-mail inválido.");
            validacaoFalhou = true;
        }

        if (email.isEmpty()) {
            etEmail.setError("Informe o e-mail.");
            validacaoFalhou = true;
        }

        if (senha.isEmpty()) {
            etSenha.setError("Informe a senha.");
            validacaoFalhou = true;
        }

        if (validacaoFalhou) return;

        LoadingUtil.mostrar(LoginActivity.this);
        FirebaseUtil.fazerLogin(email, senha).addOnCompleteListener(this, task -> {
            LoadingUtil.esconder();
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login realizado com sucesso.", Toast.LENGTH_SHORT).show();
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    etEmail.setError("E-mail não cadastrado.");
                    etEmail.requestFocus();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    etSenha.setError("Senha incorreta.");
                    etSenha.requestFocus();
                } else if (task.getException() instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(LoginActivity.this, "Erro: excesso de tentativas, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Falha ao tentar o login.", Toast.LENGTH_SHORT).show();
                }
            }
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