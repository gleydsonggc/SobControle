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

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerfilCadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private ListView listView;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;
    private ArrayAdapter<Controlavel> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cadastro);
        setTitle("Cadastrar perfil");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        etNome = findViewById(R.id.activity_perfil_cadastro_et_nome);
        listView = findViewById(R.id.activity_perfil_cadastro_lv_controlaveis);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, usuarioLogado.getControlaveis().stream().filter(Controlavel::isHabilitado).collect(Collectors.toList()));

        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    public void cadastrarPerfil(View view) {
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        List<Controlavel> controlaveisSelecionados = new ArrayList<>();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            boolean checkboxSelecionado = sparseBooleanArray.get(i);
            if (checkboxSelecionado) {
                Controlavel controlavel = (Controlavel) listView.getItemAtPosition(i);
                controlaveisSelecionados.add(controlavel);
            }
        }

        Perfil novoPerfil = new Perfil(etNome.getText().toString());
        controlaveisSelecionados.forEach(c -> novoPerfil.getControlaveisPermitidos().add(c.getId()));

        usuarioLogado.getPerfis().add(novoPerfil);

        repository.atualizar(usuarioLogado);

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

}