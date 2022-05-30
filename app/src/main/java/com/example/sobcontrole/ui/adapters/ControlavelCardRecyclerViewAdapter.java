package com.example.sobcontrole.ui.adapters;

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

import java.util.List;
import java.util.stream.Collectors;

public class ControlavelCardRecyclerViewAdapter extends RecyclerView.Adapter<ControlavelCardRecyclerViewAdapter.ViewHolder> {

    private List<Controlavel> controlaveis;

    public ControlavelCardRecyclerViewAdapter(List<Controlavel> controlaveis) {
        this.controlaveis = controlaveis;
        filtrarControlaveisDeAcordoComPerfilAtivo();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_controlavel_card_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Ok", Toast.LENGTH_SHORT).show();
        }

        public void bind(Controlavel controlavel) {
            textView.setText(controlavel.getNome());
        }
    }

}