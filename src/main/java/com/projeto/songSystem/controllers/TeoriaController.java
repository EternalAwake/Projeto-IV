package com.projeto.songSystem.controllers;

import com.projeto.songSystem.dto.*;
import com.projeto.songSystem.services.TeoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String index() {
        return "teoria";
    }
}