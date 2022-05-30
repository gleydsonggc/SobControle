package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class ControlavelEdicaoActivity extends AppCompatActivity {

    private EditText etNome;
    private Controlavel controlavel;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlavel_edicao);
        setTitle("Editar controlável");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        etNome = findViewById(R.id.activity_controlavel_edicao_et_nome);

        String idControlavel = getIntent().getStringExtra("idControlavel");
        controlavel = getControlavel(idControlavel);
        if (controlavel != null) {
            etNome.setText(controlavel.getNome());
        } else {
            Toast.makeText(this, "Controlável inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @NonNull
    private Controlavel getControlavel(String idControlavel) {
        return usuarioLogado.getControlaveis()
                .stream()
                .filter(c -> c.getId().equals(idControlavel))
                .findFirst()
                .orElse(null);
    }

    public void editarControlavel(View view) {
        String novoNome = etNome.getText().toString();
        if (novoNome.trim().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Nome vazio")
                    .setMessage("O nome não pode ser vazio.")
                    .setPositiveButton(android.R.string.ok, (dialog, id) -> {})
                    .show();
            return;
        }

        controlavel.setNome(novoNome);
        repository.atualizar(usuarioLogado);

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}