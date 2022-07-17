package com.example.sobcontrole.util;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

    private static RetrofitHttp retrofitHttp;

    private RetrofitUtil() {}

    public static class RetrofitNaoConfiguradoException extends Exception {
        public RetrofitNaoConfiguradoException() {
            super("O Retrofit precisa estar configurado para se comunicar com os dispositivos.");
        }
    }

    public static void inicializarComBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            retrofitHttp = null;
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitHttp = retrofit.create(RetrofitHttp.class);
    }

    public static Call<String> enviarComando(String id, String cmd) throws RetrofitNaoConfiguradoException {
        if (retrofitHttp == null
                && FirebaseUtil.usuario.getCentralUrl() != null
                && !FirebaseUtil.usuario.getCentralUrl().isEmpty()) {
            inicializarComBaseUrl(FirebaseUtil.usuario.getCentralUrl());
        }
        if (retrofitHttp == null) throw new RetrofitNaoConfiguradoException();
        return retrofitHttp.enviarComando(id, cmd);
    }

}
