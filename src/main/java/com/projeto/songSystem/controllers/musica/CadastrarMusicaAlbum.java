package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class CadastrarMusicaAlbum {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/albuns/visualizar/cadastrarmusica/{albumId}")
    public String exibirCadastroMusicaPorAlbum(Model model, @PathVariable Long albumId) {

        AlbumDTO album = albumService.obterAlbumCompleto(albumId);

        MusicaDTO musicaDTO = new MusicaDTO();
        musicaDTO.setAlbumId(albumId);
        musicaDTO.setBandaId(album.getBandaDTO().getBandaId());

        model.addAttribute("album", album);
        model.addAttribute("musicaDTO", musicaDTO);

        return "cadastrarmusicaalbum";
    }

    @PostMapping("/biblioteca/albuns/visualizar/cadastrarmusica")
    public String salvarMusica(@ModelAttribute("musicaDTO") MusicaDTO musicaDTO,
                               RedirectAttributes attributes) {
        try {
            musicaService.cadastrarMusica(musicaDTO);
            attributes.addFlashAttribute("mensagem", "Música cadastrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao cadastrar música: " + e.getMessage());
        }
        return "redirect:/biblioteca/albuns/visualizar/cadastrarmusica/" + musicaDTO.getAlbumId();
    }

}
