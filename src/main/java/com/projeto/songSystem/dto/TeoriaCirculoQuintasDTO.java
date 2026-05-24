package com.projeto.songSystem.dto;

public class TeoriaCirculoQuintasDTO {
    private Long id;
    private Integer posicao;
    private String tom;
    private String sustenidos;
    private String bemois;
    private String relativoMenor;
    private Integer quantidadeSustenidos;
    private Integer quantidadeBemois;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPosicao() { return posicao; }
    public void setPosicao(Integer posicao) { this.posicao = posicao; }

    public String getTom() { return tom; }
    public void setTom(String tom) { this.tom = tom; }

    public String getSustenidos() { return sustenidos; }
    public void setSustenidos(String sustenidos) { this.sustenidos = sustenidos; }

    public String getBemois() { return bemois; }
    public void setBemois(String bemois) { this.bemois = bemois; }

    public String getRelativoMenor() { return relativoMenor; }
    public void setRelativoMenor(String relativoMenor) { this.relativoMenor = relativoMenor; }

    public Integer getQuantidadeSustenidos() { return quantidadeSustenidos; }
    public void setQuantidadeSustenidos(Integer quantidadeSustenidos) { this.quantidadeSustenidos = quantidadeSustenidos; }

    public Integer getQuantidadeBemois() { return quantidadeBemois; }
    public void setQuantidadeBemois(Integer quantidadeBemois) { this.quantidadeBemois = quantidadeBemois; }
}