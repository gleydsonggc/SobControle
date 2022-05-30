package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.adapters.PerfilRecyclerViewAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PerfisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PerfilRecyclerViewAdapter adapter;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfis);
        setTitle("Perfis");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        recyclerView = findViewById(R.id.activity_perfis_reciclyerview);
        adapter = new PerfilRecyclerViewAdapter(getPerfis());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private List<Perfil> getPerfis() {
        return usuarioLogado.getPerfis()
                .stream()
                .sorted(Comparator.comparing(Perfil::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_perfis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_perfis_menu_item_adicionar:
                startActivityForResult(new Intent(this, PerfilCadastroActivity.class), 10);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            adapter.setPerfis(getPerfis());
            adapter.notifyDataSetChanged();
        }
    }
}