package com.example.sobcontrole.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sobcontrole.R;
import com.example.sobcontrole.util.FirebaseUtil;
import com.example.sobcontrole.util.LoadingUtil;
import com.google.android.material.snackbar.Snackbar;

public class ContaActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenhaNova;
    private String TAG = ContaActivity.class.getCanonicalName();

    public interface SenhaCallback {
        void onCallback(String senhaAtual);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle("Minha Conta");

        etNome = findViewById(R.id.activity_conta_et_nome);
        etEmail = findViewById(R.id.activity_conta_et_email);
        etSenhaNova = findViewById(R.id.activity_conta_et_senha_nova);

        etNome.setText(FirebaseUtil.usuario.getNome());
        etEmail.setText(FirebaseUtil.usuario.getEmail());
    }

    public void salvarConta(View view) {
        String novoNome = etNome.getText().toString();
        String novoEmail = etEmail.getText().toString();
        String senhaNova = etSenhaNova.getText().toString();

        if (novoNome.isEmpty()) {
            etNome.setError("Informe o nome.");
            return;
        }

        if (novoEmail.isEmpty()) {
            etEmail.setError("Informe o e-mail.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(novoEmail).matches()) {
            etEmail.setError("E-mail inválido.");
            return;
        }

        if (!senhaNova.isEmpty() && senhaNova.length() < 6) {
            etSenhaNova.setError("A senha deve ter no mínimo 6 caracteres.");
            return;
        }

        boolean mudouEmail = !novoEmail.equals(FirebaseUtil.usuario.getEmail());
        boolean mudouSenha = (!senhaNova.isEmpty());

        if (mudouEmail && !mudouSenha) {
            solicitarSenhaAtual(senhaAtual -> mudarEmail(novoEmail, senhaAtual));
        } else if (!mudouEmail && mudouSenha) {
            solicitarSenhaAtual(senhaAtual -> mudarSenha(senhaNova, senhaAtual));
        } else if (mudouEmail && mudouSenha) {
            solicitarSenhaAtual(senhaAtual -> mudarEmailESenha(novoEmail, senhaNova, senhaAtual));
        } else {
            FirebaseUtil.usuario.setNome(novoNome);
            FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> finish());
        }
    }

    private void mudarEmail(String novoEmail, String senhaAtual) {
        LoadingUtil.mostrar(ContaActivity.this);
        FirebaseUtil.reautenticar(senhaAtual).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUtil.atualizarEmail(novoEmail).addOnCompleteListener(this, task1 -> {
                    if (task1.isSuccessful()) {
                        Log.i(TAG, "mudarEmail: e-mail alterado com sucesso.");
                        FirebaseUtil.usuario.setEmail(novoEmail);
                        FirebaseUtil.usuario.setNome(etNome.getText().toString());
                        FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> {
                            LoadingUtil.esconder();
                            finish();
                        });
                    } else {
                        LoadingUtil.esconder();
                        showMessage("Erro ao alterar o e-mail.");
                    }
                });
            } else {
                LoadingUtil.esconder();
                showMessage("Senha incorreta.");
            }
        });
    }

    private void mudarSenha(String senhaNova, String senhaAtual) {
        LoadingUtil.mostrar(ContaActivity.this);
        FirebaseUtil.reautenticar(senhaAtual).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUtil.atualizarSenha(senhaNova).addOnCompleteListener(this, task1 -> {
                            if (task1.isSuccessful()) {
                                Log.i(TAG, "mudarSenha: senha alterada com sucesso.");
                                FirebaseUtil.usuario.setNome(etNome.getText().toString());
                                FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> {
                                    LoadingUtil.esconder();
                                    finish();
                                });
                            } else {
                                LoadingUtil.esconder();
                                showMessage("Erro ao alterar a senha.");
                            }
                        });
                    } else {
                        LoadingUtil.esconder();
                        showMessage("Senha incorreta.");
                    }
                });
    }

    private void mudarEmailESenha(String novoEmail, String senhaNova, String senhaAtual) {
        LoadingUtil.mostrar(ContaActivity.this);
        FirebaseUtil.reautenticar(senhaAtual).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUtil.atualizarEmail(novoEmail).addOnCompleteListener(this, task1 -> {
                            if (task1.isSuccessful()) {
                                Log.i(TAG, "mudarEmailESenha: e-mail alterado com sucesso.");
                                FirebaseUtil.usuario.setEmail(novoEmail);
                                FirebaseUtil.usuario.setNome(etNome.getText().toString());
                                FirebaseUtil.salvarUsuario();

                                FirebaseUtil.atualizarSenha(senhaNova).addOnCompleteListener(this, task2 -> {
                                    if (task2.isSuccessful()) {
                                        Log.i(TAG, "mudarEmailESenha: senha alterada com sucesso.");
                                        FirebaseUtil.usuario.setNome(etNome.getText().toString());
                                        FirebaseUtil.salvarUsuario().addOnSuccessListener(this, unused -> {
                                            LoadingUtil.esconder();
                                            finish();
                                        });
                                    } else {
                                        LoadingUtil.esconder();
                                        showMessage("Erro ao alterar a senha.");
                                    }
                                });
                            } else {
                                LoadingUtil.esconder();
                                showMessage("Erro ao alterar o e-mail.");
                            }
                        });
                    } else {
                        LoadingUtil.esconder();
                        showMessage("Senha incorreta.");
                    }
                });
    }

    private void solicitarSenhaAtual(SenhaCallback senhaCallback) {
        EditText etSenha = new EditText(this);
        etSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(this)
                .setTitle("Senha atual")
                .setMessage("Informe a senha atual:")
                .setView(etSenha)
                .setPositiveButton("OK", (dialog, which) -> {
                    String senhaAtual = etSenha.getText().toString();
                    if (senhaAtual.length() < 6) {
                        etSenha.setError("A senha deve ter no mínimo 6 caracteres");
                        return;
                    }
                    senhaCallback.onCallback(senhaAtual);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {})
                .show();
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}