package com.example.sobcontrole.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private List<Controlavel> controlaveis = Arrays.asList(new Controlavel[8]);
    private List<Perfil> perfis;
    private Perfil perfilAtivo;

    public Usuario() {
        inicializarControlaveis();
        this.perfis = new ArrayList<>();
    }

    private void inicializarControlaveis() {
        for (int i = 0; i < controlaveis.size(); i++) {
            controlaveis.set(i, new Controlavel());
        }
    }

    public Usuario(String nome, String email, String senha) {
        inicializarControlaveis();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfis = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Controlavel> getControlaveis() {
        return controlaveis;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public Perfil getPerfilAtivo() {
        return perfilAtivo;
    }

    public void setPerfilAtivo(Perfil perfilAtivo) {
        this.perfilAtivo = perfilAtivo;
    }
}