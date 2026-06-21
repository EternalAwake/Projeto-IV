package com.projeto.songSystem.controllers.musica;

import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/biblioteca/musicas")
public class MusicasController {

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public String listarMusicas(
            @RequestParam(required = false) String bandaId,
            @RequestParam(required = false) String tom,
            @RequestParam(required = false) String busca,
            Model model
    ) {
        List<MusicaModel> musicas = musicaService.filtrarMusicas(bandaId, tom, busca);
        List<BandaModel> todasBandas = bandaService.listarBandas();

        model.addAttribute("musicas", musicas);
        model.addAttribute("todasBandas", todasBandas);
        model.addAttribute("totalMusicas", musicaService.obterQtdMusicas());
        model.addAttribute("totalBandas", bandaService.obterQtdBandas());
        model.addAttribute("totalAlbuns", albumService.obterQtdAlbuns());

        model.addAttribute("bandaIdSelecionada", bandaId);
        model.addAttribute("tomSelecionado", tom);
        model.addAttribute("buscaAtual", busca);

        return "musicas";
    }}
