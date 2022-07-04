package com.example.sobcontrole.ui.adapters;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Dispositivo;
import com.example.sobcontrole.util.RetrofitHttp;
import com.example.sobcontrole.util.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DispositivoCardRecyclerViewAdapter extends RecyclerView.Adapter<DispositivoCardRecyclerViewAdapter.ViewHolder> {

    private List<Dispositivo> dispositivos;
    private RetrofitHttp retrofitHttp;
    private ProgressDialog progressDialog;

    public DispositivoCardRecyclerViewAdapter(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
        RetrofitUtil.inicializarComBaseUrl("http://192.168.1.11/");
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private Dispositivo dispositivo;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_dispositivo_card_tv);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            progressDialog = ProgressDialog.show(v.getContext(), null, null);
            progressDialog.setContentView(new ProgressBar(v.getContext()));
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // TODO criar activity para o usuário inserir o endereço http do ESP32
            RetrofitUtil.enviarComando("1", "on").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("Retrofit", "onResponse: resposta=" + response.body());
                    progressDialog.dismiss();
                    Toast.makeText(v.getContext(), "Resposta: " + response.body(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Retrofit", "onFailure: falhou", t);
                    progressDialog.dismiss();
                    Toast.makeText(v.getContext(), "Falha HTTP.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(Dispositivo dispositivo) {
            this.dispositivo = dispositivo;
            textView.setText(dispositivo.getNome());
        }
    }

}