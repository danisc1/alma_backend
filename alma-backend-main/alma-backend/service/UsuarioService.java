package com.alma.service;

import com.alma.model.Usuario;
import com.alma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticar(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getSenha().equals(senha))
                .orElse(null);
    }
}
