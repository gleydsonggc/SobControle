package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class ControlavelCadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlavel_cadastro);
        setTitle("Cadastrar controlável");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        etNome = findViewById(R.id.activity_controlavel_cadastro_et_nome);
    }

    public void cadastrarControlavel(View view) {
        usuarioLogado.getControlaveis().add(new Controlavel(etNome.getText().toString()));
        repository.atualizar(usuarioLogado);

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}