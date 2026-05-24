package com.projeto.songSystem.dto;

public class TeoriaCampoHarmonicoDTO {
    private Long id;
    private String tom;
    private Integer grau;
    private String acorde;
    private String tipo;
    private String funcao;
    private String notas;
    private String campoArmadura;
    private String observacoes;
    private String quantidadeAcordes;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTom() { return tom; }
    public void setTom(String tom) { this.tom = tom; }

    public Integer getGrau() { return grau; }
    public void setGrau(Integer grau) { this.grau = grau; }

    public String getAcorde() { return acorde; }
    public void setAcorde(String acorde) { this.acorde = acorde; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getCampoArmadura() { return campoArmadura; }
    public void setCampoArmadura(String campoArmadura) { this.campoArmadura = campoArmadura; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getQuantidadeAcordes() {
        return quantidadeAcordes;
    }

    public void setQuantidadeAcordes(String quantidadeAcordes) {
        this.quantidadeAcordes = quantidadeAcordes;
    }
}