package com.alma.alma.service;

import com.alma.alma.model.RegistroHumor;
import com.alma.alma.model.Usuario;
import com.alma.alma.repository.RegistroHumorRepository;
import com.alma.alma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HumorService {

    @Autowired
    private RegistroHumorRepository registroHumorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Mantido para buscar usuário pelo ID, se necessário

    public List<RegistroHumor> listarPorUsuario(Usuario usuario) {
        return registroHumorRepository.findByUsuarioOrderByDataDesc(usuario);
    }

    public List<RegistroHumor> listarPorUsuarioId(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        return registroHumorRepository.findByUsuarioOrderByDataDesc(usuarioOpt.get());
    }

    public RegistroHumor salvarRegistro(RegistroHumor registro) {
        if (registro.getData() == null) {
            registro.setData(LocalDateTime.now());
        }
        return registroHumorRepository.save(registro);
    }

    // O método 'autenticar' foi removido daqui.
    // A lógica de autenticação agora está apenas no UsuarioController (e potencialmente no Spring Security).
}