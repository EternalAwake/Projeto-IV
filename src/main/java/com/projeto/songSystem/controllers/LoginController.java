package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.models.enums.Role;
import com.projeto.songSystem.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String login(Model model, HttpSession session) {
        if (session != null && session.getAttribute("usuarioDTO") != null) {
            return "redirect:/inicio";
        }
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "login";
    }

    @PostMapping("/login/autenticar")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("senha") String senha,
                               HttpServletRequest request,
                               RedirectAttributes attributes) {
        try {
            UsuarioModel usuario = usuarioService.autenticar(username, senha);

            if (usuario == null) {
                attributes.addFlashAttribute("erro",
                        "Usuário/e-mail ou senha incorretos. Verifique e tente novamente.");
                return "redirect:/login";
            }

            // Admin no primeiro acesso (sem senha definida): redireciona para setup
            if (usuario.isPrimeiroAcesso()) {
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) oldSession.invalidate();
                HttpSession setupSession = request.getSession(true);
                // Guarda apenas o ID temporariamente — sem criar sessão completa
                setupSession.setAttribute("adminSetupId", usuario.getId());
                setupSession.setMaxInactiveInterval(10 * 60); // 10 minutos para concluir setup
                return "redirect:/admin/setup";
            }

            // Login normal
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) oldSession.invalidate();

            HttpSession newSession = request.getSession(true);
            UsuarioDTO usuarioDTO = usuarioService.buscarPorId(usuario.getId());
            newSession.setAttribute("usuarioDTO", usuarioDTO);
            newSession.setMaxInactiveInterval(30 * 60);

            return "redirect:/inicio";

        } catch (Exception e) {
            attributes.addFlashAttribute("erro",
                    "Usuário/e-mail ou senha incorretos. Verifique e tente novamente.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "redirect:/login?logout";
    }
}
