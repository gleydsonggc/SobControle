package com.example.sobcontrole.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sobcontrole.R;
import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Usuario;
import com.example.sobcontrole.repository.UsuarioRepository;
import com.example.sobcontrole.ui.activities.ControlavelEdicaoActivity;

import java.util.List;

public class ControlavelLinearRecyclerViewAdapter extends RecyclerView.Adapter<ControlavelLinearRecyclerViewAdapter.ViewHolder> {

    private List<Controlavel> controlaveis;
    Context parentContext;
    private UsuarioRepository repository;
    private Usuario usuarioLogado;

    public ControlavelLinearRecyclerViewAdapter(List<Controlavel> controlaveis) {
        this.controlaveis = controlaveis;
        this.repository = UsuarioRepository.getInstance();
        this.usuarioLogado = repository.getUsuarioLogado();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_controlavel, parent, false);
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
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvNome;
        private final ImageView ivLixeira;
        private Controlavel controlavel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.item_controlavel_tv_nome);
            ivLixeira = itemView.findViewById(R.id.item_controlavel_iv_lixeira);

            itemView.setOnClickListener(this);
            ivLixeira.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_controlavel:
                    editarControlavel(v);
                    break;
                case R.id.item_controlavel_iv_lixeira:
                    deletarControlavel();
                    break;
            }
        }

        private void deletarControlavel() {
            usuarioLogado.getControlaveis().remove(controlavel);
            setControlaveis(usuarioLogado.getControlaveis());
            notifyDataSetChanged();
        }

        private void editarControlavel(View v) {
            Intent intent = new Intent(v.getContext(), ControlavelEdicaoActivity.class);
            intent.putExtra("idControlavel", controlavel.getId());
            ((Activity) parentContext).startActivityForResult(intent, 20);
        }

        public void bind(Controlavel controlavel) {
            this.controlavel = controlavel;
            tvNome.setText(controlavel.getNome());
        }
    }
}