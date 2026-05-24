package com.projeto.songSystem.models;

import com.projeto.songSystem.models.enums.Nivel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tecnica")
public class TecnicaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "como_fazer", columnDefinition = "TEXT")
    private String comoFazer;

    @Column(columnDefinition = "TEXT")
    private String dicas;

    private String imagem;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    @Column(columnDefinition = "TEXT")
    private String exercicios;

    @Column(name = "musicas_exemplo", columnDefinition = "TEXT")
    private String musicasExemplo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

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