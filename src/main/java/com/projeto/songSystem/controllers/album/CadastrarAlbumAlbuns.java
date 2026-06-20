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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class CadastrarAlbumAlbuns {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private BandaService bandaService;

    @GetMapping("/biblioteca/albuns/cadastraralbum")
    public String exibirAlbuns(Model model, HttpSession session) {

        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        AlbumDTO albumDTO = new AlbumDTO();
        model.addAttribute("albumDTO", albumDTO);

        List<BandaModel> bandaModel = bandaService.listarBandas();
        model.addAttribute("bandas", bandaModel);

        return "cadastraralbumalbuns";
    }

    @PostMapping("/biblioteca/albuns/cadastraralbum")
    public String cadastrarAlbum(@ModelAttribute("albumDTO") AlbumDTO albumDTO, RedirectAttributes attributes) {
        try {
            albumService.cadastrarAlbum(albumDTO);
            attributes.addFlashAttribute("mensagem", "Álbum cadastrada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Falha ao cadastrar álbum: " + e.getMessage());
        }
        return "redirect:/biblioteca/albuns/cadastraralbum";
    }

}
