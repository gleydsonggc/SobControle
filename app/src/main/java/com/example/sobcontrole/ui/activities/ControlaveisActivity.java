package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;

public class ControlaveisActivity extends AppCompatActivity {

    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    private EditText[] etControlavel;
    private CheckBox[] cbControlavel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlaveis);
        setTitle("Controláveis");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();

        etControlavel = new EditText[]{
                findViewById(R.id.etControlavel1),
                findViewById(R.id.etControlavel2),
                findViewById(R.id.etControlavel3),
                findViewById(R.id.etControlavel4),
                findViewById(R.id.etControlavel5),
                findViewById(R.id.etControlavel6),
                findViewById(R.id.etControlavel7),
                findViewById(R.id.etControlavel8)
        };

        cbControlavel = new CheckBox[]{
                findViewById(R.id.cbControlavel1),
                findViewById(R.id.cbControlavel2),
                findViewById(R.id.cbControlavel3),
                findViewById(R.id.cbControlavel4),
                findViewById(R.id.cbControlavel5),
                findViewById(R.id.cbControlavel6),
                findViewById(R.id.cbControlavel7),
                findViewById(R.id.cbControlavel8)
        };

        for (int i = 0; i < etControlavel.length; i++) {
            cbControlavel[i].setChecked(usuarioLogado.getControlaveis().get(i).isHabilitado());
            etControlavel[i].setText(usuarioLogado.getControlaveis().get(i).getNome());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_controlaveis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_controlaveis_menu_salvar:
                salvarControlaveis();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void salvarControlaveis() {
        for (int i = 0; i < etControlavel.length; i++) {
            String nome = etControlavel[i].getText().toString().trim();
            cbControlavel[i].setChecked(cbControlavel[i].isChecked() && !nome.isEmpty());
            boolean checkBoxHabilitado = cbControlavel[i].isChecked();
            Controlavel controlavel = usuarioLogado.getControlaveis().get(i);
            controlavel.setNome(nome);
            controlavel.setHabilitado(checkBoxHabilitado);
            if (!checkBoxHabilitado) {
                usuarioLogado.getPerfis().forEach(p -> p.getControlaveisPermitidos().remove(controlavel.getId()));
            }
        }
        repository.atualizar(usuarioLogado);
        finish();
    }

}