package com.projeto.songSystem.controllers;

import com.projeto.songSystem.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Fluxo de recuperação de senha por pergunta secreta (3 passos):
 *
 *  1. GET  /recuperar-senha            → formulário: informa username/e-mail
 *  2. POST /recuperar-senha/pergunta   → exibe a pergunta secreta do usuário
 *  3. POST /recuperar-senha/redefinir  → valida resposta e redefine a senha
 *
 * Todas as rotas são públicas (sem sessão exigida).
 */
@Controller
@RequestMapping("/recuperar-senha")
public class RecuperarSenhaController {

    @Autowired
    private UsuarioService usuarioService;

    // Passo 1: formulário de identificação
    @GetMapping
    public String exibirFormulario() {
        return "recuperar-senha";
    }

    // Passo 2: recebe o username/e-mail e exibe a pergunta secreta
    @PostMapping("/pergunta")
    public String exibirPergunta(@RequestParam("usernameOuEmail") String usernameOuEmail,
                                 Model model,
                                 RedirectAttributes attributes) {

        String pergunta = usuarioService.buscarPerguntaSecreta(usernameOuEmail.trim());

        // Mensagem genérica — não revelamos se o usuário existe ou não
        if (pergunta == null) {
            attributes.addFlashAttribute("erro",
                    "Usuário não encontrado ou sem pergunta secreta cadastrada. " +
                    "Verifique o e-mail/usuário ou entre em contato com o administrador.");
            return "redirect:/recuperar-senha";
        }

        model.addAttribute("pergunta", pergunta);
        model.addAttribute("usernameOuEmail", usernameOuEmail.trim());
        return "recuperar-senha-pergunta";
    }

    // Passo 3: valida resposta e redefine a senha
    @PostMapping("/redefinir")
    public String redefinirSenha(@RequestParam("usernameOuEmail") String usernameOuEmail,
                                 @RequestParam("resposta") String resposta,
                                 @RequestParam("novaSenha") String novaSenha,
                                 @RequestParam("confirmar") String confirmar,
                                 Model model,
                                 RedirectAttributes attributes) {
        try {
            boolean ok = usuarioService.redefinirSenhaComResposta(
                    usernameOuEmail.trim(), resposta, novaSenha, confirmar);

            if (!ok) {
                // Resposta errada — reexibe a pergunta sem revelar o motivo exato
                String pergunta = usuarioService.buscarPerguntaSecreta(usernameOuEmail.trim());
                model.addAttribute("pergunta", pergunta);
                model.addAttribute("usernameOuEmail", usernameOuEmail.trim());
                model.addAttribute("erro", "Resposta incorreta. Tente novamente.");
                return "recuperar-senha-pergunta";
            }

            attributes.addFlashAttribute("mensagem",
                    "Senha redefinida com sucesso! Faça login com sua nova senha.");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            String pergunta = usuarioService.buscarPerguntaSecreta(usernameOuEmail.trim());
            model.addAttribute("pergunta", pergunta);
            model.addAttribute("usernameOuEmail", usernameOuEmail.trim());
            model.addAttribute("erro", e.getMessage());
            return "recuperar-senha-pergunta";
        }
    }
}
