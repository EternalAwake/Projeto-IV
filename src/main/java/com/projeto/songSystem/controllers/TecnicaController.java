package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TecnicaDTO;
import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.services.TecnicaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tecnicas")
public class TecnicaController {

    @Autowired
    private TecnicaService tecnicaService;

    @GetMapping
    public String listarTecnicas(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        model.addAttribute("usuarioDTO", usuarioDto);

        List<TecnicaDTO> tecnicas = tecnicaService.listarTodasTecnicas();
        List<String> categorias = tecnicaService.listarCategorias();
        List<String> niveis = tecnicaService.listarNiveis();

        long totalIniciante = tecnicas.stream()
                .filter(t -> t.getNivel() != null && t.getNivel().name().equals("INICIANTE"))
                .count();
        long totalInterAvancado = tecnicas.size() - totalIniciante;

        model.addAttribute("tecnicas", tecnicas);
        model.addAttribute("categorias", categorias);
        model.addAttribute("niveis", niveis);
        model.addAttribute("totalTecnicas", tecnicas.size());
        model.addAttribute("totalIniciante", totalIniciante);
        model.addAttribute("totalInterAvancado", totalInterAvancado);

        return "Tecnicas";
    }

    @GetMapping("/{id}")
    public String visualizarTecnica(@PathVariable Long id, Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        model.addAttribute("usuarioDTO", usuarioDto);

        TecnicaDTO tecnica = tecnicaService.buscarTecnicaPorId(id);
        model.addAttribute("tecnica", tecnica);
        return "VisualizarTecnica";
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        model.addAttribute("usuarioDTO", usuarioDto);

        model.addAttribute("tecnica", new TecnicaDTO());
        model.addAttribute("categorias", tecnicaService.listarCategorias());
        model.addAttribute("niveis", tecnicaService.listarNiveis());
        return "CadastrarTecnica";
    }

    // POST /salvar — o SecurityConfig já garante sessão ativa, mas verificamos
    // aqui também para devolver erro amigável caso o token tenha expirado entre
    // o GET do formulário e o submit.
    @PostMapping("/salvar")
    public String salvarTecnica(@ModelAttribute TecnicaDTO tecnica,
                                HttpSession session,
                                RedirectAttributes attributes) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        try {
            tecnicaService.salvarTecnica(tecnica);
            attributes.addFlashAttribute("mensagem", "Técnica \"" + tecnica.getNome() + "\" cadastrada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar técnica: " + e.getMessage());
        }
        // Corrigido: /tecnicas (minúsculo) corresponde ao @RequestMapping e à rota real
        return "redirect:/tecnicas";
    }

    @GetMapping("/editar/{id}")
    public String editarTecnica(@PathVariable Long id, Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        model.addAttribute("usuarioDTO", usuarioDto);

        TecnicaDTO tecnica = tecnicaService.buscarTecnicaPorId(id);
        model.addAttribute("tecnica", tecnica);
        model.addAttribute("categorias", tecnicaService.listarCategorias());
        model.addAttribute("niveis", tecnicaService.listarNiveis());
        return "AlterarTecnica";
    }

    @PostMapping("/atualizar")
    public String atualizarTecnica(@ModelAttribute TecnicaDTO tecnica,
                                   HttpSession session,
                                   RedirectAttributes attributes) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        try {
            tecnicaService.atualizarTecnica(tecnica);
            attributes.addFlashAttribute("mensagem", "Técnica \"" + tecnica.getNome() + "\" atualizada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao atualizar técnica: " + e.getMessage());
        }
        // Corrigido: /tecnicas (minúsculo)
        return "redirect:/tecnicas";
    }

    @DeleteMapping("/excluir/{id}")
    @ResponseBody
    public ResponseEntity<String> excluirTecnica(@PathVariable Long id, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada.");
        }
        try {
            tecnicaService.excluirTecnica(id);
            return ResponseEntity.ok("Técnica excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<TecnicaDTO> filtrarTecnicas(@RequestParam(required = false) String categoria,
                                            @RequestParam(required = false) String nivel,
                                            @RequestParam(required = false) String busca) {
        return tecnicaService.filtrarTecnicas(categoria, nivel, busca);
    }
}
