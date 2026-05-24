package com.projeto.songSystem.dto;

public class RepertorioEstatisticasDTO {
    private Long total;
    private Long estudando;
    private Long aprendida;
    private Long dominada;
    private Long praticadasHoje;

    // Construtores
    public RepertorioEstatisticasDTO() {}

    public RepertorioEstatisticasDTO(Long total, Long estudando, Long aprendida, Long dominada, Long praticadasHoje) {
        this.total = total;
        this.estudando = estudando;
        this.aprendida = aprendida;
        this.dominada = dominada;
        this.praticadasHoje = praticadasHoje;
    }

    // Getters e Setters
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getEstudando() {
        return estudando;
    }

    public void setEstudando(Long estudando) {
        this.estudando = estudando;
    }

    public Long getAprendida() {
        return aprendida;
    }

    public void setAprendida(Long aprendida) {
        this.aprendida = aprendida;
    }

    public Long getDominada() {
        return dominada;
    }

    public void setDominada(Long dominada) {
        this.dominada = dominada;
    }

    public Long getPraticadasHoje() {
        return praticadasHoje;
    }

    public void setPraticadasHoje(Long praticadasHoje) {
        this.praticadasHoje = praticadasHoje;
    }
}