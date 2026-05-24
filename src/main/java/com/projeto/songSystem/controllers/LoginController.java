package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.RepertorioEstatisticasDTO;
import com.projeto.songSystem.dto.RepertorioItemDTO;
import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import com.projeto.songSystem.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "login";
    }

    @PostMapping("/login/autenticar")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("senha") String senha,
                               HttpSession session,
                               RedirectAttributes attributes) {

        try {
            UsuarioModel usuario = usuarioService.autenticar(username, senha);

            if (usuario != null && senha.equals(usuario.getSenha())) {
                // Converter para DTO antes de salvar na sessão
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setUsername(usuario.getUsername());
                usuarioDTO.setEmail(usuario.getEmail());
                usuarioDTO.setNome(usuario.getNome());

                session.setAttribute("usuarioDTO", usuarioDTO);
                return "redirect:/inicio";
            } else {
                attributes.addFlashAttribute("erro", "Usuário ou senha inválidos!");
                return "redirect:/login";
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Usuário não encontrado!");
            return "redirect:/login";
        }
    }
}
