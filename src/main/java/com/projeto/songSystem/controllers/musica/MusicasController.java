package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class MusicasController {

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @GetMapping("/biblioteca/musicas")
    public String listarMusicas(Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);


        List<MusicaModel> musicas = musicaService.listarMusicas();
        List<BandaModel> todasBandas = bandaService.listarBandas();

        model.addAttribute("musicas", musicas);
        model.addAttribute("todasBandas", todasBandas);
        model.addAttribute("totalMusicas", musicaService.obterQtdMusicas());
        model.addAttribute("totalBandas", bandaService.obterQtdBandas());
        model.addAttribute("totalAlbuns", albumService.obterQtdAlbuns());

        return "musicas";
    }
}
