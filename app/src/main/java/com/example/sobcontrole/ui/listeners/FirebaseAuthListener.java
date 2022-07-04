package com.example.sobcontrole.ui.listeners;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.sobcontrole.ui.activities.CadastroActivity;
import com.example.sobcontrole.ui.activities.InicialActivity;
import com.example.sobcontrole.ui.activities.LoginActivity;
import com.example.sobcontrole.ui.activities.PrincipalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthListener implements FirebaseAuth.AuthStateListener {

    private final Activity activity;

    public FirebaseAuthListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        Intent intent = null;
        if ((user != null) && !(activity instanceof PrincipalActivity)) {
            intent = new Intent(activity, PrincipalActivity.class);
        }

        if ((user == null) && !(activity instanceof InicialActivity || activity instanceof CadastroActivity || activity instanceof LoginActivity)) {
            intent = new Intent(activity, InicialActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}