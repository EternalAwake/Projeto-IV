package com.projeto.songSystem.controllers;

/*import com.projeto.songSystem.dto.ConsultaDto;
import com.projeto.songSystem.dto.DashboardDto;
import com.projeto.songSystem.services.ConsultaService;*/
import com.projeto.songSystem.dto.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class InicioController {

    @GetMapping("/inicio")
    public String exibirHome(Model model, HttpSession session) {

        //Sessão
        UsuarioDTO usuarioDto = (UsuarioDTO) session.getAttribute("usuarioDTO");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDTO", usuarioDto);

        return "inicio";
    }
}
