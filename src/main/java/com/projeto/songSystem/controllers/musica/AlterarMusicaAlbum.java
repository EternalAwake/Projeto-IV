package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class AlterarMusicaAlbum {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/albuns/visualizar/alterarmusica/{id}")
    public String exibirCadastrarMusica(Model model, HttpSession session, @PathVariable Long id) {

        //Sessão
        /*UsuarioModel usuarioDto = (UsuarioModel) session.getAttribute("usuarioDto");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDto", usuarioDto);*/

        MusicaDTO musica = musicaService.obterMusicaComDadosCompletos(id);

        List<AlbumModel> albuns = albumService.listarAlbuns();
        List<BandaModel> bandas = bandaService.listarBandas();

        model.addAttribute("musica", musica);
        model.addAttribute("albuns", albuns);
        model.addAttribute("bandas", bandas);

        return "AlterarMusicaAlbum";
    }

    @PostMapping("/biblioteca/albuns/visualizar/alterarmusica")
    public String alterarMusica(@ModelAttribute("musica") MusicaDTO musicaDTO,
                                RedirectAttributes attributes) {
        try {
            musicaService.alterarMusica(musicaDTO);
            attributes.addFlashAttribute("mensagem", "Música alterada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao alterar música: " + e.getMessage());
        }
        return "redirect:/biblioteca/albuns/visualizar/alterarmusica/" + musicaDTO.getMusicaId();
    }

}
