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
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.util.RetrofitUtil;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ControlavelCardRecyclerViewAdapter extends RecyclerView.Adapter<ControlavelCardRecyclerViewAdapter.ViewHolder> {

    private List<Controlavel> controlaveis;
    private RetrofitUtil service;

    public ControlavelCardRecyclerViewAdapter(List<Controlavel> controlaveis) {
        this.controlaveis = controlaveis;
        initRetrofit();
        filtrarControlaveisDeAcordoComHabilitado();
        filtrarControlaveisDeAcordoComPerfilAtivo();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.11/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitUtil.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_controlavel_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(controlaveis.get(position));
    }

    @Override
    public int getItemCount() {
        return controlaveis.size();
    }

    public void setControlaveis(List<Controlavel> controlaveis) {
        this.controlaveis = controlaveis;
        filtrarControlaveisDeAcordoComHabilitado();
        filtrarControlaveisDeAcordoComPerfilAtivo();
    }

    private void filtrarControlaveisDeAcordoComPerfilAtivo() {
        Usuario usuarioLogado = UsuarioRepository.getInstance().getUsuarioLogado();
        if (usuarioLogado.getPerfilAtivo() == null) {
            return;
        }
        this.controlaveis = controlaveis.stream()
                .filter(c -> usuarioLogado.getPerfilAtivo().podeAcessarControlavel(c.getId()))
                .collect(Collectors.toList());
    }

    private void filtrarControlaveisDeAcordoComHabilitado() {
        Usuario usuarioLogado = UsuarioRepository.getInstance().getUsuarioLogado();
        this.controlaveis = controlaveis.stream()
                .filter(Controlavel::isHabilitado)
                .collect(Collectors.toList());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private Controlavel controlavel;
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_controlavel_card_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Call<String> call = service.enviarComando("1", "on");
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

        public void bind(Controlavel controlavel) {
            this.controlavel = controlavel;
//            this.itemView.setVisibility(controlavel.isHabilitado() ? View.VISIBLE : View.GONE);
            textView.setText(controlavel.getNome());
        }
    }

}