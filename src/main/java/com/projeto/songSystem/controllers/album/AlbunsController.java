package com.projeto.songSystem.controllers.album;

import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class AlbunsController {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/albuns")
    public String listarAlbuns(Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);


        model.addAttribute("albuns", albumService.listarAlbuns());
        model.addAttribute("totalBandas", bandaService.obterQtdBandas());
        model.addAttribute("totalMusicas", musicaService.obterQtdMusicas());
        model.addAttribute("totalAlbuns", albumService.obterQtdAlbuns());

        return "albuns";

    }

    @DeleteMapping("/biblioteca/albuns/excluir/{id}")
    public ResponseEntity<String> excluirAlbum(@PathVariable Long id, HttpSession session) {
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada.");
        }
        boolean retorno = albumService.excluirAlbum(id);
        if (retorno) {
            return ResponseEntity.ok("Álbum excluído com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir álbum.");
    }
}