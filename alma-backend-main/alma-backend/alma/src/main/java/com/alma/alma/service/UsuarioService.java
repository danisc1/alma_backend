package com.alma.alma.service;

import com.alma.alma.model.Usuario;
import com.alma.alma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean validarSenha(String senhaDigitada, String senhaBanco) {
        return passwordEncoder.matches(senhaDigitada, senhaBanco);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}
