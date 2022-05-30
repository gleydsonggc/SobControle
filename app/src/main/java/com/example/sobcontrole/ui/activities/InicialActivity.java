package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class InicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        getSupportActionBar().hide();

        Usuario usuarioLogado = UsuarioRepository.getInstance().getUsuarioLogado();
        if (usuarioLogado != null) {
            Intent intent = new Intent(this, PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void entrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }
}