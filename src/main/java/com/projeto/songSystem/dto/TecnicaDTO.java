package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.enums.Nivel;
import java.time.LocalDateTime;

public class TecnicaDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private String comoFazer;
    private String dicas;
    private String imagem;
    private String videoUrl;
    private Nivel nivel;
    private String exercicios;
    private String musicasExemplo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // Construtor padrão
    public TecnicaDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getComoFazer() { return comoFazer; }
    public void setComoFazer(String comoFazer) { this.comoFazer = comoFazer; }

    public String getDicas() { return dicas; }
    public void setDicas(String dicas) { this.dicas = dicas; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getExercicios() { return exercicios; }
    public void setExercicios(String exercicios) { this.exercicios = exercicios; }

    public String getMusicasExemplo() { return musicasExemplo; }
    public void setMusicasExemplo(String musicasExemplo) { this.musicasExemplo = musicasExemplo; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}