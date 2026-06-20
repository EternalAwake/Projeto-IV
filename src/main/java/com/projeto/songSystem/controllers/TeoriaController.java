package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.*;
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
@RequestMapping("/teoria")
public class TeoriaController {

    @Autowired
    private TeoriaService teoriaService;

    // Página inicial da teoria
    @GetMapping
    public String index(Model model, HttpSession session) {
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) return "redirect:/login";
        model.addAttribute("usuarioDTO", usuarioDto);
        return "teoria";
    }
}