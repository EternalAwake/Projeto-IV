package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.enums.Role;
import com.projeto.songSystem.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    // ─── Setup inicial do admin ───────────────────────────────────────────────

    /**
     * Página de setup exibida no primeiro login do admin (sem sessão completa,
     * apenas adminSetupId na sessão temporária).
     */
    @GetMapping("/setup")
    public String exibirSetup(HttpSession session, RedirectAttributes attributes) {
        Long adminSetupId = (Long) session.getAttribute("adminSetupId");
        if (adminSetupId == null) {
            // Sessão de setup expirada ou acesso direto inválido
            attributes.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/login";
        }
        return "admin-setup";
    }

    @PostMapping("/setup/salvar")
    public String salvarSetup(@RequestParam("novaSenha") String novaSenha,
                              @RequestParam("confirmar") String confirmar,
                              @RequestParam("pergunta") String pergunta,
                              @RequestParam("resposta") String resposta,
                              HttpSession session,
                              RedirectAttributes attributes) {

        Long adminSetupId = (Long) session.getAttribute("adminSetupId");
        if (adminSetupId == null) {
            attributes.addFlashAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "redirect:/login";
        }

        try {
            usuarioService.concluirSetupAdmin(adminSetupId, novaSenha, confirmar, pergunta, resposta);
            // Invalida sessão temporária de setup e manda para o login com mensagem
            session.invalidate();
            attributes.addFlashAttribute("mensagem",
                    "Configuração concluída! Faça login com sua nova senha.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/admin/setup";
        }
    }

    // ─── Painel de usuários ───────────────────────────────────────────────────

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, HttpSession session) {
        UsuarioDTO admin = (UsuarioDTO) session.getAttribute("usuarioDTO");
        model.addAttribute("usuarioDTO", admin);

        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());

        return "admin-usuarios";
    }

    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public String excluirUsuario(@PathVariable Long id, HttpSession session) {
        UsuarioDTO admin = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (admin == null) return "erro:Sessão expirada";
        try {
            usuarioService.excluirUsuario(admin.getId(), id);
            return "ok";
        } catch (IllegalArgumentException e) {
            return "erro:" + e.getMessage();
        } catch (Exception e) {
            return "erro:Não foi possível excluir o usuário.";
        }
    }

    @PostMapping("/usuarios/{id}/tornar-admin")
    @ResponseBody
    public String tornarAdmin(@PathVariable Long id, HttpSession session) {
        UsuarioDTO admin = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (admin == null) return "erro:Sessão expirada";
        try {
            usuarioService.alterarRole(admin.getId(), id, Role.ADMIN);
            return "ok";
        } catch (IllegalArgumentException e) {
            return "erro:" + e.getMessage();
        } catch (Exception e) {
            return "erro:Não foi possível promover o usuário.";
        }
    }

    @PostMapping("/usuarios/{id}/remover-admin")
    @ResponseBody
    public String removerAdmin(@PathVariable Long id, HttpSession session) {
        UsuarioDTO admin = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (admin == null) return "erro:Sessão expirada";
        try {
            usuarioService.alterarRole(admin.getId(), id, Role.USER);
            return "ok";
        } catch (IllegalArgumentException e) {
            return "erro:" + e.getMessage();
        } catch (Exception e) {
            return "erro:Não foi possível rebaixar o usuário.";
        }
    }
}
