package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerfilEdicaoActivity extends AppCompatActivity {

    private UsuarioRepository repository;
    private Usuario usuarioLogado;
    private EditText etNome;
    private ListView listView;
    private Perfil perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_edicao);
        setTitle("Editar perfil");

        etNome = findViewById(R.id.activity_perfil_edicao_et_nome);
        listView = findViewById(R.id.activity_perfil_edicao_lv_controlaveis);

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();

        String idPerfil = getIntent().getStringExtra("idPerfil");
        perfil = usuarioLogado.getPerfis()
                .stream()
                .filter(p -> p.getId().equals(idPerfil))
                .findFirst()
                .orElse(null);
        if (perfil != null) {
            etNome.setText(perfil.getNome());
        } else {
            Toast.makeText(this, "Perfil inválido", Toast.LENGTH_SHORT).show();
            finish();
        }


        ArrayAdapter<Controlavel> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, usuarioLogado.getControlaveis());

        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        for (int position = 0; position < usuarioLogado.getControlaveis().size(); position++) {
            Controlavel controlavel = (Controlavel) listView.getItemAtPosition(position);
            this.listView.setItemChecked(position, perfil.podeAcessarControlavel(controlavel.getId()));
        }
    }

    public void salvarPerfil(View view) {
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        List<Controlavel> controlaveisSelecionados = new ArrayList<>();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            boolean checkboxSelecionado = sparseBooleanArray.get(i);
            if (checkboxSelecionado) {
                Controlavel controlavel = (Controlavel) listView.getItemAtPosition(i);
                controlaveisSelecionados.add(controlavel);
            }
        }

        perfil.setNome(etNome.getText().toString());

        List<String> idControlaveisSelecionados = controlaveisSelecionados.stream()
                .map(Controlavel::getId)
                .collect(Collectors.toList());
        perfil.setControlaveisPermitidos(idControlaveisSelecionados);

        repository.atualizar(usuarioLogado);

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    public void ativarPerfil(View view) {
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        List<Controlavel> controlaveisSelecionados = new ArrayList<>();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            boolean checkboxSelecionado = sparseBooleanArray.get(i);
            if (checkboxSelecionado) {
                Controlavel controlavel = (Controlavel) listView.getItemAtPosition(i);
                controlaveisSelecionados.add(controlavel);
            }
        }

        perfil.setNome(etNome.getText().toString());

        List<String> idControlaveisSelecionados = controlaveisSelecionados.stream()
                .map(Controlavel::getId)
                .collect(Collectors.toList());
        perfil.setControlaveisPermitidos(idControlaveisSelecionados);

        usuarioLogado.setPerfilAtivo(perfil);

        repository.atualizar(usuarioLogado);

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

}