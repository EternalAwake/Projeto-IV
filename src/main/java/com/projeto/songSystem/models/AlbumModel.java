package com.projeto.songSystem.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Album")
public class AlbumModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    @Column(nullable = false, length = 150)
    private String albumNome;

    @Column(name = "ano_lancamento")
    private Integer albumAnoLancamento;

    @Column(name = "numero_faixas")
    private Integer albumNumeroFaixas;

    @Column(length = 20)
    private String albumDuracao;

    @Column(columnDefinition = "TEXT")
    private String albumObservacoes;

    @Column(length = 500)
    private String albumImagem;

    @Column()
    private Boolean albumDestaque = false;

    // Campos de auditoria
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "banda_id", nullable = false)
    private BandaModel banda;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicaModel> musicas = new ArrayList<>();

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumNome() {
        return albumNome;
    }

    public void setAlbumNome(String albumNome) {
        this.albumNome = albumNome;
    }

    public Integer getAlbumAnoLancamento() {
        return albumAnoLancamento;
    }

    public void setAlbumAnoLancamento(Integer albumAnoLancamento) {
        this.albumAnoLancamento = albumAnoLancamento;
    }

    public Integer getAlbumNumeroFaixas() {
        return albumNumeroFaixas;
    }

    public void setAlbumNumeroFaixas(Integer albumNumeroFaixas) {
        this.albumNumeroFaixas = albumNumeroFaixas;
    }

    public String getAlbumDuracao() {
        return albumDuracao;
    }

    public void setAlbumDuracao(String albumDuracao) {
        this.albumDuracao = albumDuracao;
    }

    public String getAlbumObservacoes() {
        return albumObservacoes;
    }

    public void setAlbumObservacoes(String albumObservacoes) {
        this.albumObservacoes = albumObservacoes;
    }

    public String getAlbumImagem() {
        return albumImagem;
    }

    public void setAlbumImagem(String albumImagem) {
        this.albumImagem = albumImagem;
    }

    public Boolean getAlbumDestaque() {
        return albumDestaque;
    }

    public void setAlbumDestaque(Boolean albumDestaque) {
        this.albumDestaque = albumDestaque;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public BandaModel getBanda() {
        return banda;
    }

    public void setBanda(BandaModel banda) {
        this.banda = banda;
    }

    public List<MusicaModel> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<MusicaModel> musicas) {
        this.musicas = musicas;
    }
}
