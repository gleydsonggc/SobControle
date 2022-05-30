package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.adapters.ControlavelLinearRecyclerViewAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ControlaveisActivity extends AppCompatActivity {

    private UsuarioRepository repository;
    private Usuario usuarioLogado;
    private RecyclerView recyclerView;
    private ControlavelLinearRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlaveis);
        setTitle("Controláveis");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        recyclerView = findViewById(R.id.activity_controlaveis_recyclerview);
        adapter = new ControlavelLinearRecyclerViewAdapter(getControlaveis());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private List<Controlavel> getControlaveis() {
        return usuarioLogado.getControlaveis()
                .stream()
                .sorted(Comparator.comparing(Controlavel::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_controlaveis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_controlaveis_menu_item_adicionar:
                if (usuarioLogado.getControlaveis().size() >= 8) {
                    new AlertDialog.Builder(this)
                            .setTitle("Limite atingido")
                            .setMessage("Você já atingiu o máximo de 8 controláveis.")
                            .setPositiveButton(android.R.string.ok, (dialog, id) -> {})
                            .show();
                    return true;
                }
                startActivityForResult(new Intent(this, ControlavelCadastroActivity.class), 10);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            adapter.setControlaveis(getControlaveis());
            adapter.notifyDataSetChanged();
        }
    }

}