package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaGeralDTO;
import com.projeto.songSystem.dto.TeoriaGeralResponseDTO;
import com.projeto.songSystem.services.TeoriaGeralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/teoria/geral")
public class TeoriaGeraisController {

    @Autowired
    private TeoriaGeralService teoriaService;

    // Listar todas as teorias
    @GetMapping
    public String listarTeorias(Model model) {
        List<TeoriaGeralResponseDTO> teorias = teoriaService.buscarTodasAtivas();
        List<String> categorias = teoriaService.buscarTodasCategorias();

        model.addAttribute("teorias", teorias);
        model.addAttribute("categorias", categorias);

        return "TeoriasGerais";
    }

    // Visualizar teoria específica
    @GetMapping("/visualizar/{id}")
    public String visualizarTeoria(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TeoriaGeralResponseDTO teoria = teoriaService.buscarPorId(id);
            model.addAttribute("teoria", teoria);
            return "ViusalizarTeoriaGeral";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Teoria não encontrada!");
            return "redirect:/TeoriasGerais";
        }
    }

    // Formulário para nova teoria
    @GetMapping("/novo")
    public String formularioNovaTeoria(Model model) {
        TeoriaGeralDTO teoriaDTO = new TeoriaGeralDTO();
        teoriaDTO.setAtivo(true);
        teoriaDTO.setOrdemExibicao(0);
        teoriaDTO.setIcone("bi-book");
        teoriaDTO.setCorIcone("primary");
        teoriaDTO.setTipoBadge("primary");

        model.addAttribute("teoriaDTO", teoriaDTO);
        model.addAttribute("acao", "nova");
        model.addAttribute("iconesDisponiveis", getIconesDisponiveis());
        model.addAttribute("coresDisponiveis", getCoresDisponiveis());

        return "CadastrarEditarTeoriaGeral";
    }

    // Salvar nova teoria
    @PostMapping("/salvar")
    public String salvarTeoria(@ModelAttribute("teoriaDTO") TeoriaGeralDTO teoriaDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("acao", "nova");
            model.addAttribute("iconesDisponiveis", getIconesDisponiveis());
            model.addAttribute("coresDisponiveis", getCoresDisponiveis());
            return "CadastrarEditarTeoriaGeral";
        }

        try {
            TeoriaGeralResponseDTO saved = teoriaService.criarTeoria(teoriaDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Teoria \"" + saved.getTitulo() + "\" criada com sucesso!");
            return "redirect:/TeoriasGerais";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
            model.addAttribute("acao", "nova");
            model.addAttribute("iconesDisponiveis", getIconesDisponiveis());
            model.addAttribute("coresDisponiveis", getCoresDisponiveis());
            return "CadastrarEditarTeoriaGeral";
        }
    }

    // Formulário para editar teoria
    @GetMapping("/editar/{id}")
    public String formularioEditarTeoria(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            TeoriaGeralDTO teoriaDTO = teoriaService.buscarParaEdicao(id);
            model.addAttribute("teoriaDTO", teoriaDTO);
            model.addAttribute("acao", "editar");
            model.addAttribute("iconesDisponiveis", getIconesDisponiveis());
            model.addAttribute("coresDisponiveis", getCoresDisponiveis());
            return "CadastrarEditarTeoriaGeral";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Teoria não encontrada para edição!");
            return "redirect:/TeoriasGearis";
        }
    }

    // Atualizar teoria
    @PostMapping("/atualizar/{id}")
    public String atualizarTeoria(@PathVariable Long id,
                                  @ModelAttribute("teoriaDTO") TeoriaGeralDTO teoriaDTO,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("acao", "editar");
            model.addAttribute("iconesDisponiveis", getIconesDisponiveis());
            model.addAttribute("coresDisponiveis", getCoresDisponiveis());
            return "CadastrarEditarTeoriaGeral";
        }

        try {
            TeoriaGeralResponseDTO updated = teoriaService.atualizarTeoria(id, teoriaDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Teoria \"" + updated.getTitulo() + "\" atualizada com sucesso!");
            return "redirect:/TeoriasGearis";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/CadastrarEditarTeoriaGeral/" + id;
        }
    }

    // Excluir teoria
    @PostMapping("/excluir/{id}")
    public String excluirTeoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teoriaService.excluirTeoria(id);
            redirectAttributes.addFlashAttribute("sucesso", "Teoria excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/teoria/geral";
    }

    // Métodos auxiliares
    private List<String> getIconesDisponiveis() {
        return Arrays.asList(
                "bi-circle", "bi-arrow-left-right", "bi-file-music", "bi-shield",
                "bi-grid-3x3-gap-fill", "bi-music-note-list", "bi-diagram-3",
                "bi-pentagon", "bi-star", "bi-heart", "bi-music-note-beamed",
                "bi-book", "bi-lightbulb", "bi-graph-up", "bi-puzzle"
        );
    }

    private List<CorDTO> getCoresDisponiveis() {
        return Arrays.asList(
                new CorDTO("primary", "#0d6efd"),
                new CorDTO("success", "#198754"),
                new CorDTO("danger", "#dc3545"),
                new CorDTO("warning", "#ffc107"),
                new CorDTO("info", "#0dcaf0"),
                new CorDTO("dark", "#212529"),
                new CorDTO("secondary", "#6c757d")
        );
    }

    // Classe interna para cores
    class CorDTO {
        private String nome;
        private String valor;

        public CorDTO(String nome, String valor) {
            this.nome = nome;
            this.valor = valor;
        }

        public String getNome() { return nome; }
        public String getValor() { return valor; }
    }
}