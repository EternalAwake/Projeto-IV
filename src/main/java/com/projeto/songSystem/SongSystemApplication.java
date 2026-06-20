package com.projeto.songSystem;

import com.projeto.songSystem.services.UsuarioService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SongSystemApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SongSystemApplication.class, args);

        // Garante que o usuário admin exista na inicialização.
        // Se não existir, é criado sem senha — o primeiro login redireciona para setup.
        UsuarioService usuarioService = ctx.getBean(UsuarioService.class);
        usuarioService.garantirAdminExiste();
    }
}
