package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.TeoriaCampoHarmonicoDTO;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import com.projeto.songSystem.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class CadastrarCampoHarmonico {

    @Autowired
    private TeoriaService teoriaService;

    @GetMapping("/teoria/campo-harmonico/cadastrar")
    public String exibirFormulario(Model model, HttpSession session) {
        // Sessão
        com.projeto.songSystem.dto.UsuarioDTO usuarioDto =
                (com.projeto.songSystem.dto.UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        List<String> tons = getTons();
        model.addAttribute("tons", tons);
        model.addAttribute("campo", new TeoriaCampoHarmonicoDTO());
        return "CadastrarCampoHarmonico";
    }

    @PostMapping("/teoria/campo-harmonico/salvar-campo-completo")
    public String salvarCampoCompleto(
            @RequestParam String tom,
            @RequestParam String acorde1, @RequestParam String tipo1, @RequestParam String funcao1, @RequestParam(required = false) String notas1,
            @RequestParam String acorde2, @RequestParam String tipo2, @RequestParam String funcao2, @RequestParam(required = false) String notas2,
            @RequestParam String acorde3, @RequestParam String tipo3, @RequestParam String funcao3, @RequestParam(required = false) String notas3,
            @RequestParam String acorde4, @RequestParam String tipo4, @RequestParam String funcao4, @RequestParam(required = false) String notas4,
            @RequestParam String acorde5, @RequestParam String tipo5, @RequestParam String funcao5, @RequestParam(required = false) String notas5,
            @RequestParam String acorde6, @RequestParam String tipo6, @RequestParam String funcao6, @RequestParam(required = false) String notas6,
            @RequestParam String acorde7, @RequestParam String tipo7, @RequestParam String funcao7, @RequestParam(required = false) String notas7,
            HttpSession session,
            RedirectAttributes attributes) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";

        try {
            // Salvar cada acorde
            salvarAcorde(tom, 1, acorde1, tipo1, funcao1, notas1);
            salvarAcorde(tom, 2, acorde2, tipo2, funcao2, notas2);
            salvarAcorde(tom, 3, acorde3, tipo3, funcao3, notas3);
            salvarAcorde(tom, 4, acorde4, tipo4, funcao4, notas4);
            salvarAcorde(tom, 5, acorde5, tipo5, funcao5, notas5);
            salvarAcorde(tom, 6, acorde6, tipo6, funcao6, notas6);
            salvarAcorde(tom, 7, acorde7, tipo7, funcao7, notas7);

            attributes.addFlashAttribute("mensagem", "Campo harmônico de " + tom + " criado com sucesso!");
            return "redirect:/teoria/campo-harmonico";
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
            return "redirect:/teoria/campo-harmonico/cadastrar";
        }
    }

    private void salvarAcorde(String tom, int grau, String acorde, String tipo, String funcao, String notas) {
        TeoriaCampoHarmonicoDTO dto = new TeoriaCampoHarmonicoDTO();
        dto.setTom(tom);
        dto.setGrau(grau);
        dto.setAcorde(acorde);
        dto.setTipo(tipo);
        dto.setFuncao(funcao);
        dto.setNotas(notas);
        teoriaService.salvarCampoHarmonico(dto);
    }

    private List<String> getTons() {
        return List.of(
                "C", "C#", "Db", "D", "D#", "Eb", "E", "F",
                "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B"
        );
    }
}