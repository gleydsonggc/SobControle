package com.example.sobcontrole.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private List<Dispositivo> dispositivos = Arrays.asList(new Dispositivo[8]);
    private List<Perfil> perfis;
    private Perfil perfilAtivo;
    private Integer pin;

    public Usuario() {
        inicializarDispositivos();
        this.perfis = new ArrayList<>();
    }

    private void inicializarDispositivos() {
        for (int i = 0; i < dispositivos.size(); i++) {
            dispositivos.set(i, new Dispositivo(String.valueOf(i + 1), "", false, i + 1));
        }
    }

    public Usuario(String nome, String email, String senha) {
        inicializarDispositivos();
        this.nome = nome;
        this.email = email;
        this.perfis = new ArrayList<>();
    }

    @Exclude
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

    public List<Dispositivo> getDispositivos() {
        return dispositivos;
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

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        Objects.requireNonNull(pin, "O valor do PIN não pode ser nulo.");
        if (!validaPin(String.valueOf(pin))) {
            throw new IllegalArgumentException("O valor do PIN deve ser positivo e conter seis dígitos.");
        }
        this.pin = pin;
    }

    public static boolean validaPin(String pin) {
        if (pin == null) return false;
        if (pin.trim().length() != 6) return false;
        Integer tempPin;
        try {
            tempPin = Integer.valueOf(pin);
        } catch (NumberFormatException e) {
            return false;
        }
        if (tempPin < 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dispositivos=" + dispositivos +
                ", perfis=" + perfis +
                ", perfilAtivo=" + perfilAtivo +
                ", pin=" + pin +
                '}';
    }
}