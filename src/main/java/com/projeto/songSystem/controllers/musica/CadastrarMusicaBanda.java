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
public class CadastrarMusicaBanda {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/bandas/visualizar/cadastrarmusica/{bandaId}")
    public String exibirCadastrarMusica(Model model, HttpSession session, @PathVariable Long bandaId) {

        //Sessão
        /*UsuarioModel usuarioDto = (UsuarioModel) session.getAttribute("usuarioDto");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDto", usuarioDto);*/

        // Buscar a banda
        BandaModel banda = bandaService.obterBandaModelPorId(bandaId);

        // Buscar apenas os álbuns da banda selecionada
        List<AlbumModel> albuns = albumService.listarAlbunsPorBanda(bandaId);

        model.addAttribute("banda", banda);
        model.addAttribute("albuns", albuns);

        // Criar DTO e pré-selecionar a banda
        MusicaDTO musicaDTO = new MusicaDTO();
        musicaDTO.setBandaId(bandaId);
        model.addAttribute("musicaDTO", musicaDTO);

        return "cadastrarmusicabanda";
    }

    @PostMapping("/biblioteca/bandas/visualizar/cadastrarmusica")
    public String salvarMusica(@ModelAttribute("musicaDTO") MusicaDTO musicaDTO,
                               RedirectAttributes attributes) {
        try {
            musicaService.cadastrarMusica(musicaDTO);
            attributes.addFlashAttribute("mensagem", "Música cadastrada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao cadastrar música: " + e.getMessage());
        }
        return "redirect:/biblioteca/bandas/visualizar/cadastrarmusica/" + musicaDTO.getBandaId();
    }

}