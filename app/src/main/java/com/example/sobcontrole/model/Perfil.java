package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Perfil implements Serializable {

    private String id;
    private String nome;
    private List<String> controlaveisPermitidos;

    public Perfil() {
    }

    public Perfil(String id, String nome) {
        this.id = id;
        this.nome = nome != null ? nome : "";
        this.controlaveisPermitidos = new ArrayList<>();
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

    public List<String> getControlaveisPermitidos() {
        return controlaveisPermitidos;
    }

    public void setControlaveisPermitidos(List<String> controlaveisPermitidos) {
        this.controlaveisPermitidos = controlaveisPermitidos;
    }

    public boolean podeAcessarControlavel(String controlavelId) {
        return controlaveisPermitidos.contains(controlavelId);
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", controlaveisPermitidos=" + controlaveisPermitidos +
                '}';
    }
}