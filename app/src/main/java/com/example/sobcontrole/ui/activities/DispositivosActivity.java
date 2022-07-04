package com.example.sobcontrole.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class DispositivosActivity extends AppCompatActivity {

    private EditText[] etDispositivo;
    private CheckBox[] cbDispositivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos);
        setTitle("Dispositivos");

        etDispositivo = new EditText[]{
                findViewById(R.id.etDispositivo1),
                findViewById(R.id.etDispositivo2),
                findViewById(R.id.etDispositivo3),
                findViewById(R.id.etDispositivo4),
                findViewById(R.id.etDispositivo5),
                findViewById(R.id.etDispositivo6),
                findViewById(R.id.etDispositivo7),
                findViewById(R.id.etDispositivo8)
        };

        cbDispositivo = new CheckBox[]{
                findViewById(R.id.cbDispositivo1),
                findViewById(R.id.cbDispositivo2),
                findViewById(R.id.cbDispositivo3),
                findViewById(R.id.cbDispositivo4),
                findViewById(R.id.cbDispositivo5),
                findViewById(R.id.cbDispositivo6),
                findViewById(R.id.cbDispositivo7),
                findViewById(R.id.cbDispositivo8)
        };

        for (int i = 0; i < etDispositivo.length; i++) {
            cbDispositivo[i].setChecked(FirebaseUtil.usuario.getDispositivos().get(i).isHabilitado());
            etDispositivo[i].setText(FirebaseUtil.usuario.getDispositivos().get(i).getNome());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dispositivos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_dispositivos_menu_salvar:
                salvarDispositivos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void salvarDispositivos() {
        for (int i = 0; i < etDispositivo.length; i++) {
            String nome = etDispositivo[i].getText().toString().trim();

            cbDispositivo[i].setChecked(cbDispositivo[i].isChecked() && !nome.isEmpty());
            boolean checkBoxHabilitado = cbDispositivo[i].isChecked();

            Dispositivo dispositivo = FirebaseUtil.usuario.getDispositivos().get(i);
            dispositivo.setNome(nome);
            dispositivo.setHabilitado(checkBoxHabilitado);

            if (!checkBoxHabilitado) {
                FirebaseUtil.usuario.getPerfis()
                        .forEach(p -> p.getDispositivosPermitidos().remove(dispositivo.getId()));
            }
        }

        FirebaseUtil.salvarUsuario().addOnSuccessListener(unused -> finish());
    }

}