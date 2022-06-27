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
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.ui.adapters.DispositivoCardRecyclerViewAdapter;
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.example.sobcontrole.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DispositivoCardRecyclerViewAdapter adapter;
    private MenuItem menuMinhaConta;
    private MenuItem menuDispositivos;
    private MenuItem menuPerfis;
    private MenuItem menuSair;
    private MenuItem menuSairPerfil;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Sob Controle");

        authListener = new FirebaseAuthListener(this);

        recyclerView = findViewById(R.id.activity_principal_recyclerview);
        int qtdColunas = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, qtdColunas));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        menuMinhaConta = menu.findItem(R.id.activity_principal_menu_item_minhaconta);
        menuDispositivos = menu.findItem(R.id.activity_principal_menu_item_dispositivos);
        menuPerfis = menu.findItem(R.id.activity_principal_menu_item_perfis);
        menuSair = menu.findItem(R.id.activity_principal_menu_item_sair);
        menuSairPerfil = menu.findItem(R.id.activity_principal_menu_item_sair_do_perfil);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FirebaseUtil.getUsuarioRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario tempUsuario = dataSnapshot.getValue(Usuario.class);
                if (tempUsuario != null) {
                    tempUsuario.setId(dataSnapshot.getKey());
                    FirebaseUtil.usuario = tempUsuario;
                    adapter = new DispositivoCardRecyclerViewAdapter(FirebaseUtil.usuario.getDispositivos());
                    recyclerView.setAdapter(adapter);
                    configurarMenu();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void configurarMenu() {
        if (FirebaseUtil.usuario.getPerfilAtivo() != null) {
            menuMinhaConta.setVisible(false);
            menuDispositivos.setVisible(false);
            menuPerfis.setVisible(false);
            menuSair.setVisible(false);
            menuSairPerfil.setVisible(true);
        } else {
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
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
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
                            FirebaseUtil.usuario.setPerfilAtivo(null);
                            FirebaseUtil.salvarUsuario();
                            recreate();
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
        FirebaseUtil.getFbAuth().addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseUtil.getFbAuth().removeAuthStateListener(authListener);
    }

}