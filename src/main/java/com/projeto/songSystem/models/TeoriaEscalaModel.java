package com.projeto.songSystem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "teoria_escala")
public class TeoriaEscalaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 20)
    private String tipo;

    @Column(nullable = false, length = 10)
    private String tonica;

    @Column(length = 100)
    private String notas;

    @Column(length = 50)
    private String formula;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "graus", length = 100)
    private String graus;

    @Column(name = "acordes", length = 200)
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