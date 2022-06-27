package com.example.sobcontrole.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Dispositivo;
import com.example.sobcontrole.util.FirebaseUtil;
import com.example.sobcontrole.util.RetrofitUtil;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DispositivoCardRecyclerViewAdapter extends RecyclerView.Adapter<DispositivoCardRecyclerViewAdapter.ViewHolder> {

    private List<Dispositivo> dispositivos;
    private RetrofitUtil retrofitUtil;

    public DispositivoCardRecyclerViewAdapter(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
        initRetrofit();
        filtrarDispositivosDeAcordoComHabilitado();
        filtrarDispositivosDeAcordoComPerfilAtivo();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitUtil = retrofit.create(RetrofitUtil.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_dispositivo_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dispositivos.get(position));
    }

    @Override
    public int getItemCount() {
        return dispositivos.size();
    }

    public void setDispositivos(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
        filtrarDispositivosDeAcordoComHabilitado();
        filtrarDispositivosDeAcordoComPerfilAtivo();
    }

    private void filtrarDispositivosDeAcordoComPerfilAtivo() {
        if (FirebaseUtil.usuario.getPerfilAtivo() == null) {
            return;
        }
        this.dispositivos = dispositivos.stream()
                .filter(c -> FirebaseUtil.usuario.getPerfilAtivo().podeAcessarDispositivo(c.getId()))
                .collect(Collectors.toList());
    }

    private void filtrarDispositivosDeAcordoComHabilitado() {
        this.dispositivos = dispositivos.stream()
                .filter(Dispositivo::isHabilitado)
                .collect(Collectors.toList());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private Dispositivo dispositivo;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_dispositivo_card_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Call<String> call = retrofitUtil.enviarComando("1", "on");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("Retrofit", "onResponse: resposta=" + response.body());
                    Toast.makeText(v.getContext(), "Resposta: " + response.body(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Retrofit", "onFailure: falhou", t);
                }
            });
        }

        public void bind(Dispositivo dispositivo) {
            this.dispositivo = dispositivo;
//            this.itemView.setVisibility(dispositivo.isHabilitado() ? View.VISIBLE : View.GONE);
            textView.setText(dispositivo.getNome());
        }
    }

}