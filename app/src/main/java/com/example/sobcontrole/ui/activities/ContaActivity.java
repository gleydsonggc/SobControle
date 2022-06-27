package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.util.FirebaseUtil;
import com.google.firebase.database.FirebaseDatabase;

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

//        repository = UsuarioRepository.getInstance();
//        usuarioLogado = repository.getUsuarioLogado();
        etNome = findViewById(R.id.activity_conta_et_nome);
        etEmail = findViewById(R.id.activity_conta_et_email);
//        etSenha = findViewById(R.id.activity_conta_et_senha);

        etNome.setText(FirebaseUtil.usuario.getNome());
        etEmail.setText(FirebaseUtil.usuario.getEmail());
//        etSenha.setText(usuarioLogado.getSenha());
    }

    public void salvarConta(View view) {
        String novoNome = etNome.getText().toString();
        String novoEmail = etEmail.getText().toString();
//        String novaSenha = etSenha.getText().toString();

        FirebaseUtil.usuario.setNome(novoNome);
        FirebaseUtil.usuario.setEmail(novoEmail);
//        usuarioLogado.setSenha(novaSenha);

//        repository.atualizar(usuarioLogado);
        FirebaseUtil.salvarUsuario();
        finish();
    }
}