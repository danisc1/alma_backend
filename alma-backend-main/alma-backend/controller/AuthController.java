package com.alma.controller;

import com.alma.model.Usuario;
import com.alma.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public String register(@RequestBody Usuario usuario) {
        usuarioService.cadastrar(usuario);
        return "Cadastro realizado com sucesso";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario credenciais, HttpSession session) {
        Usuario usuario = usuarioService.autenticar(credenciais.getEmail(), credenciais.getSenha());
        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            return "Login realizado com sucesso";
        } else {
            return "Credenciais inválidas";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logout realizado";
    }

    @GetMapping("/me")
    public Usuario usuarioLogado(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }
}
