package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.sobcontrole.ui.listeners.FirebaseAuthListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private ControlavelCardRecyclerViewAdapter adapter;
//    private UsuarioRepository repository;
//    private Usuario usuarioLogado;
    private MenuItem menuMinhaConta;
    private MenuItem menuControlaveis;
    private MenuItem menuPerfis;
    private MenuItem menuSair;
    private MenuItem menuSairPerfil;
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;
    private DatabaseReference rdbUsuario;
    private FirebaseUser fbUsuario;
    private FirebaseDatabase rdb;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Sob Controle");

        fbAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuthListener(this);

//        repository = UsuarioRepository.getInstance();
//        usuarioLogado = repository.getUsuarioLogado();
//        recyclerView = findViewById(R.id.activity_principal_recyclerview);
//        adapter = new ControlavelCardRecyclerViewAdapter(usuarioLogado.getControlaveis());
//
//        int qtdColunas = 2;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, qtdColunas));
//        recyclerView.setAdapter(adapter);

        rdb = FirebaseDatabase.getInstance();
        fbUsuario = fbAuth.getCurrentUser();
        rdbUsuario = rdb.getReference("usuarios/" + fbUsuario.getUid());

        rdbUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario tempUsuario = dataSnapshot.getValue(Usuario.class);
                if (tempUsuario != null) {
                    tempUsuario.setId(dataSnapshot.getKey());
                    PrincipalActivity.this.usuario = tempUsuario;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        menuMinhaConta = menu.findItem(R.id.activity_principal_menu_item_minhaconta);
        menuControlaveis = menu.findItem(R.id.activity_principal_menu_item_controlaveis);
        menuPerfis = menu.findItem(R.id.activity_principal_menu_item_perfis);
        menuSair = menu.findItem(R.id.activity_principal_menu_item_sair);
        menuSairPerfil = menu.findItem(R.id.activity_principal_menu_item_sair_do_perfil);

//        if (usuarioLogado.getPerfilAtivo() != null) {
//            menuMinhaConta.setVisible(false);
//            menuControlaveis.setVisible(false);
//            menuPerfis.setVisible(false);
//            menuSair.setVisible(false);
//            menuSairPerfil.setVisible(true);
//        } else {
//            menuMinhaConta.setVisible(true);
//            menuControlaveis.setVisible(true);
//            menuPerfis.setVisible(true);
//            menuSair.setVisible(true);
//            menuSairPerfil.setVisible(false);
//        }
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
//                            if (!pinDigitado.equals(usuarioLogado.getPin().toString())) {
//                                Toast.makeText(this, "PIN incorreto.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            usuarioLogado.setPerfilAtivo(null);
//                            repository.atualizar(usuarioLogado);
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
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.setControlaveis(usuarioLogado.getControlaveis());
//        adapter.notifyDataSetChanged();
    }
}