package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.UUID;

public class Controlavel implements Serializable {

    private String id;

    private String nome;

    public Controlavel() {
        this.id = UUID.randomUUID().toString();
    }

    public Controlavel(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
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

    @Override
    public String toString() {
        return nome + " " + id.substring(0, 4);
    }
}