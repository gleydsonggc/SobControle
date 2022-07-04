package com.example.sobcontrole.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        setTitle("Cadastro");

        authListener = new FirebaseAuthListener(this);

        etNome = findViewById(R.id.activity_cadastro_et_nome);
        etEmail = findViewById(R.id.activity_cadastro_et_email);
        etSenha = findViewById(R.id.activity_cadastro_et_senha);
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

    public void cadastrar(View view) {
        String nome = etNome.getText().toString();
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        FirebaseUtil.cadastrarUsuario(email, senha).addOnCompleteListener(this, task -> {
            String msg = task.isSuccessful() ? "Cadastro realizado com sucesso." : "Falha ao cadastrar.";
            Toast.makeText(CadastroActivity.this, msg, Toast.LENGTH_SHORT).show();

            if (task.isSuccessful()) {
                FirebaseUtil.mudarDisplayName(nome);
                FirebaseUtil.cadastrarUsuarioRD(nome, email, senha);
            }
        });
    }
}