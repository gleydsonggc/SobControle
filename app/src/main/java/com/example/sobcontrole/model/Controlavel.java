package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.UUID;

public class Controlavel implements Serializable {

    private static int releCounter = 0;

    private String id;
    private String nome;
    private boolean habilitado;
    private int rele;

    public Controlavel() {
        this("");
    }

    public Controlavel(String nome) {
        this(nome, false);
    }

    public Controlavel(String nome, boolean habilitado) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome != null ? nome : "";
        this.habilitado = habilitado;
        this.rele = ++releCounter;
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
        return nome;
    }
}