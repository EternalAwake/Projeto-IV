package com.projeto.songSystem.models;

import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repertorio_item")
public class RepertorioItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "musica_id", nullable = false)
    private MusicaModel musica;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusRepertorio status = StatusRepertorio.ESTUDANDO;

    @Enumerated(EnumType.STRING)
    private Dificuldade dificuldade = Dificuldade.MEDIO;

    @Column(name = "ultima_pratica")
    private LocalDate ultimaPratica;

    @Column(name = "contador_praticas")
    private Integer contadorPraticas = 0;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_adicionado", nullable = false, updatable = false)
    private LocalDateTime dataAdicionado;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Construtor padrão
    public RepertorioItemModel() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MusicaModel getMusica() {
        return musica;
    }

    public void setMusica(MusicaModel musica) {
        this.musica = musica;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public StatusRepertorio getStatus() {
        return status;
    }

    public void setStatus(StatusRepertorio status) {
        this.status = status;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public LocalDate getUltimaPratica() {
        return ultimaPratica;
    }

    public void setUltimaPratica(LocalDate ultimaPratica) {
        this.ultimaPratica = ultimaPratica;
    }

    public Integer getContadorPraticas() {
        return contadorPraticas;
    }

    public void setContadorPraticas(Integer contadorPraticas) {
        this.contadorPraticas = contadorPraticas;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataAdicionado() {
        return dataAdicionado;
    }

    public void setDataAdicionado(LocalDateTime dataAdicionado) {
        this.dataAdicionado = dataAdicionado;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}