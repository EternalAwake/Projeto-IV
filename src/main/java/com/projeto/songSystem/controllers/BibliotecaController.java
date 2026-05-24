package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class BibliotecaController {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca")
    public String exibirBiblioteca(Model model, HttpSession session) {

        //Sessão
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        long totalBandas = bandaService.obterQtdBandas();
        long totalAlbuns = albumService.obterQtdAlbuns();
        long totalMusicas = musicaService.obterQtdMusicas();

        model.addAttribute("totalBandas", totalBandas);
        model.addAttribute("totalAlbuns", totalAlbuns);
        model.addAttribute("totalMusicas", totalMusicas);

        List<MusicaModel> musicasDestaque = musicaService.obterMusicasEmDestque();
        List<BandaModel> bandasDestaque = bandaService.obterBandasEmDestaque();
        List<AlbumModel> albunsDestaque = albumService.obterAlbunsEmDestaque();

        model.addAttribute("musicasDestaque", musicasDestaque);
        model.addAttribute("bandasDestaque", bandasDestaque);
        model.addAttribute("albunsDestaque", albunsDestaque);

        return "biblioteca";
    }

    @DeleteMapping("/biblioteca/musicas/excluir/{id}")
    public ResponseEntity<String> excluirMusica(@PathVariable Long id) {
        try {
            musicaService.excluirMusica(id);
            return ResponseEntity.ok("Música excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir música: " + e.getMessage());
        }
    }

}
