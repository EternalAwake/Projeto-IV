package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.SetlistDTO;
import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.RepertorioItemModel;
import com.projeto.songSystem.services.SetlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping
public class SetListsController {

    @Autowired
    private SetlistService setlistService;

    // Listar todos os setlists do usuário
    @GetMapping("/repertorio/setlists")
    public String listarSetlists(Model model, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        System.out.println("Usuário logado: " + usuario.getUsername() + " (ID: " + usuario.getId() + ")");

        List<SetlistDTO> setlists = setlistService.listarSetlistsPorUsuario(usuario.getId());
        model.addAttribute("setlists", setlists);
        return "setlists";
    }

    // Página de criação (modal já resolve)
    @PostMapping("/repertorio/setlists/criar")
    public String criarSetlist(@RequestParam String nome,
                               @RequestParam(required = false) String descricao,
                               HttpSession session,
                               RedirectAttributes attributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            SetlistDTO setlist = setlistService.criarSetlist(nome, descricao, usuario.getId());
            attributes.addFlashAttribute("mensagem", "Setlist criado com sucesso!");
            return "redirect:/repertorio/setlists";
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/repertorio/setlists";
        }
    }

    // Visualizar um setlist específico
    @GetMapping("/repertorio/visualizar/{id}")
    public String visualizarSetlist(@PathVariable Long id, Model model, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            SetlistDTO setlist = setlistService.buscarSetlistPorId(id);
            model.addAttribute("setlist", setlist);

            List<RepertorioItemModel> musicasDisponiveis = setlistService.listarMusicasDisponiveisParaSetlist(usuario.getId(), id);
            model.addAttribute("musicasDisponiveis", musicasDisponiveis);

            return "visualizarSetlist";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/repertorio//setlists";
        }
    }

    // Excluir setlist
    @PostMapping("/repertorio/setlists/{id}/excluir")
    public String excluirSetlist(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            setlistService.excluirSetlist(id);
            attributes.addFlashAttribute("mensagem", "Setlist excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/repertorio/setlists";
    }
}