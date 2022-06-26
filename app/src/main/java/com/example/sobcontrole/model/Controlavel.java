package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.UUID;

public class Controlavel implements Serializable {

    private String id;
    private String nome;
    private boolean habilitado;
    private int rele;

    public Controlavel() {
    }

    public Controlavel(String id, String nome, boolean habilitado, int rele) {
        this.id = id;
        this.nome = nome != null ? nome : "";
        this.habilitado = habilitado;
        this.rele = rele;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "Controlavel{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", habilitado=" + habilitado +
                ", rele=" + rele +
                '}';
    }
}