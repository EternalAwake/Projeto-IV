package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaCampoHarmonicoDTO;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class TeoriaCampoHarmonicoController {

    @Autowired
    private TeoriaService teoriaService;

    @GetMapping("/teoria/campo-harmonico")
    public String listarCampos(Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        List<TeoriaCampoHarmonicoDTO> todos = teoriaService.listarTodosCamposHarmonicos();

        // Agrupar por tom e contar acordes
        List<ResumoCampoDTO> campos = todos.stream()
                .collect(Collectors.groupingBy(TeoriaCampoHarmonicoDTO::getTom))
                .entrySet().stream()
                .map(entry -> new ResumoCampoDTO(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());

        model.addAttribute("campos", campos);
        return "campo-harmonico";
    }

    static class ResumoCampoDTO {
        private String tom;
        private int quantidadeAcordes;

        public ResumoCampoDTO(String tom, int quantidadeAcordes) {
            this.tom = tom;
            this.quantidadeAcordes = quantidadeAcordes;
        }
        public String getTom() { return tom; }
        public int getQuantidadeAcordes() { return quantidadeAcordes; }
    }
}