package com.projeto.songSystem.dto;

public class TeoriaEscalaDTO {
    private Long id;
    private String nome;
    private String tipo;
    private String tonica;
    private String notas;
    private String formula;
    private String descricao;
    private String graus;
    private String acordes;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTonica() { return tonica; }
    public void setTonica(String tonica) { this.tonica = tonica; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getFormula() { return formula; }
    public void setFormula(String formula) { this.formula = formula; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getGraus() { return graus; }
    public void setGraus(String graus) { this.graus = graus; }

    public String getAcordes() { return acordes; }
    public void setAcordes(String acordes) { this.acordes = acordes; }
}