package com.projeto.songSystem.controllers.album;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class CadastrarAlbumBanda {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @GetMapping("/biblioteca/bandas/visualizar/cadastraralbum/{id}")
    public String exibirAlbuns(Model model, HttpSession session, @PathVariable Long id) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);


        // Buscar a banda para mostrar no título
        BandaDTO banda = bandaService.obterBanda(id);
        model.addAttribute("banda", banda);

        // Criar um novo DTO e já setar o ID da banda
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setBandaId(id);  // Seta apenas o ID

        // Opcional: setar também o bandaDTO para exibição
        albumDTO.setBandaDTO(banda);

        model.addAttribute("albumDTO", albumDTO);

        return "cadastraralbumbanda";
    }

    @PostMapping("/biblioteca/bandas/visualizar/cadastraralbum")
    public String cadastrarAlbum(@ModelAttribute("albumDTO") AlbumDTO albumDTO,
                                 RedirectAttributes attributes) {
        try {
            albumService.cadastrarAlbumBanda(albumDTO);
            attributes.addFlashAttribute("mensagem", "Álbum cadastrado com sucesso!");

            // Redirecionar para a visualização da banda usando o bandaId
            return "redirect:/biblioteca/bandas/visualizar/" + albumDTO.getBandaId();

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao cadastrar álbum: " + e.getMessage());

            // Em caso de erro, volta para o formulário
            return "redirect:/biblioteca/bandas/visualizar/cadastraralbum/" + albumDTO.getBandaId();
        }
    }

}
