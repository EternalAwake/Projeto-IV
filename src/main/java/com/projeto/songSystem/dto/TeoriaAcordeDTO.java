package com.projeto.songSystem.dto;

public class TeoriaAcordeDTO {
    private Long id;
    private String nome;
    private String tipo;
    private String tonica;
    private String terça;
    private String quinta;
    private String setima;
    private String notas;
    private String cifra;
    private String descricao;
    private String posicoesBraco;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTonica() { return tonica; }
    public void setTonica(String tonica) { this.tonica = tonica; }

    public String getTerça() { return terça; }
    public void setTerça(String terça) { this.terça = terça; }

    public String getQuinta() { return quinta; }
    public void setQuinta(String quinta) { this.quinta = quinta; }

    public String getSetima() { return setima; }
    public void setSetima(String setima) { this.setima = setima; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getCifra() { return cifra; }
    public void setCifra(String cifra) { this.cifra = cifra; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPosicoesBraco() { return posicoesBraco; }
    public void setPosicoesBraco(String posicoesBraco) { this.posicoesBraco = posicoesBraco; }
}