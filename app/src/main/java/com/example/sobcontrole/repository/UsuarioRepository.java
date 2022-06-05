package com.example.sobcontrole.repository;

import com.example.sobcontrole.model.Controlavel;
import com.example.sobcontrole.model.Perfil;
import com.example.sobcontrole.model.Usuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UsuarioRepository {

    private static volatile UsuarioRepository instance;

    private Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioLogado;

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            synchronized (UsuarioRepository.class) {
                if (instance == null) {
                    instance = new UsuarioRepository();
                }
            }
        }
        return instance;
    }

    private UsuarioRepository() {
//        ArrayList<Controlavel> controlaveis = new ArrayList<>();
//        controlaveis.add(new Controlavel("Quarto"));
//        controlaveis.add(new Controlavel("Sala"));
//        controlaveis.add(new Controlavel("Ar-condicionadoo"));
//        controlaveis.add(new Controlavel("Cafeteira"));
//        controlaveis.add(new Controlavel("Ventilador"));
//        controlaveis.add(new Controlavel("Cozinha"));
//        controlaveis.add(new Controlavel("Porta"));
//        controlaveis.add(new Controlavel("Despensa"));

        Usuario usuario = new Usuario("Fulano da Silva", "fulano@fulanomail.com", "1");
//        usuario.getControlaveis().addAll(controlaveis);
        usuario.getControlaveis().set(0, new Controlavel("Quarto"));
        usuario.getControlaveis().set(1, new Controlavel("Sala"));
        usuario.getControlaveis().set(2, new Controlavel("Ar-condicionadoo"));
        usuario.getControlaveis().set(3, new Controlavel("Cafeteira"));
        usuario.getControlaveis().set(4, new Controlavel("Ventilador"));
        usuario.getControlaveis().set(5, new Controlavel("Cozinha"));
        usuario.getControlaveis().set(6, new Controlavel("Porta"));
        usuario.getControlaveis().set(7, new Controlavel("Despensa"));

        ArrayList<Perfil> perfis = new ArrayList<>();
        perfis.add(new Perfil("Filho 1"));
        perfis.add(new Perfil("Filho 2"));
        perfis.add(new Perfil("Filho 3"));
        usuario.getPerfis().addAll(perfis);

        adicionar(usuario);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public List<Usuario> listar() {
        return usuarios.values().stream()
                .sorted(Comparator.comparing(Usuario::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Usuario adicionar(Usuario usuario) {
        usuario.setId(UUID.randomUUID().toString());
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public Usuario ler(String id) {
        return usuarios.get(id);
    }

    public Usuario atualizar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public boolean deletar(String id) {
        return usuarios.remove(id) != null;
    }

    public Usuario buscarPorEmailESenha(String email, String senha) {
        return UsuarioRepository.getInstance().listar().stream()
                .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                .filter(usuario -> usuario.getSenha().equalsIgnoreCase(senha))
                .findFirst()
                .orElse(null);
    }

    public boolean logarComEmailESenha(String email, String senha) {
        Usuario usuarioEncontrado = UsuarioRepository.getInstance().listar().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .filter(u -> u.getSenha().equalsIgnoreCase(senha))
                .findFirst()
                .orElse(null);
        usuarioLogado = usuarioEncontrado;
        return usuarioLogado != null;
    }

    public boolean logarComUsuario(Usuario usuario) {
        Usuario usuarioEncontrado = UsuarioRepository.getInstance().listar().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(usuario.getEmail()))
                .filter(u -> u.getSenha().equalsIgnoreCase(usuario.getSenha()))
                .findFirst()
                .orElse(null);
        usuarioLogado = usuarioEncontrado;
        return usuarioLogado != null;
    }

    public void logout() {
        usuarioLogado = null;
    }

}
