package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.repository.UsuarioRepository;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        etEmail = findViewById(R.id.activity_login_et_email);
        etSenha = findViewById(R.id.activity_login_et_senha);
    }

    public void entrar(View view) {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        boolean logado = UsuarioRepository.getInstance().logarComEmailESenha(email, senha);
        if (!logado) {
            Toast.makeText(this, "Não há usuário cadastrado com esses dados.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}