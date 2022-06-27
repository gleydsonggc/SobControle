package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Perfil implements Serializable {

    private String id;
    private String nome;
    private List<String> dispositivosPermitidos;

    public Perfil() {
    }

    public Perfil(String id, String nome) {
        this.id = id;
        this.nome = nome != null ? nome : "";
        this.dispositivosPermitidos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getDispositivosPermitidos() {
        return dispositivosPermitidos;
    }

    public void setDispositivosPermitidos(List<String> dispositivosPermitidos) {
        this.dispositivosPermitidos = dispositivosPermitidos;
    }

    public boolean podeAcessarDispositivo(String dispositivoId) {
        return dispositivosPermitidos.contains(dispositivoId);
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", dispositivosPermitidos=" + dispositivosPermitidos +
                '}';
    }
}