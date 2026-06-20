package com.projeto.songSystem.controllers.banda;

import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
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

import java.util.List;

@Controller
@RequestMapping()
public class BandasController {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    @GetMapping("/biblioteca/bandas")
    public String exbirBandas(Model model, HttpSession session) {

        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        long totalBandas = bandaService.obterQtdBandas();
        long totalAlbuns = albumService.obterQtdAlbuns();
        long totalMusicas = musicaService.obterQtdMusicas();

        model.addAttribute("totalBandas", totalBandas);
        model.addAttribute("totalAlbuns", totalAlbuns);
        model.addAttribute("totalMusicas", totalMusicas);

        List<BandaModel> listaBandas = bandaService.listarBandas();
        model.addAttribute("listaBandas", listaBandas);

        return "bandas";
    }

    @DeleteMapping("/biblioteca/bandas/excluir/{id}")
    public ResponseEntity<String> excluirBanda(@PathVariable Long id, HttpSession session) {
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada.");
        }
        boolean retorno = bandaService.excluirBanda(id);
        if (retorno) {
            return ResponseEntity.ok("Banda excluída com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir banda.");
    }

}