package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.RepertorioEstatisticasDTO;
import com.projeto.songSystem.dto.RepertorioItemDTO;
import com.projeto.songSystem.dto.TecnicaDTO;
import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.models.RepertorioItemModel;
import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import com.projeto.songSystem.repositories.MusicaRepository;
import com.projeto.songSystem.repositories.RepertorioItemRepository;
import com.projeto.songSystem.services.MusicaService;
import com.projeto.songSystem.services.RepertorioService;
import com.projeto.songSystem.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/repertorio")
public class RepertorioController {

    @Autowired
    private RepertorioService repertorioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private RepertorioItemRepository repertorioItemRepository;

    /**
     * Página principal do repertório
     */
    @GetMapping
    public String paginaRepertorio(Model model, HttpSession session) {
        //Sessão
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        // Dados do repertório
        List<RepertorioItemDTO> repertorio = repertorioService.listarRepertorio(usuarioDto.getId());
        RepertorioEstatisticasDTO estatisticas = repertorioService.obterEstatisticas(usuarioDto.getId());

        model.addAttribute("repertorio", repertorio);
        model.addAttribute("totalRepertorio", estatisticas.getTotal());
        model.addAttribute("estudando", estatisticas.getEstudando());
        model.addAttribute("aprendida", estatisticas.getAprendida());
        model.addAttribute("dominada", estatisticas.getDominada());
        model.addAttribute("praticadasHoje", estatisticas.getPraticadasHoje());

        // Enums para os selects
        model.addAttribute("statusList", StatusRepertorio.values());
        model.addAttribute("dificuldadeList", Dificuldade.values());

        return "Repertorio";
    }

    /**
     * Adiciona música ao repertório (via requisição AJAX)
     */
    @PostMapping("/adicionar")
    @ResponseBody
    public Object adicionarMusica(@RequestParam Long musicaId,
                                  @RequestParam(required = false) Dificuldade dificuldade,
                                  HttpSession session) {
        try {
            Long usuarioId = getUsuarioLogado(session);
            RepertorioItemDTO item = repertorioService.adicionarAoRepertorio(usuarioId, musicaId, dificuldade);
            return new SuccessResponse(true, "Música adicionada ao repertório!", item);
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Atualiza status da música no repertório
     */
    @PutMapping("/{musicaId}/status")
    @ResponseBody
    public Object atualizarStatus(@PathVariable Long musicaId,
                                  @RequestParam StatusRepertorio status,
                                  HttpSession session) {
        try {
            Long usuarioId = getUsuarioLogado(session);
            RepertorioItemDTO item = repertorioService.atualizarStatus(usuarioId, musicaId, status);
            return new SuccessResponse(true, "Status atualizado!", item);
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Atualiza dificuldade da música no repertório
     */
    @PutMapping("/{musicaId}/dificuldade")
    @ResponseBody
    public Object atualizarDificuldade(@PathVariable Long musicaId,
                                       @RequestParam Dificuldade dificuldade,
                                       HttpSession session) {
        try {
            Long usuarioId = getUsuarioLogado(session);
            RepertorioItemDTO item = repertorioService.atualizarDificuldade(usuarioId, musicaId, dificuldade);
            return new SuccessResponse(true, "Dificuldade atualizada!", item);
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Registra prática da música
     */
    @PostMapping("/{musicaId}/praticar")
    @ResponseBody
    public Object registrarPratica(@PathVariable Long musicaId,
                                   HttpSession session) {
        try {
            Long usuarioId = getUsuarioLogado(session);
            RepertorioItemDTO item = repertorioService.registrarPratica(usuarioId, musicaId);
            return new SuccessResponse(true, "Prática registrada!", item);
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Remove música do repertório
     */
    @DeleteMapping("/{musicaId}")
    @ResponseBody
    public Object removerMusica(@PathVariable Long musicaId,
                                HttpSession session) {
        try {
            Long usuarioId = getUsuarioLogado(session);
            repertorioService.removerDoRepertorio(usuarioId, musicaId);
            return new SuccessResponse(true, "Música removida do repertório!");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Página de estatísticas do repertório
     */
    @GetMapping("/estatisticas")
    public String estatisticas(Model model, HttpSession session) {
        Long usuarioId = getUsuarioLogado(session);
        RepertorioEstatisticasDTO estatisticas = repertorioService.obterEstatisticas(usuarioId);

        model.addAttribute("estatisticas", estatisticas);
        return "repertorioEstatisticas";
    }

    @GetMapping("/api/musicas/disponiveis")
    @ResponseBody
    public List<Map<String, Object>> getMusicasDisponiveis(@RequestParam(required = false, defaultValue = "") String busca,
                                                           HttpSession session) {

        // 1. Verificar usuário na sessão
        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuarioDTO");

        if (usuarioLogado == null) {
            return new ArrayList<>();
        }

        Long usuarioId = usuarioLogado.getId();

        // 2. Buscar todas as músicas
        List<MusicaModel> todasMusicas = musicaRepository.findAll();

        // 3. Buscar músicas no repertório do usuário
        List<RepertorioItemModel> repertorioItens = repertorioItemRepository.findByUsuarioId(usuarioId);

        Set<Long> idsNoRepertorio = repertorioItens.stream()
                .map(item -> item.getMusica().getMusicaId())
                .collect(Collectors.toSet());

        // 4. Filtrar músicas disponíveis
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (MusicaModel m : todasMusicas) {

            if (idsNoRepertorio.contains(m.getMusicaId())) {
                continue;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("musicaId", m.getMusicaId());
            map.put("musicaNome", m.getMusicaNome());
            map.put("musicaTom", m.getMusicaTom() != null ? m.getMusicaTom() : "");
            map.put("bandaNome", m.getBanda() != null ? m.getBanda().getBandaNome() : "Artista desconhecido");
            resultado.add(map);
        }

        return resultado;
    }

    @PostMapping("/adicionar-multiplas")
    @ResponseBody
    public Map<String, Object> adicionarMultiplasMusicas(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long usuarioId = getUsuarioLogado(session);
            List<Integer> musicasIdsList = (List<Integer>) payload.get("musicasIds");
            String dificuldadeStr = (String) payload.get("dificuldade");

            Dificuldade dificuldade = Dificuldade.valueOf(dificuldadeStr);

            int adicionadas = 0;
            for (Integer id : musicasIdsList) {
                Long musicaId = id.longValue();
                try {
                    repertorioService.adicionarAoRepertorio(usuarioId, musicaId, dificuldade);
                    adicionadas++;
                } catch (Exception e) {
                    System.out.println("Erro ao adicionar música " + musicaId + ": " + e.getMessage());
                }
            }

            response.put("success", true);
            response.put("adicionadas", adicionadas);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<RepertorioItemDTO> filtrarItensRepertorio(@RequestParam(required = false) StatusRepertorio status,
                                                          @RequestParam(required = false) Dificuldade dificuldade,
                                                          @RequestParam(required = false) String musicaTom,
                                                          @RequestParam(required = false) String busca) {
        return repertorioService.filtrarItensRepertorio(status, dificuldade, musicaTom, busca);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Long getUsuarioLogado(HttpSession session) {
        // TODO: Implementar com a sessão real do usuário
        // UsuarioModel usuario = (UsuarioModel) session.getAttribute("usuario");
        // return usuario.getId();

        // Temporário: retornar um ID fixo para teste
        return 1L;
    }

    // Classes para resposta JSON
    static class SuccessResponse {
        private boolean success;
        private String message;
        private Object data;

        public SuccessResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public SuccessResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }

    static class ErrorResponse {
        private boolean success;
        private String error;

        public ErrorResponse(boolean success, String error) {
            this.success = success;
            this.error = error;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}