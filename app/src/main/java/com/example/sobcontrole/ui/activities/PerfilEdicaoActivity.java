package com.example.sobcontrole.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerfilEdicaoActivity extends AppCompatActivity {

    private EditText etNome;
    private ListView listView;
    private Perfil perfil;
    private Button btAtivar;
    private ArrayAdapter<Dispositivo> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_edicao);
        setTitle("Editar perfil");

        etNome = findViewById(R.id.activity_perfil_edicao_et_nome);
        listView = findViewById(R.id.activity_perfil_edicao_lv_dispositivos);
        btAtivar = findViewById(R.id.activity_perfil_edicao_bt_ativar);

        String idPerfil = getIntent().getStringExtra("idPerfil");
        perfil = FirebaseUtil.usuario.getPerfilComId(idPerfil);
        etNome.setText(perfil.getNome());

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

        for (int position = 0; position < listView.getCount(); position++) {
            Dispositivo dispositivo = (Dispositivo) listView.getItemAtPosition(position);
            this.listView.setItemChecked(position, perfil.podeAcessarDispositivo(dispositivo.getId()));
        }
    }

    public void salvarPerfil(View view) {
        perfil.setNome(etNome.getText().toString());
        perfil.setDispositivosPermitidos(getIdDispositivosSelecionados());
        FirebaseUtil.salvarUsuario().addOnSuccessListener(unused -> finish());
    }

    public void ativarPerfil(View view) {
        if (FirebaseUtil.usuario.getPin() == null) {
            new AlertDialog.Builder(this)
                    .setTitle("PIN não configurado")
                    .setMessage("Antes de ativar um perfil, você deve configurar o PIN.")
                    .setPositiveButton("Configurar PIN", (dialog, which) -> {
                        startActivity(new Intent(this, PinActivity.class));
                    })
                    .show();
            return;
        }

        perfil.setNome(etNome.getText().toString());
        perfil.setDispositivosPermitidos(getIdDispositivosSelecionados());
        FirebaseUtil.usuario.ativarPerfil(perfil);

        FirebaseUtil.salvarUsuario().addOnSuccessListener(unused -> {
            Intent intent = new Intent(this, PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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