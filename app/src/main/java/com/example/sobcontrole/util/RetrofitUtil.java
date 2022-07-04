package com.example.sobcontrole.util;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtil {

    private static RetrofitHttp retrofitHttp;

    private RetrofitUtil() {}

    public static void inicializarComBaseUrl(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitHttp = retrofit.create(RetrofitHttp.class);
    }

    public static Call<String> enviarComando(String id, String cmd) {
        return retrofitHttp.enviarComando(id, cmd);
    }
}
