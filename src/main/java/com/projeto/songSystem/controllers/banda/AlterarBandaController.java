package com.projeto.songSystem.controllers.banda;

import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.services.BandaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class AlterarBandaController {

    @Autowired
    private BandaService bandaService;

    @GetMapping("/biblioteca/bandas/visualizar/alterarbanda/{id}")
    public String exibirAlterarBanda(Model model, HttpSession session, @PathVariable Long id) {

        //Sessão
        /*UsuarioModel usuarioDto = (UsuarioModel) session.getAttribute("usuarioDto");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDto", usuarioDto);*/

        BandaDTO bandaDTO = bandaService.obterBanda(id);
        model.addAttribute("banda", bandaDTO);

        return "alterarbanda";
    }

    @PostMapping("/biblioteca/bandas/visualizar/alterarbanda")
    public String alterarBanda(@ModelAttribute("banda") BandaDTO bandaDTO,
                               RedirectAttributes attributes) {
        try {
            bandaService.alterarBanda(bandaDTO);
            attributes.addFlashAttribute("mensagem", "Banda alterada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("erro", "Falha ao alterar banda: " + e.getMessage());
        }
        return "redirect:/biblioteca/bandas/visualizar/alterarbanda/" + bandaDTO.getBandaId();
    }

}
