package com.example.sobcontrole.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.activities.PerfilEdicaoActivity;

import java.util.List;

public class PerfilRecyclerViewAdapter extends RecyclerView.Adapter<PerfilRecyclerViewAdapter.ViewHolder> {

    private List<Perfil> perfis;
    private Context parentContext;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    public PerfilRecyclerViewAdapter(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        repository = UsuarioRepository.getInstance();
        usuarioLogado = repository.getUsuarioLogado();

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_perfil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(perfis.get(position));
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    @Override
    public int getItemCount() {
        return perfis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final ImageView imageView;
        private Perfil perfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.item_perfil_tv_nome);
            imageView = itemView.findViewById(R.id.item_perfil_iv_lixeira);

            itemView.setOnClickListener(this);

            imageView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Lixeira", Toast.LENGTH_SHORT).show();
                deletarPerfil();
            });
        }

        private void deletarPerfil() {
            if (isPerfilAtivo(perfil)) {
                usuarioLogado.setPerfilAtivo(null);
            }
            usuarioLogado.getPerfis().remove(perfil);
            repository.atualizar(usuarioLogado);
            setPerfis(usuarioLogado.getPerfis());
            notifyDataSetChanged();
        }

        private boolean isPerfilAtivo(Perfil perfil) {
            return usuarioLogado != null
                    && usuarioLogado.getPerfilAtivo() != null
                    && usuarioLogado.getPerfilAtivo().getId().equals(perfil.getId());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), PerfilEdicaoActivity.class);
            intent.putExtra("idPerfil", perfil.getId());
            ((Activity) parentContext).startActivityForResult(intent, 200);
        }

        public void bind(Perfil perfil) {
            this.perfil = perfil;
            textView.setText(perfil.getNome() + (isPerfilAtivo(perfil) ? " (Ativo)" : ""));
        }
    }
}