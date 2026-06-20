package com.projeto.songSystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Mapeia as URLs /uploads/** para os arquivos físicos no diretório de uploads.
     *
     * Resolve o diretório para caminho absoluto antes de montar a URI file:,
     * pois caminhos relativos (ex.: ./uploads/...) não são servidos de forma
     * confiável por "file:" + string. Garante também a barra final, exigida
     * pelo resource handler para tratar o caminho como diretório.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = uploadPath.toUri().toString(); // gera file:///... com barra final

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
