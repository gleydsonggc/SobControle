package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.adapters.ControlavelCardRecyclerViewAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ControlavelCardRecyclerViewAdapter adapter;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Sob Controle");

        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();
        recyclerView = findViewById(R.id.activity_principal_recyclerview);
        adapter = new ControlavelCardRecyclerViewAdapter(usuarioLogado.getControlaveis());

        int qtdColunas = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, qtdColunas));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_principal_menu_item_minhaconta:
                startActivity(new Intent(this, ContaActivity.class));
                return true;
            case R.id.activity_principal_menu_item_controlaveis:
                startActivity(new Intent(this, ControlaveisActivity.class));
                return true;
            case R.id.activity_principal_menu_item_perfis:
                startActivity(new Intent(this, PerfisActivity.class));
                return true;
            case R.id.activity_principal_menu_item_sair:
                repository.logout();
                Intent intent = new Intent(this, InicialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setControlaveis(usuarioLogado.getControlaveis());
        adapter.notifyDataSetChanged();
    }
}