package com.example.sobcontrole.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.ui.adapters.PerfilRecyclerViewAdapter;
import com.example.sobcontrole.util.FirebaseUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PerfisActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PerfilRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfis);
        setTitle("Perfis");

        recyclerView = findViewById(R.id.activity_perfis_reciclyerview);
        adapter = new PerfilRecyclerViewAdapter(FirebaseUtil.usuario.getPerfisOrdemAlfabetica());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
                startActivity(new Intent(this, PerfilCadastroActivity.class));
                return true;
            case R.id.activity_perfis_menu_item_configurar_pin:
                startActivity(new Intent(this, PinActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.setPerfis(FirebaseUtil.usuario.getPerfisOrdemAlfabetica());
        adapter.notifyDataSetChanged();
    }
}