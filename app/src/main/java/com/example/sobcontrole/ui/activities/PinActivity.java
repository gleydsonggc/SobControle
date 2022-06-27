package com.example.sobcontrole.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.util.FirebaseUtil;

public class PinActivity extends AppCompatActivity {

    private EditText etPin;
    private MenuItem menuItemSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        etPin = findViewById(R.id.activity_pin_pin);

        if (FirebaseUtil.usuario.getPin() != null) {
            etPin.setText(FirebaseUtil.usuario.getPin().toString());
        }

        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                menuItemSalvar.setVisible(Usuario.validaPin(etPin.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pin, menu);
        menuItemSalvar = menu.findItem(R.id.activity_pin_menu_salvar);

        menuItemSalvar.setVisible(Usuario.validaPin(etPin.getText().toString()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_pin_menu_salvar:
                String pin = etPin.getText().toString();
                FirebaseUtil.usuario.setPin(Integer.valueOf(pin));
                FirebaseUtil.salvarUsuario();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}