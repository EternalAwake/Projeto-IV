package com.projeto.songSystem.controllers.album;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class AlterarAlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private BandaService bandaService;

    @GetMapping("/biblioteca/albuns/visualizar/alteraralbum/{id}")
    public String exibirAlterarAlbum(Model model, HttpSession session, @PathVariable Long id) {

        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        AlbumDTO albumDTO = albumService.obterAlbumCompleto(id);
        model.addAttribute("albumDTO", albumDTO);

        List<BandaModel> bandaModel = bandaService.listarBandasBasicas();
        model.addAttribute("bandas", bandaModel);

        return "AlterarAlbum";
    }

    @PostMapping("/biblioteca/albuns/visualizar/alteraralbum")
    public String alterarAlbum(@ModelAttribute("albumDTO") AlbumDTO albumDTO,
                               RedirectAttributes attributes) {
        try {
            albumService.alterarAlbum(albumDTO);
            attributes.addFlashAttribute("mensagem", "Álbum alterado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao alterar álbum: " + e.getMessage());
        }
        return "redirect:/biblioteca/albuns/visualizar/alteraralbum/" + albumDTO.getAlbumId();
    }

}