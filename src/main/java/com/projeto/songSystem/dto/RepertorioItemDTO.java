package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RepertorioItemDTO {

    private Long id;
    private Long musicaId;
    private String musicaNome;
    private String bandaNome;
    private String musicaTom;
    private String musicaDuracao;
    private StatusRepertorio status;
    private Dificuldade dificuldade;
    private LocalDate ultimaPratica;
    private Integer contadorPraticas;
    private String observacoes;
    private LocalDateTime dataAdicionado;

    // Construtor padrão
    public RepertorioItemDTO() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Long musicaId) {
        this.musicaId = musicaId;
    }

    public String getMusicaNome() {
        return musicaNome;
    }

    public void setMusicaNome(String musicaNome) {
        this.musicaNome = musicaNome;
    }

    public String getBandaNome() {
        return bandaNome;
    }

    public void setBandaNome(String bandaNome) {
        this.bandaNome = bandaNome;
    }

    public String getMusicaTom() {
        return musicaTom;
    }

    public void setMusicaTom(String musicaTom) {
        this.musicaTom = musicaTom;
    }

    public String getMusicaDuracao() {
        return musicaDuracao;
    }

    public void setMusicaDuracao(String musicaDuracao) {
        this.musicaDuracao = musicaDuracao;
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
}