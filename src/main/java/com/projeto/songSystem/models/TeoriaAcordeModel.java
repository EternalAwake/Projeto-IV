package com.projeto.songSystem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "teoria_acorde")
public class TeoriaAcordeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nome;

    @Column(length = 20)
    private String tipo;

    @Column(length = 10)
    private String tonica;

    @Column(length = 10)
    private String terça;

    @Column(length = 10)
    private String quinta;

    @Column(length = 10)
    private String setima;

    @Column(length = 100)
    private String notas;

    @Column(length = 50)
    private String cifra;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "posicoes_braco", columnDefinition = "TEXT")
    private String posicoesBraco; // JSON com posições no braço

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