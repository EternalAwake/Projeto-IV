package com.projeto.songSystem.controllers.album;

import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/biblioteca/albuns")
public class AlbunsController {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping
    public String listarAlbuns(
            @RequestParam(required = false) String bandaId,
            @RequestParam(required = false) String ano,
            @RequestParam(required = false) String busca,
            Model model
    ) {
        model.addAttribute("albuns", albumService.filtrarAlbuns(bandaId, ano, busca));
        model.addAttribute("todasBandas", bandaService.listarBandas());
        model.addAttribute("anosDisponiveis", albumService.obterAnosDisponiveis(bandaId));
        model.addAttribute("totalBandas", bandaService.obterQtdBandas());
        model.addAttribute("totalMusicas", musicaService.obterQtdMusicas());
        model.addAttribute("totalAlbuns", albumService.obterQtdAlbuns());

        // Mantém os filtros selecionados após o submit
        model.addAttribute("bandaIdSelecionada", bandaId);
        model.addAttribute("anoSelecionado", ano);
        model.addAttribute("buscaAtual", busca);

        return "albuns";
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirAlbum(@PathVariable Long id) {

        boolean retorno = albumService.excluirAlbum(id);

        if(retorno) {
            return ResponseEntity.ok("Álbum excluída com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir álbum.");
    }
}