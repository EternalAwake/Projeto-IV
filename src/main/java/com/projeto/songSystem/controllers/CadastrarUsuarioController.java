package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class CadastrarUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "CadastrarUsuario";
    }

    @PostMapping("/cadastro/salvar")
    public String salvarCadastro(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
                                 @RequestParam("confirmarSenha") String confirmarSenha,
                                 RedirectAttributes attributes) {

        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().trim().isEmpty()) {
            attributes.addFlashAttribute("erro", "O nome é obrigatório!");
            return "redirect:/cadastro";
        }
        if (usuarioDTO.getUsername() == null || usuarioDTO.getUsername().trim().isEmpty()) {
            attributes.addFlashAttribute("erro", "O usuário é obrigatório!");
            return "redirect:/cadastro";
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            attributes.addFlashAttribute("erro", "O e-mail é obrigatório!");
            return "redirect:/cadastro";
        }
        // Mínimo 8 caracteres — alinhado com trocarSenha e com o HTML
        if (usuarioDTO.getSenha() == null || usuarioDTO.getSenha().length() < 8) {
            attributes.addFlashAttribute("erro", "A senha deve ter no mínimo 8 caracteres!");
            return "redirect:/cadastro";
        }
        if (!usuarioDTO.getSenha().equals(confirmarSenha)) {
            attributes.addFlashAttribute("erro", "As senhas não coincidem!");
            return "redirect:/cadastro";
        }

        try {
            usuarioService.cadastrarUsuario(usuarioDTO);
            attributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/login";
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }
}
