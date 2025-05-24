package com.alma.alma.controller;

import com.alma.alma.model.Usuario;
import com.alma.alma.repository.RegistroHumorRepository;
import com.alma.alma.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RegistroHumorRepository registroHumorRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, String>> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getTipo() == null || usuario.getTipo().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Erro: tipo do usuário é obrigatório (paciente ou psicologo)"));
        }

        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email já cadastrado!"));
        }

        usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok(Map.of("message", "Usuário cadastrado com sucesso!"));
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String email = loginData.get("email");
        String senha = loginData.get("senha");

        Optional<Usuario> userOpt = usuarioService.buscarPorEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }

        Usuario usuario = userOpt.get();
        if (!usuarioService.validarSenha(senha, usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }

        session.setAttribute("usuarioLogado", usuario);
        return ResponseEntity.ok(usuario.getTipo());
    }

    @GetMapping("/logado")
    public ResponseEntity<?> getUsuarioLogado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário não autenticado."));
        }
        Usuario userResponse = new Usuario();
        userResponse.setId(usuario.getId());
        userResponse.setNome(usuario.getNome());
        userResponse.setEmail(usuario.getEmail());
        userResponse.setTipo(usuario.getTipo());
        userResponse.setFotoPerfil(usuario.getFotoPerfil());
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/upload-foto")
    public ResponseEntity<String> uploadFotoPerfil(@RequestParam("foto") MultipartFile foto, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        Optional<Usuario> userOpt = usuarioService.buscarPorId(usuarioLogado.getId());
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = userOpt.get();
        try {
            File uploadDir = new File("src/main/resources/static/uploads/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String nomeArquivo = "perfil_" + usuario.getId() + "_" + foto.getOriginalFilename();
            String caminho = uploadDir.getAbsolutePath() + File.separator + nomeArquivo;
            File dest = new File(caminho);
            foto.transferTo(dest);
            usuario.setFotoPerfil(nomeArquivo);
            usuarioService.salvarUsuario(usuario);
            return ResponseEntity.ok("Foto enviada com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    @DeleteMapping("/apagar-conta")
    public ResponseEntity<String> excluirConta(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        Long id = usuarioLogado.getId();

        Optional<Usuario> userOpt = usuarioService.buscarPorId(id);
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");

        Usuario usuario = userOpt.get();

        registroHumorRepository.deleteByUsuarioId(id);

        if (usuario.getFotoPerfil() != null) {
            String caminho = "src/main/resources/static/uploads/" + usuario.getFotoPerfil();
            File file = new File(caminho);
            if (file.exists()) file.delete();
        }

        session.invalidate();

        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok("Conta e dados excluídos com sucesso!");
    }
}
