package com.projeto.songSystem.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Banda")
public class BandaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bandaId;

    @Column(nullable = false, length = 100)
    private String bandaNome;

    @Column(length = 100)
    private String bandaGenero;

    @Column(length = 50)
    private String bandaPais;

    @Column(length = 500)
    private String bandaImagem;

    @Column(columnDefinition = "TEXT")
    private String bandaBiografia;

    @Column(length = 4)
    private String bandaAnoFormacao;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime bandaDataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime bandaDataAtualizacao;

    @Column()
    private Boolean bandaDestaque = false;

    @OneToMany(mappedBy = "banda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumModel> albuns = new ArrayList<>();

    @OneToMany(mappedBy = "banda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicaModel> musicas = new ArrayList<>();

    public Long getBandaId() {
        return bandaId;
    }

    public void setBandaId(Long bandaId) {
        this.bandaId = bandaId;
    }

    public String getBandaNome() {
        return bandaNome;
    }

    public void setBandaNome(String bandaNome) {
        this.bandaNome = bandaNome;
    }

    public String getBandaGenero() {
        return bandaGenero;
    }

    public void setBandaGenero(String bandaGenero) {
        this.bandaGenero = bandaGenero;
    }

    public String getBandaPais() {
        return bandaPais;
    }

    public void setBandaPais(String bandaPais) {
        this.bandaPais = bandaPais;
    }

    public String getBandaImagem() {
        return bandaImagem;
    }

    public void setBandaImagem(String bandaImagem) {
        this.bandaImagem = bandaImagem;
    }

    public String getBandaBiografia() {
        return bandaBiografia;
    }

    public void setBandaBiografia(String bandaBiografia) {
        this.bandaBiografia = bandaBiografia;
    }

    public String getBandaAnoFormacao() {
        return bandaAnoFormacao;
    }

    public void setBandaAnoFormacao(String bandaAnoFormacao) {
        this.bandaAnoFormacao = bandaAnoFormacao;
    }

    public LocalDateTime getBandaDataCadastro() {
        return bandaDataCadastro;
    }

    public void setBandaDataCadastro(LocalDateTime bandaDataCadastro) {
        this.bandaDataCadastro = bandaDataCadastro;
    }

    public LocalDateTime getBandaDataAtualizacao() {
        return bandaDataAtualizacao;
    }

    public void setBandaDataAtualizacao(LocalDateTime bandaDataAtualizacao) {
        this.bandaDataAtualizacao = bandaDataAtualizacao;
    }

    public Boolean getBandaDestaque() {
        return bandaDestaque;
    }

    public void setBandaDestaque(Boolean bandaDestaque) {
        this.bandaDestaque = bandaDestaque;
    }

    public List<AlbumModel> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(List<AlbumModel> albuns) {
        this.albuns = albuns;
    }

    public List<MusicaModel> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<MusicaModel> musicas) {
        this.musicas = musicas;
    }

    public BandaModel() {}

}
