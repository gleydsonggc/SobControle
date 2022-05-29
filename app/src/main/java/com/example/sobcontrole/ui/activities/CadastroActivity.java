package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private UsuarioRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setTitle("Cadastro");

        repository = UsuarioRepository.getInstance();
        etNome = findViewById(R.id.activity_cadastro_et_nome);
        etEmail = findViewById(R.id.activity_cadastro_et_email);
        etSenha = findViewById(R.id.activity_cadastro_et_senha);
    }

    public void cadastrar(View view) {
        Usuario novoUsuario = new Usuario(
                etNome.getText().toString()
                , etEmail.getText().toString()
                , etSenha.getText().toString()
        );

        repository.adicionar(novoUsuario);
        repository.logarComUsuario(novoUsuario);
    }
}