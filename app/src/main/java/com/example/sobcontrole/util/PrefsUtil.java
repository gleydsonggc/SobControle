package com.example.sobcontrole.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.sobcontrole.model.Perfil;

public class PrefsUtil {

    public static void salvarIdPerfilAtivoLocalmente(Context context, String perfilId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("perfilAtivo", perfilId)
                .commit();
    }

    public static String getIdPerfilAtivoLocalmente(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("perfilAtivo", "");
    }

}
