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

    @GetMapping("/repertorio/setlists")
    public String listarSetlists(Model model, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        List<SetlistDTO> setlists = setlistService.listarSetlistsPorUsuario(usuario.getId());
        model.addAttribute("setlists", setlists);
        model.addAttribute("usuarioDTO", usuario);

        // ── Estatísticas ──────────────────────────────────────────────
        // Total de setlists
        model.addAttribute("totalSetlists", setlists.size());

        // Total de músicas somando os itens de cada setlist
        int totalMusicas = setlists.stream()
                .mapToInt(s -> s.getTotalMusicas() != null ? s.getTotalMusicas() : 0)
                .sum();
        model.addAttribute("totalMusicasEmSetlists", totalMusicas);

        // Duração total: SetlistDTO.duracaoTotal é em segundos (pode ser null)
        int totalSegundos = setlists.stream()
                .mapToInt(s -> s.getDuracaoTotal() != null ? s.getDuracaoTotal() : 0)
                .sum();
        String duracaoFormatada = formatarDuracao(totalSegundos);
        model.addAttribute("duracaoTotal", duracaoFormatada);

        // Nome do setlist criado mais recentemente
        String ultimoSetlist = setlists.stream()
                .filter(s -> s.getDataCriacao() != null)
                .max((a, b) -> a.getDataCriacao().compareTo(b.getDataCriacao()))
                .map(SetlistDTO::getNome)
                .orElse("—");
        model.addAttribute("ultimoSetlist", ultimoSetlist);

        return "setlists";
    }

    @PostMapping("/repertorio/setlists/criar")
    public String criarSetlist(@RequestParam String nome,
                                @RequestParam(required = false) String descricao,
                                HttpSession session,
                                RedirectAttributes attributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            setlistService.criarSetlist(nome, descricao, usuario.getId());
            attributes.addFlashAttribute("mensagem", "Setlist \"" + nome + "\" criado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/repertorio/setlists";
    }

    @GetMapping("/repertorio/visualizar/{id}")
    public String visualizarSetlist(@PathVariable Long id, Model model, HttpSession session) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            SetlistDTO setlist = setlistService.buscarSetlistPorId(id);

            if (!setlist.getUsuarioId().equals(usuario.getId())) {
                return "redirect:/repertorio/setlists?erro=acesso_negado";
            }

            model.addAttribute("setlist", setlist);
            model.addAttribute("usuarioDTO", usuario);

            List<RepertorioItemModel> musicasDisponiveis =
                    setlistService.listarMusicasDisponiveisParaSetlist(usuario.getId(), id);
            model.addAttribute("musicasDisponiveis", musicasDisponiveis);

            return "visualizarSetlist";
        } catch (Exception e) {
            return "redirect:/repertorio/setlists";
        }
    }

    @PostMapping("/repertorio/setlists/{id}/excluir")
    public String excluirSetlist(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes attributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            SetlistDTO setlist = setlistService.buscarSetlistPorId(id);

            if (!setlist.getUsuarioId().equals(usuario.getId())) {
                attributes.addFlashAttribute("erro", "Você não tem permissão para excluir este setlist.");
                return "redirect:/repertorio/setlists";
            }

            setlistService.excluirSetlist(id);
            attributes.addFlashAttribute("mensagem", "Setlist excluído com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/repertorio/setlists";
    }


    /**
     * Adiciona uma música (via RepertorioItem) ao setlist.
     * Rota chamada pelos forms em visualizarSetlist.html.
     */
    @PostMapping("/repertorio/setlists/{setlistId}/adicionar")
    public String adicionarMusicaAoSetlist(@PathVariable Long setlistId,
                                            @RequestParam Long repertorioItemId,
                                            HttpSession session,
                                            RedirectAttributes attributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            SetlistDTO setlist = setlistService.buscarSetlistPorId(setlistId);
            if (!setlist.getUsuarioId().equals(usuario.getId())) {
                attributes.addFlashAttribute("erro", "Acesso negado.");
                return "redirect:/repertorio/setlists";
            }
            setlistService.adicionarMusicaAoSetlist(setlistId, repertorioItemId, null);
            attributes.addFlashAttribute("mensagem", "Música adicionada ao setlist!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao adicionar música: " + e.getMessage());
        }
        return "redirect:/repertorio/visualizar/" + setlistId;
    }

    /**
     * Remove um item do setlist pelo ID do SetlistItem.
     * Rota chamada pelo JS de remover em visualizarSetlist.html.
     */
    @PostMapping("/repertorio/setlists/item/{itemId}/remover")
    public String removerMusicaDoSetlist(@PathVariable Long itemId,
                                          HttpSession session,
                                          RedirectAttributes attributes) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuario == null) return "redirect:/login";

        try {
            // Buscar o item para obter o setlistId (necessário para redirect e para verificar ownership)
            Long setlistId = setlistService.buscarSetlistIdPorItem(itemId, usuario.getId());
            setlistService.removerMusicaDoSetlist(setlistId, itemId);
            attributes.addFlashAttribute("mensagem", "Música removida do setlist.");
            return "redirect:/repertorio/visualizar/" + setlistId;
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao remover música: " + e.getMessage());
            return "redirect:/repertorio/setlists";
        }
    }

    // ── Helper ──────────────────────────────────────────────────────
    private String formatarDuracao(int totalSegundos) {
        if (totalSegundos <= 0) return "0 min";
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        if (horas > 0) {
            return horas + "h " + (minutos > 0 ? minutos + "min" : "");
        }
        return minutos + " min";
    }
}
