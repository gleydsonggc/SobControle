package com.example.sobcontrole.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sobcontrole.R;
import com.example.sobcontrole.util.FirebaseUtil;

public class ContaActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle("Minha Conta");

        etNome = findViewById(R.id.activity_conta_et_nome);
        etEmail = findViewById(R.id.activity_conta_et_email);

        etNome.setText(FirebaseUtil.usuario.getNome());
        etEmail.setText(FirebaseUtil.usuario.getEmail());
    }

    public void salvarConta(View view) {
        String novoNome = etNome.getText().toString();
        String novoEmail = etEmail.getText().toString();

        FirebaseUtil.usuario.setNome(novoNome);
        FirebaseUtil.usuario.setEmail(novoEmail);

        FirebaseUtil.salvarUsuario();
        finish();
    }
}