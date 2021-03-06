package com.example.sobcontrole.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Dispositivo;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.ui.adapters.DispositivoCardRecyclerViewAdapter;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;
import com.example.sobcontrole.util.LoadingUtil;
import com.example.sobcontrole.util.PrefsUtil;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DispositivoCardRecyclerViewAdapter adapter;
    private MenuItem menuConfiguracoes;
    private MenuItem menuMinhaConta;
    private MenuItem menuDispositivos;
    private MenuItem menuPerfis;
    private MenuItem menuSair;
    private MenuItem menuSairPerfil;
    private FirebaseAuthListener authListener;
    private String TAG = PrincipalActivity.class.getCanonicalName();
    private boolean menuDeveSerConfigurado;
    private Button btConfigurarDispositivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: start");
        setContentView(R.layout.activity_principal);
        setTitle("Sob Controle");

        authListener = new FirebaseAuthListener(this);

        btConfigurarDispositivos = findViewById(R.id.activity_principal_bt_configurar_dispositivos);
        btConfigurarDispositivos.setOnClickListener(v -> startActivity(new Intent(this, DispositivosActivity.class)));

        recyclerView = findViewById(R.id.activity_principal_recyclerview);
        recyclerView.setLayoutManager(new FlexboxLayoutManager(this));

        LoadingUtil.mostrar(PrincipalActivity.this);
        FirebaseUtil.getUsuarioRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario tempUsuario = dataSnapshot.getValue(Usuario.class);
                if (tempUsuario != null) {
                    tempUsuario.setId(dataSnapshot.getKey());
                    FirebaseUtil.usuario = tempUsuario;

                    String idPerfilAtivoLocalmente = PrefsUtil.getIdPerfilAtivoLocalmente(PrincipalActivity.this);
                    List<Dispositivo> dispositivosPodemSerExibidos = FirebaseUtil.usuario.getDispositivosPodemSerExibidos(idPerfilAtivoLocalmente);
                    btConfigurarDispositivos.setVisibility(dispositivosPodemSerExibidos.isEmpty() && idPerfilAtivoLocalmente.isEmpty() ? View.VISIBLE : View.GONE);
                    if (recyclerView.getAdapter() == null) {
                        adapter = new DispositivoCardRecyclerViewAdapter(dispositivosPodemSerExibidos);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setDispositivos(dispositivosPodemSerExibidos);
                        adapter.notifyDataSetChanged();
                    }

                    menuDeveSerConfigurado = true;
                    LoadingUtil.esconder();
                    Log.d(TAG, "onDataChange: onCreate");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
        Log.d(TAG, "onCreate: finish");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        menuConfiguracoes = menu.findItem(R.id.activity_principal_menu_item_configuracoes);
        menuMinhaConta = menu.findItem(R.id.activity_principal_menu_item_minhaconta);
        menuDispositivos = menu.findItem(R.id.activity_principal_menu_item_dispositivos);
        menuPerfis = menu.findItem(R.id.activity_principal_menu_item_perfis);
        menuSair = menu.findItem(R.id.activity_principal_menu_item_sair);
        menuSairPerfil = menu.findItem(R.id.activity_principal_menu_item_sair_do_perfil);

        if (menuDeveSerConfigurado) {
            configurarMenu();
            menuDeveSerConfigurado = false;
        }

        Log.d(TAG, "onCreateOptionsMenu: finish");
        return true;
    }

    private void configurarMenu() {
        if (!PrefsUtil.getIdPerfilAtivoLocalmente(this.getApplicationContext()).isEmpty()) {
            menuConfiguracoes.setVisible(false);
            menuMinhaConta.setVisible(false);
            menuDispositivos.setVisible(false);
            menuPerfis.setVisible(false);
            menuSair.setVisible(false);
            menuSairPerfil.setVisible(true);
        } else {
            menuConfiguracoes.setVisible(true);
            menuMinhaConta.setVisible(true);
            menuDispositivos.setVisible(true);
            menuPerfis.setVisible(true);
            menuSair.setVisible(true);
            menuSairPerfil.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_principal_menu_item_configuracoes:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
                return true;
            case R.id.activity_principal_menu_item_minhaconta:
                startActivity(new Intent(this, ContaActivity.class));
                return true;
            case R.id.activity_principal_menu_item_dispositivos:
                startActivity(new Intent(this, DispositivosActivity.class));
                return true;
            case R.id.activity_principal_menu_item_perfis:
                startActivity(new Intent(this, PerfisActivity.class));
                return true;
            case R.id.activity_principal_menu_item_sair:
                if (FirebaseUtil.getCurrentUser() != null) {
                    FirebaseUtil.getFbAuth().signOut();
                } else {
                    Toast.makeText(PrincipalActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
                }
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
                            if (!pinDigitado.equals(FirebaseUtil.usuario.getPin().toString())) {
                                Toast.makeText(this, "PIN incorreto.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            PrefsUtil.salvarIdPerfilAtivoLocalmente(this.getApplicationContext(), "");
                            FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> recreate());
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: start");
        FirebaseUtil.getFbAuth().addAuthStateListener(authListener);
        Log.d(TAG, "onStart: finish");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: start");
        FirebaseUtil.getFbAuth().removeAuthStateListener(authListener);
        Log.d(TAG, "onStop: finish");
    }

}