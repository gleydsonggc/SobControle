package com.example.sobcontrole.util;

import com.example.sobcontrole.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FirebaseUtil {

    public static Usuario usuario;

    private FirebaseUtil() {}

    public static void salvarUsuario() {
        Objects.requireNonNull(usuario, "Não foi possível salvar o usuario, pois ele é null.");
        getUsuarioRef().setValue(usuario);
    }

    public static DatabaseReference getUsuarioRef() {
        return FirebaseDatabase.getInstance().getReference().child("usuarios").child(getCurrentUser().getUid());
    }

    public static FirebaseUser getCurrentUser() {
        return getFbAuth().getCurrentUser();
    }

    public static FirebaseAuth getFbAuth() {
        return FirebaseAuth.getInstance();
    }

}
