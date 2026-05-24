package com.projeto.songSystem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "teoria_campo_harmonico")
public class TeoriaCampoHarmonicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String tom;

    @Column(nullable = false)
    private Integer grau;

    @Column(length = 20)
    private String acorde;

    @Column(length = 20)
    private String tipo;

    @Column(length = 30)
    private String funcao;

    @Column(length = 100)
    private String notas;

    @Column(name = "campo_armadura", length = 50)
    private String campoArmadura;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

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
}