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
        Usuario usuario = new Usuario("Fulano da Silva", "fulano@fulanomail.com", "1");

        usuario.getControlaveis().get(0).setNome("Quarto");
        usuario.getControlaveis().get(1).setNome("Sala");
        usuario.getControlaveis().get(2).setNome("Ar-condicionado");
        usuario.getControlaveis().get(3).setNome("Cafeteira");
        usuario.getControlaveis().get(4).setNome("Ventilador");
        usuario.getControlaveis().get(5).setNome("Cozinha");
        usuario.getControlaveis().get(6).setNome("Porta");
        usuario.getControlaveis().get(7).setNome("Despensa");

        ArrayList<Perfil> perfis = new ArrayList<>();
//        perfis.add(new Perfil("Filho 1"));
//        perfis.add(new Perfil("Filho 2"));
//        perfis.add(new Perfil("Filho 3"));
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
                .findFirst()
                .orElse(null);
    }

    public boolean logarComEmailESenha(String email, String senha) {
        Usuario usuarioEncontrado = UsuarioRepository.getInstance().listar().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
        usuarioLogado = usuarioEncontrado;
        return usuarioLogado != null;
    }

    public boolean logarComUsuario(Usuario usuario) {
        Usuario usuarioEncontrado = UsuarioRepository.getInstance().listar().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(usuario.getEmail()))
                .findFirst()
                .orElse(null);
        usuarioLogado = usuarioEncontrado;
        return usuarioLogado != null;
    }

    public void logout() {
        usuarioLogado = null;
    }

}
