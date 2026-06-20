package com.projeto.songSystem.config;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de segurança por sessão.
 *
 * Interceptor que:
 *  - Bloqueia acesso a rotas protegidas sem sessão ativa.
 *  - Bloqueia acesso a rotas /admin/** para usuários sem role ADMIN.
 *  - Adiciona headers anti-cache em todas as respostas.
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private static final String[] ROTAS_PUBLICAS = {
        "/login", "/login/autenticar", "/cadastro", "/cadastro/salvar",
        "/recuperar-senha", "/recuperar-senha/pergunta", "/recuperar-senha/redefinir",
        "/admin/setup", "/admin/setup/salvar",
        "/css/**", "/js/**", "/images/**", "/uploads/**", "/webjars/**",
        "/favicon.ico", "/error"
    };

    @Bean
    public HandlerInterceptor authInterceptor() {
        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) throws Exception {

                String uri = request.getRequestURI();
                setNoCacheHeaders(response);

                // Rotas públicas passam sem verificação
                for (String publica : ROTAS_PUBLICAS) {
                    String pattern = publica.replace("/**", "");
                    if (uri.equals(publica) || uri.startsWith(pattern + "/") || uri.equals(pattern)) {
                        return true;
                    }
                }

                // Verificar sessão ativa
                HttpSession session = request.getSession(false);
                UsuarioDTO usuarioDTO = (session != null)
                        ? (UsuarioDTO) session.getAttribute("usuarioDTO")
                        : null;

                if (usuarioDTO == null) {
                    if (session != null) session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }

                // Rotas /admin/** exigem role ADMIN
                if (uri.startsWith("/admin")) {
                    if (!Role.ADMIN.equals(usuarioDTO.getRole())) {
                        response.sendRedirect(request.getContextPath() + "/inicio");
                        return false;
                    }
                }

                return true;
            }

            private void setNoCacheHeaders(HttpServletResponse response) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(ROTAS_PUBLICAS);
    }
}
