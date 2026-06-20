package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.UsuarioDTO;
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
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String exibirPerfil(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        UsuarioDTO dadosAtuais = usuarioService.buscarPorId(usuarioDto.getId());
        model.addAttribute("usuarioDTO", dadosAtuais);
        return "perfil";
    }

    @PostMapping("/trocar-senha")
    public String trocarSenha(@RequestParam("senhaAtual") String senhaAtual,
                              @RequestParam("novaSenha") String novaSenha,
                              @RequestParam("confirmarNovaSenha") String confirmarNovaSenha,
                              HttpSession session,
                              RedirectAttributes attributes) {

        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        try {
            usuarioService.trocarSenha(usuarioDto.getId(), senhaAtual, novaSenha, confirmarNovaSenha);
            attributes.addFlashAttribute("mensagem", "Senha alterada com sucesso!");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao alterar a senha. Tente novamente.");
        }
        return "redirect:/perfil";
    }

    @PostMapping("/pergunta-secreta")
    public String salvarPerguntaSecreta(@RequestParam("perguntaSecreta") String pergunta,
                                        @RequestParam("respostaSecreta") String resposta,
                                        HttpSession session,
                                        RedirectAttributes attributes) {

        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        try {
            usuarioService.salvarPerguntaSecreta(usuarioDto.getId(), pergunta, resposta);
            attributes.addFlashAttribute("mensagem", "Pergunta secreta salva com sucesso!");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar. Tente novamente.");
        }
        return "redirect:/perfil";
    }
}
