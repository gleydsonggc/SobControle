package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.UUID;

public class Dispositivo implements Serializable {

    private String id;
    private String nome;
    private boolean habilitado;
    private boolean ligado;
    private int rele;

    public Dispositivo() {
    }

    public Dispositivo(String id, String nome, boolean habilitado, int rele) {
        this.id = id;
        this.nome = nome != null ? nome : "";
        this.habilitado = habilitado;
        this.rele = rele;
    }

    public Dispositivo(Dispositivo dispositivo) {
        this.id = dispositivo.getId();
        this.nome = dispositivo.getNome();
        this.habilitado = dispositivo.isHabilitado();
        this.ligado = dispositivo.isLigado();
        this.rele = dispositivo.getRele();
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

    public boolean isLigado() {
        return ligado;
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public int getRele() {
        return rele;
    }

    public void setRele(int rele) {
        this.rele = rele;
    }

    @Override
    public String toString() {
        return "Dispositivo{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", habilitado=" + habilitado +
                ", ligado=" + ligado +
                ", rele=" + rele +
                '}';
    }
}