package com.example.sobcontrole.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitHttp {

    @GET("/dispositivo")
    Call<String> enviarComando(@Query("id") String id, @Query("cmd") String cmd);

}
