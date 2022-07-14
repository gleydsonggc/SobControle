package com.example.sobcontrole.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private List<Dispositivo> dispositivos = Arrays.asList(new Dispositivo[8]);
    private List<Perfil> perfis;
    private Perfil perfilAtivo;
    private Integer pin;
    private String centralUrl;

    public Usuario() {
        inicializarDispositivos();
        this.perfis = new ArrayList<>();
    }

    private void inicializarDispositivos() {
        for (int i = 0; i < dispositivos.size(); i++) {
            dispositivos.set(i, new Dispositivo(String.valueOf(i + 1), "", false));
        }
    }

    public Usuario(String nome, String email) {
        inicializarDispositivos();
        this.nome = nome;
        this.email = email;
        this.perfis = new ArrayList<>();
    }

    public Usuario(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dispositivos = usuario.getDispositivos().stream().map(Dispositivo::new).collect(Collectors.toList());
        this.perfis = usuario.getPerfis().stream().map(Perfil::new).collect(Collectors.toList());
        this.perfilAtivo = usuario.getPerfilAtivo() == null ? null : new Perfil(usuario.getPerfilAtivo());
        this.pin = new Integer(usuario.getPin());
        this.centralUrl = usuario.getCentralUrl();
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

    public void setDispositivos(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
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

    public String getCentralUrl() {
        return centralUrl;
    }

    public void setCentralUrl(String centralUrl) {
        this.centralUrl = centralUrl;
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

    @Exclude
    public List<Dispositivo> getDispositivosPodemSerExibidos() {
        if (perfilAtivo == null) {
            return getDispositivosHabilitados();
        } else {
            return dispositivos.stream()
                    .filter(dispositivo -> perfilAtivo.podeAcessarDispositivo(dispositivo.getId()))
                    .collect(Collectors.toList());
        }
    }

    @Exclude
    @NonNull
    public List<Dispositivo> getDispositivosHabilitados() {
        return dispositivos.stream()
                .filter(Dispositivo::isHabilitado)
                .collect(Collectors.toList());
    }

    public List<Dispositivo> getDispositivosPodemSerExibidosDoPerfil(String perfilId) {
        if (perfilId == null || perfilId.isEmpty()) {
            return getDispositivosHabilitados();
        }
        return dispositivos.stream()
                .filter(Dispositivo::isHabilitado)
                .filter(dispositivo -> getPerfilComId(perfilId).podeAcessarDispositivo(dispositivo.getId()))
                .collect(Collectors.toList());
    }

    public Perfil getPerfilComId(String id) {
        return perfis.stream()
                .filter(perfil -> perfil.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Exclude
    public List<Perfil> getPerfisOrdemAlfabetica() {
        return perfis.stream()
                .sorted(Comparator.comparing(Perfil::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public boolean adicionarPerfil(String nome, List<String> dispositivosPermitidos) {
        return perfis.add(new Perfil(String.valueOf(perfis.size() + 1), nome, dispositivosPermitidos));
    }

    public boolean removerPerfil(Perfil perfil) {
        Perfil perfilEncontrado = getPerfilComId(perfil.getId());
        if (perfilEncontrado == null) {
            return false;
        }
        if (isPerfilAtivo(perfilEncontrado)) {
            desativarPerfil();
        }
        return perfis.remove(perfilEncontrado);
    }

    public boolean isPerfilAtivo(Perfil perfil) {
        if (perfilAtivo == null || perfil == null) return false;
        return perfilAtivo.equals(perfil);
    }

    public void ativarPerfil(Perfil perfil) {
        this.perfilAtivo = getPerfilComId(perfil.getId());
    }

    public void desativarPerfil() {
        this.perfilAtivo = null;
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