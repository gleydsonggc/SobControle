package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class ContaActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle("Minha Conta");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        etNome = findViewById(R.id.activity_conta_et_nome);
        etEmail = findViewById(R.id.activity_conta_et_email);
        etSenha = findViewById(R.id.activity_conta_et_senha);

        etNome.setText(usuarioLogado.getNome());
        etEmail.setText(usuarioLogado.getEmail());
//        etSenha.setText(usuarioLogado.getSenha());
    }

    public void salvarConta(View view) {
        String novoNome = etNome.getText().toString();
        String novoEmail = etEmail.getText().toString();
        String novaSenha = etSenha.getText().toString();

        usuarioLogado.setNome(novoNome);
        usuarioLogado.setEmail(novoEmail);
//        usuarioLogado.setSenha(novaSenha);

        repository.atualizar(usuarioLogado);
        finish();
    }
}