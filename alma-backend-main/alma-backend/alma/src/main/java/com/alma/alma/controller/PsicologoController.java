package com.alma.alma.controller;

import com.alma.alma.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PsicologoController {

    @GetMapping("/psicologo/dashboard")
    public String dashboardPsicologo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null || !usuario.getTipo().equalsIgnoreCase("psicologo")) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "dashboard-psicologo";
    }
}
