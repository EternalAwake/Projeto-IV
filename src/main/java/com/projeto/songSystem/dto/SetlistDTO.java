package com.projeto.songSystem.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SetlistDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioUsername;
    private List<SetlistItemDTO> itens = new ArrayList<>();
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer totalMusicas;
    private Integer duracaoTotal; // em segundos

    // Construtor padrão
    public SetlistDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public String getUsuarioUsername() { return usuarioUsername; }
    public void setUsuarioUsername(String usuarioUsername) { this.usuarioUsername = usuarioUsername; }

    public List<SetlistItemDTO> getItens() { return itens; }
    public void setItens(List<SetlistItemDTO> itens) { this.itens = itens; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public Integer getTotalMusicas() { return totalMusicas; }
    public void setTotalMusicas(Integer totalMusicas) { this.totalMusicas = totalMusicas; }

    public Integer getDuracaoTotal() { return duracaoTotal; }
    public void setDuracaoTotal(Integer duracaoTotal) { this.duracaoTotal = duracaoTotal; }
}