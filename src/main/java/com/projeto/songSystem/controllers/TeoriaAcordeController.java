package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaAcordeDTO;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teoria/acordes")
public class TeoriaAcordeController {

    @Autowired
    private TeoriaService teoriaService;

    @GetMapping
    public String listarAcordes(Model model) {
        List<TeoriaAcordeDTO> acordes = teoriaService.listarTodosAcordes();
        List<String> tiposAcorde = teoriaService.listarTiposAcorde();

        model.addAttribute("acordes", acordes);
        model.addAttribute("tiposAcorde", tiposAcorde);
        model.addAttribute("totalAcordes", acordes.size());

        // Estatísticas
        long totalMaiores = acordes.stream().filter(a -> "Maior".equals(a.getTipo())).count();
        long totalMenores = acordes.stream().filter(a -> "Menor".equals(a.getTipo())).count();
        long totalDiminutos = acordes.stream().filter(a -> "Diminuto".equals(a.getTipo())).count();
        long totalAumentados = acordes.stream().filter(a -> "Aumentado".equals(a.getTipo())).count();

        model.addAttribute("totalMaiores", totalMaiores);
        model.addAttribute("totalMenores", totalMenores);
        model.addAttribute("totalDiminutos", totalDiminutos);
        model.addAttribute("totalAumentados", totalAumentados);

        return "Acordes";
    }

    @GetMapping("/{id}")
    public String visualizarAcorde(@PathVariable Long id, Model model) {
        TeoriaAcordeDTO acorde = teoriaService.buscarAcordePorId(id);
        model.addAttribute("acorde", acorde);
        return "VisualizarAcorde";
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("acorde", new TeoriaAcordeDTO());
        model.addAttribute("tipos", List.of("Maior", "Menor", "Diminuto", "Aumentado", "Suspenso"));
        model.addAttribute("tons", List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"));
        return "CadastrarAcorde";
    }

    @PostMapping("/salvar")
    public String salvarAcorde(@ModelAttribute TeoriaAcordeDTO acorde,
                               RedirectAttributes attributes) {
        try {
            teoriaService.salvarAcorde(acorde);
            attributes.addFlashAttribute("mensagem", "Acorde " + acorde.getNome() + " cadastrado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar acorde: " + e.getMessage());
        }
        return "redirect:/teoria/acordes";
    }

    @GetMapping("/editar/{id}")
    public String editarAcorde(@PathVariable Long id, Model model) {
        TeoriaAcordeDTO acorde = teoriaService.buscarAcordePorId(id);
        model.addAttribute("acorde", acorde);
        model.addAttribute("tipos", List.of("Maior", "Menor", "Diminuto", "Aumentado", "Suspenso"));
        model.addAttribute("tons", List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"));
        return "AlterarAcorde";
    }

    @PostMapping("/atualizar")
    public String atualizarAcorde(@ModelAttribute TeoriaAcordeDTO acorde,
                                  RedirectAttributes attributes) {
        try {
            teoriaService.atualizarAcorde(acorde);
            attributes.addFlashAttribute("mensagem", "Acorde " + acorde.getNome() + " atualizado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao atualizar acorde: " + e.getMessage());
        }
        return "redirect:/teoria/acordes";
    }

    @DeleteMapping("/excluir/{id}")
    @ResponseBody
    public ResponseEntity<String> excluirAcorde(@PathVariable Long id) {
        try {
            teoriaService.excluirAcorde(id);
            return ResponseEntity.ok("Acorde excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<TeoriaAcordeDTO> filtrarAcordes(@RequestParam(required = false) String tipo,
                                                @RequestParam(required = false) String tonica,
                                                @RequestParam(required = false) String busca) {
        return teoriaService.filtrarAcordes(tipo, tonica, busca);
    }
}