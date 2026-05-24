package com.projeto.songSystem.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Musica")
public class MusicaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long musicaId;

    @Column(nullable = false, length = 150)
    private String musicaNome;

    @Column(length = 10)
    private String musicaTom;

    @Column(length = 10)
    private String musicaDuracao;

    @Column(columnDefinition = "TEXT")
    private String musicaAnotacoes;

    @Column(length = 50)
    private String musicaAfinacao;

    @Column(name = "musica_destaque")
    private Boolean musicaDestaque = false;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "banda_id", nullable = true)
    private BandaModel banda;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = true)
    private AlbumModel album;

    public Long getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Long musicaId) {
        this.musicaId = musicaId;
    }

    public String getMusicaNome() {
        return musicaNome;
    }

    public void setMusicaNome(String musicaNome) {
        this.musicaNome = musicaNome;
    }

    public String getMusicaTom() {
        return musicaTom;
    }

    public void setMusicaTom(String musicaTom) {
        this.musicaTom = musicaTom;
    }

    public String getMusicaDuracao() {
        return musicaDuracao;
    }

    public void setMusicaDuracao(String musicaDuracao) {
        this.musicaDuracao = musicaDuracao;
    }

    public String getMusicaAnotacoes() {
        return musicaAnotacoes;
    }

    public void setMusicaAnotacoes(String musicaAnotacoes) {
        this.musicaAnotacoes = musicaAnotacoes;
    }

    public String getMusicaAfinacao() {
        return musicaAfinacao;
    }

    public void setMusicaAfinacao(String musicaAfinacao) {
        this.musicaAfinacao = musicaAfinacao;
    }

    public Boolean getMusicaDestaque() {
        return musicaDestaque;
    }

    public void setMusicaDestaque(Boolean musicaDestaque) {
        this.musicaDestaque = musicaDestaque;
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

    public AlbumModel getAlbum() {
        return album;
    }

    public void setAlbum(AlbumModel album) {
        this.album = album;
    }
}
