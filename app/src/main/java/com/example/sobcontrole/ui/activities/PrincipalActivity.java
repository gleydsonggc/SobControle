package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.adapters.ControlavelCardRecyclerViewAdapter;
import com.example.sobcontrole.util.RetrofitUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ControlavelCardRecyclerViewAdapter adapter;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;
    private MenuItem menuMinhaConta;
    private MenuItem menuControlaveis;
    private MenuItem menuPerfis;
    private MenuItem menuSair;
    private MenuItem menuSairPerfil;

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
        menuMinhaConta = menu.findItem(R.id.activity_principal_menu_item_minhaconta);
        menuControlaveis = menu.findItem(R.id.activity_principal_menu_item_controlaveis);
        menuPerfis = menu.findItem(R.id.activity_principal_menu_item_perfis);
        menuSair = menu.findItem(R.id.activity_principal_menu_item_sair);
        menuSairPerfil = menu.findItem(R.id.activity_principal_menu_item_sair_do_perfil);

        if (usuarioLogado.getPerfilAtivo() != null) {
            menuMinhaConta.setVisible(false);
            menuControlaveis.setVisible(false);
            menuPerfis.setVisible(false);
            menuSair.setVisible(false);
            menuSairPerfil.setVisible(true);
        } else {
            menuMinhaConta.setVisible(true);
            menuControlaveis.setVisible(true);
            menuPerfis.setVisible(true);
            menuSair.setVisible(true);
            menuSairPerfil.setVisible(false);
        }
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
            case R.id.activity_principal_menu_item_sair_do_perfil:
                EditText etPin = new EditText(this);
                etPin.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
                etPin.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
                new AlertDialog.Builder(this)
                        .setTitle("PIN")
                        .setMessage("Informe o PIN:")
                        .setView(etPin)
                        .setPositiveButton("OK", (dialog, which) -> {
                            String pinDigitado = etPin.getText().toString();
                            if (!pinDigitado.equals(usuarioLogado.getPin().toString())) {
                                Toast.makeText(this, "PIN incorreto.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            usuarioLogado.setPerfilAtivo(null);
                            repository.atualizar(usuarioLogado);
                            recreate();
                        })
                        .show();
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