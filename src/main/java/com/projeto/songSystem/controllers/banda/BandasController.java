package com.projeto.songSystem.controllers.banda;

import com.projeto.songSystem.dto.FiltroCustomizadoDTO;
import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.services.AlbumService;
import com.projeto.songSystem.services.BandaService;
import com.projeto.songSystem.services.MusicaService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping()
public class BandasController {

    @Autowired
    private BandaService bandaService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicaService musicaService;

    private static final Pattern PADRAO_CAMPO = Pattern.compile("^campo_(\\d+)$");

    @GetMapping("/biblioteca/bandas")
    public String exbirBandas(Model model, HttpSession session, HttpServletRequest request) {
        long totalBandas = bandaService.obterQtdBandas();
        long totalAlbuns = albumService.obterQtdAlbuns();
        long totalMusicas = musicaService.obterQtdMusicas();
        model.addAttribute("totalBandas", totalBandas);
        model.addAttribute("totalAlbuns", totalAlbuns);
        model.addAttribute("totalMusicas", totalMusicas);

        List<FiltroCustomizadoDTO> filtros = extrairFiltros(request);
        List<BandaModel> listaBandas = bandaService.filtrarBandas(filtros);
        model.addAttribute("listaBandas", listaBandas);

        model.addAttribute("filtros", filtros);

        return "bandas";
    }

    @DeleteMapping("/biblioteca/bandas/excluir/{id}")
    public ResponseEntity<String> excluirBanda(@PathVariable Long id) {

        boolean retorno = bandaService.excluirBanda(id);

        if(retorno) {
            return ResponseEntity.ok("Banda excluída com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir banda.");
    }

    private List<FiltroCustomizadoDTO> extrairFiltros(HttpServletRequest request) {
        List<FiltroCustomizadoDTO> filtros = new ArrayList<>();

        for (String nomeParam : request.getParameterMap().keySet()) {
            Matcher matcher = PADRAO_CAMPO.matcher(nomeParam);
            if (!matcher.matches()) continue;

            String indice = matcher.group(1);
            String campo = request.getParameter("campo_" + indice);
            String operador = request.getParameter("operador_" + indice);
            String valor = request.getParameter("valor_" + indice);

            if (campo != null && operador != null && valor != null && !valor.isBlank()) {
                filtros.add(new FiltroCustomizadoDTO(campo, operador, valor));
            }
        }

        return filtros;
    }

}