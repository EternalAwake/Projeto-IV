package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.dto.MusicaDTO;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class VisualizarMusicaBiblioteca {

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/visualizarmusica/{id}")
    public String visualizarMusica(Model model, @PathVariable Long id, HttpSession session) {

        // Buscar a música com todos os dados (banda e álbum)
        MusicaDTO musica = musicaService.obterMusicaComDadosCompletos(id);
        model.addAttribute("musica", musica);

        return "VisualizarMusicaBiblioteca";
    }

    @DeleteMapping("/biblioteca/musicas/visualizar/excluir/{id}")
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
