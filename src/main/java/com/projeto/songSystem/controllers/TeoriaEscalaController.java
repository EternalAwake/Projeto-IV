package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaEscalaDTO;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teoria/escalas")
public class TeoriaEscalaController {

    @Autowired
    private TeoriaService teoriaService;

    @GetMapping
    public String listarEscalas(Model model) {
        List<TeoriaEscalaDTO> escalas = teoriaService.listarTodasEscalas();
        List<String> tiposEscala = teoriaService.listarTiposEscala();

        model.addAttribute("escalas", escalas);
        model.addAttribute("tiposEscala", tiposEscala);
        model.addAttribute("totalEscalas", escalas.size());

        // Estatísticas
        long totalMaiores = escalas.stream().filter(e -> "Maior".equals(e.getTipo())).count();
        long totalMenores = escalas.stream().filter(e -> "Menor".equals(e.getTipo())).count();
        long totalPentatonicas = escalas.stream().filter(e -> "Pentatônica".equals(e.getTipo())).count();

        model.addAttribute("totalMaiores", totalMaiores);
        model.addAttribute("totalMenores", totalMenores);
        model.addAttribute("totalPentatonicas", totalPentatonicas);

        return "escalas";
    }

    @GetMapping("/{id}")
    public String visualizarEscala(@PathVariable Long id, Model model) {
        TeoriaEscalaDTO escala = teoriaService.buscarEscalaPorId(id);
        model.addAttribute("escala", escala);
        return "VisualizarEscala";
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("escala", new TeoriaEscalaDTO());
        model.addAttribute("tipos", List.of("Maior", "Menor Natural", "Menor Harmônica", "Menor Melódica", "Pentatônica", "Blues"));
        model.addAttribute("tons", List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"));
        return "CadastrarEscala";
    }

    @PostMapping("/salvar")
    public String salvarEscala(@ModelAttribute TeoriaEscalaDTO escala,
                               RedirectAttributes attributes) {
        try {
            teoriaService.salvarEscala(escala);
            attributes.addFlashAttribute("mensagem", "Escala " + escala.getNome() + " cadastrada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar escala: " + e.getMessage());
        }
        return "redirect:/teoria/escalas";
    }

    @GetMapping("/editar/{id}")
    public String editarEscala(@PathVariable Long id, Model model) {
        TeoriaEscalaDTO escala = teoriaService.buscarEscalaPorId(id);
        model.addAttribute("escala", escala);
        model.addAttribute("tipos", List.of("Maior", "Menor Natural", "Menor Harmônica", "Menor Melódica", "Pentatônica", "Blues"));
        model.addAttribute("tons", List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"));
        return "AlterarEscala";
    }

    @PostMapping("/atualizar")
    public String atualizarEscala(@ModelAttribute TeoriaEscalaDTO escala,
                                  RedirectAttributes attributes) {
        try {
            teoriaService.atualizarEscala(escala);
            attributes.addFlashAttribute("mensagem", "Escala " + escala.getNome() + " atualizada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao atualizar escala: " + e.getMessage());
        }
        return "redirect:/teoria/escalas";
    }

    @DeleteMapping("/excluir/{id}")
    @ResponseBody
    public ResponseEntity<String> excluirEscala(@PathVariable Long id) {
        try {
            teoriaService.excluirEscala(id);
            return ResponseEntity.ok("Escala excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<TeoriaEscalaDTO> filtrarEscalas(@RequestParam(required = false) String tipo,
                                                @RequestParam(required = false) String tonica,
                                                @RequestParam(required = false) String busca) {
        return teoriaService.filtrarEscalas(tipo, tonica, busca);
    }
}