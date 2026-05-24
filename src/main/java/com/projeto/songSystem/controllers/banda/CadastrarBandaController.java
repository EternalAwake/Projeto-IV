package com.projeto.songSystem.controllers.banda;

import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.services.BandaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class CadastrarBandaController {

    @Autowired
    private BandaService bandaService;

    @GetMapping("/biblioteca/bandas/cadastrarbanda")
    public String exibirCadastrarBanda(Model model, HttpSession session) {

        //Sessão
        /*UsuarioModel usuarioDto = (UsuarioModel) session.getAttribute("usuarioDto");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDto", usuarioDto);*/

        BandaDTO bandaDTO = new BandaDTO();
        model.addAttribute("bandaDTO", bandaDTO);

        return "cadastrarbanda";
    }

    @PostMapping("/biblioteca/bandas/cadastrarbanda")
    public String cadastrarbanda(@ModelAttribute("bandaDTO") BandaDTO bandaDTO, RedirectAttributes attributes) {
        try {
            bandaService.cadastrarBanda(bandaDTO);
            attributes.addFlashAttribute("mensagem", "Banda cadastrada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Falha ao cadastrar banda.");
        }
        return "redirect:/biblioteca/bandas/cadastrarbanda";
    }
}
