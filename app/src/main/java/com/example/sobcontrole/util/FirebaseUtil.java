package com.example.sobcontrole.util;

import com.example.sobcontrole.model.Usuario;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FirebaseUtil {

    public static Usuario usuario;

    private FirebaseUtil() {}

    public static void salvarUsuario() {
        Objects.requireNonNull(usuario, "Não foi possível salvar o usuario, pois ele é null.");
        FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuario.getId()).setValue(usuario);
    }

}
