package com.example.sobcontrole.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Dispositivo;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerfilCadastroActivity extends AppCompatActivity {

    private EditText etNome;
    private ListView listView;
    private ArrayAdapter<Dispositivo> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cadastro);
        setTitle("Cadastrar perfil");

        etNome = findViewById(R.id.activity_perfil_cadastro_et_nome);
        listView = findViewById(R.id.activity_perfil_cadastro_lv_dispositivos);

        arrayAdapter = new ArrayAdapter<Dispositivo>(this,
                android.R.layout.simple_list_item_checked,
                FirebaseUtil.usuario.getDispositivosHabilitados()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(getItem(position).getNome());
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void cadastrarPerfil(View view) {
        String perfilNome = etNome.getText().toString();
        FirebaseUtil.usuario.adicionarPerfil(perfilNome, getIdDispositivosSelecionados());
        FirebaseUtil.salvarUsuario().addOnSuccessListener(unused -> finish());
    }

    @NonNull
    private List<String> getIdDispositivosSelecionados() {
        List<String> idDispositivosSelecionados = new ArrayList<>();
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            boolean checkboxSelecionado = sparseBooleanArray.get(i);
            if (checkboxSelecionado) {
                Dispositivo dispositivo = (Dispositivo) listView.getItemAtPosition(i);
                idDispositivosSelecionados.add(dispositivo.getId());
            }
        }

        return idDispositivosSelecionados;
    }

}