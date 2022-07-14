package com.example.sobcontrole.util;

import androidx.annotation.NonNull;

import com.example.sobcontrole.model.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FirebaseUtil {

    public static Usuario usuario;

    private FirebaseUtil() {
    }

    public static Task<Void> salvarUsuario() {
        return getUsuarioRef().setValue(Objects.requireNonNull(usuario));
    }

    public static DatabaseReference getUsuarioRef() {
        return getUsuariosRef().child(getCurrentUser().getUid());
    }

    @NonNull
    public static DatabaseReference getUsuariosRef() {
        return FirebaseDatabase.getInstance().getReference().child("usuarios");
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseAuth getFbAuth() {
        return FirebaseAuth.getInstance();
    }

    public static AuthCredential getCredential(String senhaAtual) {
        return EmailAuthProvider.getCredential(FirebaseUtil.getCurrentUser().getEmail(), senhaAtual);
    }

    public static Task<Void> reautenticar(String senhaAtual) {
        return getCurrentUser().reauthenticate(getCredential(senhaAtual));
    }

    public static Task<Void> atualizarEmail(String novoEmail) {
        return getCurrentUser().updateEmail(novoEmail);
    }

    public static Task<Void> atualizarSenha(String senhaNova) {
        return getCurrentUser().updatePassword(senhaNova);
    }

    public static Task<AuthResult> cadastrarUsuario(String email, String senha) {
        return getFbAuth().createUserWithEmailAndPassword(email, senha);
    }

    public static Task<AuthResult> fazerLogin(String email, String senha) {
        return getFbAuth().signInWithEmailAndPassword(email, senha);
    }

    public static Task<Void> mudarDisplayName(String nome) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();
        return getCurrentUser().updateProfile(profileUpdates);
    }

    public static Task<Void> cadastrarUsuarioRD(String nome, String email) {
        return getUsuarioRef().setValue(new Usuario(nome, email));
    }

}
