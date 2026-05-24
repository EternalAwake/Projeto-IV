package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.BandaModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class MusicaDTO {

    private Long musicaId;

    private String musicaNome;

    private String musicaTom;

    private String musicaDuracao;

    private String musicaAnotacoes;

    private String musicaAfinacao;

    private Boolean musicaDestaque = false;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;

    // Campos para formulário (IDs simples)
    private Long bandaId;

    private Long albumId;

    private BandaModel banda;

    private BandaDTO bandaDTO;

    private AlbumDTO albumDTO;

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
        this.musicaDestaque = musicaDestaque != null ? musicaDestaque : false;
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

    public AlbumDTO getAlbumDTO() {
        return albumDTO;
    }

    public void setAlbumDTO(AlbumDTO albumDTO) {
        this.albumDTO = albumDTO;
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

    public Long getBandaId() {
        return bandaId;
    }

    public void setBandaId(Long bandaId) {
        this.bandaId = bandaId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public BandaDTO getBandaDTO() {
        return bandaDTO;
    }

    public void setBandaDTO(BandaDTO bandaDTO) {
        this.bandaDTO = bandaDTO;
    }

    public MusicaDTO() {}
}
