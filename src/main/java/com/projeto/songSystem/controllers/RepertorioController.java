package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.RepertorioEstatisticasDTO;
import com.projeto.songSystem.dto.RepertorioItemDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // ==================== HELPERS DE SESSÃO ====================

    /**
     * Obtém o ID do usuário logado a partir da sessão.
     * Lança IllegalStateException se não estiver autenticado
     * (o SecurityConfig já bloqueia antes, mas é uma segunda defesa).
     */
    private Long getUsuarioId(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDTO == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return usuarioDTO.getId();
    }

    private UsuarioDTO getUsuarioDTO(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDTO == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return usuarioDTO;
    }

    // ==================== ENDPOINTS ====================

    @GetMapping
    public String paginaRepertorio(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        model.addAttribute("usuarioDTO", usuarioDto);

        Long usuarioId = usuarioDto.getId();
        List<RepertorioItemDTO> repertorio = repertorioService.listarRepertorio(usuarioId);
        RepertorioEstatisticasDTO estatisticas = repertorioService.obterEstatisticas(usuarioId);

        model.addAttribute("repertorio", repertorio);
        model.addAttribute("totalRepertorio", estatisticas.getTotal());
        model.addAttribute("estudando", estatisticas.getEstudando());
        model.addAttribute("aprendida", estatisticas.getAprendida());
        model.addAttribute("dominada", estatisticas.getDominada());
        model.addAttribute("praticadasHoje", estatisticas.getPraticadasHoje());
        model.addAttribute("statusList", StatusRepertorio.values());
        model.addAttribute("dificuldadeList", Dificuldade.values());

        return "repertorio";
    }

    @PostMapping("/adicionar")
    @ResponseBody
    public Object adicionarMusica(@RequestParam Long musicaId,
                                   @RequestParam(required = false) Dificuldade dificuldade,
                                   HttpSession session) {
        try {
            Long usuarioId = getUsuarioId(session);
            RepertorioItemDTO item = repertorioService.adicionarAoRepertorio(usuarioId, musicaId, dificuldade);
            return new SuccessResponse(true, "Música adicionada ao repertório!", item);
        } catch (IllegalStateException e) {
            return new ErrorResponse(false, "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    @PutMapping("/{musicaId}/status")
    @ResponseBody
    public Object atualizarStatus(@PathVariable Long musicaId,
                                   @RequestParam StatusRepertorio status,
                                   HttpSession session) {
        try {
            Long usuarioId = getUsuarioId(session);
            // Garante que o item pertence ao usuário logado
            RepertorioItemDTO item = repertorioService.atualizarStatus(usuarioId, musicaId, status);
            return new SuccessResponse(true, "Status atualizado!", item);
        } catch (IllegalStateException e) {
            return new ErrorResponse(false, "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    @PutMapping("/{musicaId}/dificuldade")
    @ResponseBody
    public Object atualizarDificuldade(@PathVariable Long musicaId,
                                        @RequestParam Dificuldade dificuldade,
                                        HttpSession session) {
        try {
            Long usuarioId = getUsuarioId(session);
            RepertorioItemDTO item = repertorioService.atualizarDificuldade(usuarioId, musicaId, dificuldade);
            return new SuccessResponse(true, "Dificuldade atualizada!", item);
        } catch (IllegalStateException e) {
            return new ErrorResponse(false, "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    @PostMapping("/{musicaId}/praticar")
    @ResponseBody
    public Object registrarPratica(@PathVariable Long musicaId,
                                    HttpSession session) {
        try {
            Long usuarioId = getUsuarioId(session);
            RepertorioItemDTO item = repertorioService.registrarPratica(usuarioId, musicaId);
            return new SuccessResponse(true, "Prática registrada!", item);
        } catch (IllegalStateException e) {
            return new ErrorResponse(false, "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    /**
     * Remove música do repertório — verifica que o item pertence ao usuário logado
     * antes de excluir (sem essa verificação, qualquer usuário poderia excluir
     * itens de outro usuário passando um ID arbitrário).
     */
    @DeleteMapping("/{musicaId}")
    @ResponseBody
    public Object removerMusica(@PathVariable Long musicaId,
                                 HttpSession session) {
        try {
            Long usuarioId = getUsuarioId(session);
            // removerDoRepertorio já filtra por usuarioId + musicaId
            repertorioService.removerDoRepertorio(usuarioId, musicaId);
            return new SuccessResponse(true, "Música removida do repertório!");
        } catch (IllegalStateException e) {
            return new ErrorResponse(false, "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            return new ErrorResponse(false, e.getMessage());
        }
    }

    @GetMapping("/estatisticas")
    public String estatisticas(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        RepertorioEstatisticasDTO estatisticas = repertorioService.obterEstatisticas(usuarioDto.getId());
        model.addAttribute("estatisticas", estatisticas);
        return "repertorioEstatisticas";
    }

    @GetMapping("/api/musicas/disponiveis")
    @ResponseBody
    public List<Map<String, Object>> getMusicasDisponiveis(
            @RequestParam(required = false, defaultValue = "") String busca,
            HttpSession session) {

        UsuarioDTO usuarioLogado = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioLogado == null) {
            return new ArrayList<>();
        }

        Long usuarioId = usuarioLogado.getId();

        List<MusicaModel> todasMusicas = musicaRepository.findAll();
        List<RepertorioItemModel> repertorioItens = repertorioItemRepository.findByUsuarioId(usuarioId);

        Set<Long> idsNoRepertorio = repertorioItens.stream()
                .map(item -> item.getMusica().getMusicaId())
                .collect(Collectors.toSet());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (MusicaModel m : todasMusicas) {
            if (idsNoRepertorio.contains(m.getMusicaId())) continue;
            if (!busca.isEmpty() && !m.getMusicaNome().toLowerCase().contains(busca.toLowerCase())) continue;

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
    public Map<String, Object> adicionarMultiplasMusicas(
            @RequestBody Map<String, Object> payload,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Long usuarioId = getUsuarioId(session);
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
                    // Música já no repertório ou não encontrada — ignorar e continuar
                }
            }

            response.put("success", true);
            response.put("adicionadas", adicionadas);

        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("error", "Sessão expirada. Faça login novamente.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    // ==================== INNER CLASSES (resposta JSON) ====================

    static class SuccessResponse {
        private boolean success;
        private String message;
        private Object data;

        public SuccessResponse(boolean success, String message) { this.success = success; this.message = message; }
        public SuccessResponse(boolean success, String message, Object data) { this.success = success; this.message = message; this.data = data; }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

    static class ErrorResponse {
        private boolean success;
        private String error;

        public ErrorResponse(boolean success, String error) { this.success = success; this.error = error; }

        public boolean isSuccess() { return success; }
        public String getError() { return error; }
    }
}
