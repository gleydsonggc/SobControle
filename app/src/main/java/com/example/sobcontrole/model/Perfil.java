package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Perfil implements Serializable {

    private String id;
    private String nome;
    private List<String> dispositivosPermitidos;

    public Perfil() {
        this.dispositivosPermitidos = new ArrayList<>();
    }

    public Perfil(String id, String nome) {
        this.id = id;
        this.nome = nome != null ? nome : "";
        this.dispositivosPermitidos = new ArrayList<>();
    }

    public Perfil(Perfil perfil) {
        this.id = perfil.getId();
        this.nome = perfil.getNome();
        this.dispositivosPermitidos = new ArrayList<>(perfil.getDispositivosPermitidos());
    }

    public Perfil(String id, String nome, List<String> dispositivosPermitidos) {
        this.id = id;
        this.nome = nome;
        this.dispositivosPermitidos = dispositivosPermitidos;
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

    public void removerDispositivosPermitidos() {
        this.dispositivosPermitidos = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(id, perfil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}