package com.alma.alma.controller;

import com.alma.alma.model.RegistroHumor;
import com.alma.alma.model.Usuario;
import com.alma.alma.service.HumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HumorController {

    @Autowired
    private HumorService humorService;

 

    // Logout, remove sessão
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // Listar registros do usuário logado
    @GetMapping("/humor/meus-registros")
    public List<RegistroHumor> listarMeusRegistros(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        return humorService.listarPorUsuario(usuario);
    }

    // Salvar novo registro para usuário logado
    @PostMapping("/humor/registros")
    public RegistroHumor salvarRegistro(@RequestBody RegistroHumor registro, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        registro.setUsuario(usuario);
        return humorService.salvarRegistro(registro);
    }

    // Psicólogo listar registros de um usuário pelo ID
    @GetMapping("/humor/usuario/{usuarioId}")
    public List<RegistroHumor> listarPorUsuarioId(@PathVariable Long usuarioId, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        // Garante que apenas psicólogos logados possam acessar
        if (usuarioLogado == null || !usuarioLogado.getTipo().equalsIgnoreCase("psicologo")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }
        return humorService.listarPorUsuarioId(usuarioId);
    }
}