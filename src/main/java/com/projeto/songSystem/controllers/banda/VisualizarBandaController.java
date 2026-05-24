package com.projeto.songSystem.controllers.banda;

import com.projeto.songSystem.dto.BandaDTO;
import com.projeto.songSystem.services.BandaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class VisualizarBandaController {

    @Autowired
    private BandaService bandaService;

    @GetMapping("/biblioteca/bandas/visualizar/{id}")
    public String exibirHome(Model model, HttpSession session, @PathVariable Long id) {

        //Sessão
        /*UsuarioModel usuarioDto = (UsuarioModel) session.getAttribute("usuarioDto");
        if (usuarioDto == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioDto", usuarioDto);*/

        // Buscar a banda com álbuns e músicas
        BandaDTO banda = bandaService.obterBanda(id);
        model.addAttribute("banda", banda);

        return "VisualizarBanda";
    }

    @DeleteMapping("/biblioteca/bandas/visualizar/excluir/{id}")
    public ResponseEntity<String> excluirBanda(@PathVariable Long id) {

        boolean retorno = bandaService.excluirBanda(id);

        if(retorno) {
            return ResponseEntity.ok("Banda excluída com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir banda.");
    }

}
