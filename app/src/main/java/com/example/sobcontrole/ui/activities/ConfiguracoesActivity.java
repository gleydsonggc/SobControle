package com.example.sobcontrole.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sobcontrole.R;
import com.example.sobcontrole.util.FirebaseUtil;
import com.example.sobcontrole.util.LoadingUtil;
import com.example.sobcontrole.util.RetrofitUtil;

public class ConfiguracoesActivity extends AppCompatActivity {

    private EditText etCentralUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        etCentralUrl = findViewById(R.id.activity_configuracoes_et_central_url);

        boolean usuarioTemCentralUrl = FirebaseUtil.usuario.getCentralUrl() != null && !FirebaseUtil.usuario.getCentralUrl().isEmpty();
        if (usuarioTemCentralUrl) {
            etCentralUrl.setText(FirebaseUtil.usuario.getCentralUrl());
        }
    }

    public void salvarConfiguracoes(View view) {
        String centralUrl = etCentralUrl.getText().toString().trim();

        boolean urlInformadaEInvalida = !centralUrl.isEmpty() && !centralUrl.startsWith("http://");
        if (urlInformadaEInvalida) {
            etCentralUrl.setError("A URL deve começar com http://. Exemplo: http://192.168.1.10/");
            return;
        }

        FirebaseUtil.usuario.setCentralUrl(centralUrl);
        LoadingUtil.mostrar(ConfiguracoesActivity.this);
        FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> {
            if (centralUrl.isEmpty()) {
                RetrofitUtil.resetar();
            } else {
                RetrofitUtil.inicializarComBaseUrl(centralUrl);
            }
            LoadingUtil.esconder();
            finish();
        });
    }
}