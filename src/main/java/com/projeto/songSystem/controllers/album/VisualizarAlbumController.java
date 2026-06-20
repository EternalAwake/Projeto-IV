package com.projeto.songSystem.controllers.album;

import com.projeto.songSystem.dto.AlbumDTO;
import com.projeto.songSystem.services.AlbumService;
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

@Controller
@RequestMapping()
public class VisualizarAlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping("/biblioteca/albuns/visualizar/{id}")
    public String exibirAlbum(Model model, HttpSession session, @PathVariable Long id) {

        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        AlbumDTO album = albumService.obterAlbumCompleto(id);
        model.addAttribute("album", album);

        return "VisualizarAlbum";
    }

    @DeleteMapping("/biblioteca/albuns/visualizar/excluir/{id}")
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