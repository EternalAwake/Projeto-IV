package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaCampoHarmonicoDTO;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class VisualizarCampoHarmonico {

    @Autowired
    private TeoriaService teoriaService;

    @GetMapping("/teoria/campo-harmonico/{tom}")
    public String visualizarCampo(@PathVariable String tom, Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        List<TeoriaCampoHarmonicoDTO> campos = teoriaService.listarCampoHarmonicoPorTom(tom);
        model.addAttribute("tom", tom);
        model.addAttribute("campos", campos);
        return "VisualizarCampoHarmonico";
    }

    @PostMapping("/teoria/campo-harmonico/salvar")
    public String salvarAcorde(
            @RequestParam String tom,
            @RequestParam Integer grau,
            @RequestParam String acorde,
            @RequestParam String tipo,
            @RequestParam String funcao,
            @RequestParam(required = false) String notas,
            HttpSession session,
            RedirectAttributes attributes) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        try {
            TeoriaCampoHarmonicoDTO dto = new TeoriaCampoHarmonicoDTO();
            dto.setTom(tom);
            dto.setGrau(grau);
            dto.setAcorde(acorde);
            dto.setTipo(tipo);
            dto.setFuncao(funcao);
            dto.setNotas(notas);

            teoriaService.salvarCampoHarmonico(dto);
            attributes.addFlashAttribute("mensagem", "Acorde do " + getGrauRomano(grau) + " grau adicionado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar acorde: " + e.getMessage());
        }
        return "redirect:/teoria/campo-harmonico/" + tom;
    }

    @PostMapping("/teoria/campo-harmonico/atualizar")
    public String atualizarAcorde(
            @RequestParam Long id,
            @RequestParam String tom,
            @RequestParam Integer grau,
            @RequestParam String acorde,
            @RequestParam String tipo,
            @RequestParam String funcao,
            @RequestParam(required = false) String notas,
            HttpSession session,
            RedirectAttributes attributes) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        try {
            TeoriaCampoHarmonicoDTO dto = new TeoriaCampoHarmonicoDTO();
            dto.setId(id);
            dto.setTom(tom);
            dto.setGrau(grau);
            dto.setAcorde(acorde);
            dto.setTipo(tipo);
            dto.setFuncao(funcao);
            dto.setNotas(notas);

            teoriaService.atualizarCampoHarmonico(dto);
            attributes.addFlashAttribute("mensagem", "Acordo do " + getGrauRomano(grau) + " grau atualizado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao atualizar acorde: " + e.getMessage());
        }
        return "redirect:/teoria/campo-harmonico/" + tom;
    }

    @DeleteMapping("/teoria/campo-harmonico/excluir-acorde/{id}")
    @ResponseBody
    public ResponseEntity<String> excluirAcorde(@PathVariable Long id, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada.");
        try {
            TeoriaCampoHarmonicoDTO acorde = teoriaService.buscarCampoHarmonicoPorId(id);
            String tom = acorde.getTom();
            teoriaService.excluirCampoHarmonico(id);
            return ResponseEntity.ok("Acorde excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/teoria/campo-harmonico/excluir-campo/{tom}")
    @ResponseBody
    public ResponseEntity<String> excluirCampoCompleto(@PathVariable String tom, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada.");
        try {
            teoriaService.excluirCampoPorTom(tom);
            return ResponseEntity.ok("Campo harmônico de " + tom + " excluído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private String getGrauRomano(Integer grau) {
        switch (grau) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            default: return String.valueOf(grau);
        }
    }
}