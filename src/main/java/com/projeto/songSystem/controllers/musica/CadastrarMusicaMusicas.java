package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class CadastrarMusicaMusicas {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/musicas/cadastrarmusica")
    public String exibirCadastrarMusica(Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);


        // Buscar listas para os selects
        List<BandaModel> bandas = bandaService.listarBandas();
        List<AlbumModel> albuns = albumService.listarAlbuns();

        model.addAttribute("bandas", bandas);
        model.addAttribute("albuns", albuns);
        model.addAttribute("musicaDTO", new MusicaDTO());

        return "CadastrarMusicaMusicas";
    }

    @PostMapping("/biblioteca/musicas/cadastrarmusica")
    public String salvarMusica(@ModelAttribute("musicaDTO") MusicaDTO musicaDTO,
                               RedirectAttributes attributes) {
        try {
            musicaService.cadastrarMusica(musicaDTO);
            attributes.addFlashAttribute("mensagem", "Música cadastrada com sucesso!");
            return "redirect:/biblioteca/musicas/cadastrarmusica";

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao cadastrar música: " + e.getMessage());
            return "redirect:/biblioteca/musicas/cadastrarmusica";
        }
    }
}