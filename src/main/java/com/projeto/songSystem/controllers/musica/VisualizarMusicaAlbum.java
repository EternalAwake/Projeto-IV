package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class VisualizarMusicaAlbum {

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/albuns/visualizar/visualizarmusica/{id}")
    public String exibirMusica(Model model, HttpSession session, @PathVariable Long id) {

        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        // Buscar a música com todos os dados (banda e álbum)
        MusicaDTO musica = musicaService.obterMusicaComDadosCompletos(id);
        model.addAttribute("musica", musica);

        return "VisualizarMusicaAlbum";
    }

}
