package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.AlbumModel;
import com.projeto.songSystem.models.MusicaModel;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BandaDTO {

    // Campo para upload da imagem
    private MultipartFile bandaAvatar;

    private String bandaImagem;

    private Long bandaId;

    private String bandaNome;

    private String bandaGenero;

    private String bandaPais;

    private String bandaBiografia;

    private Boolean bandaDestaque;

    private String bandaAnoFormacao;

    private LocalDateTime bandaDataCadastro;

    private LocalDateTime bandaDataAtualizacao;

    private List<AlbumDTO> albuns = new ArrayList<>();

    private List<MusicaModel> musicas = new ArrayList<>();

    private List<MusicaDTO> musicasDTO = new ArrayList<>();

    public BandaDTO() {}

    public MultipartFile getBandaAvatar() {
        return bandaAvatar;
    }

    public void setBandaAvatar(MultipartFile bandaAvatar) {
        this.bandaAvatar = bandaAvatar;
    }

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

    public Boolean getBandaDestaque() {
        return bandaDestaque;
    }

    public void setBandaDestaque(Boolean bandaDestaque) {
        this.bandaDestaque = bandaDestaque;
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

    public List<AlbumDTO> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(List<AlbumDTO> albuns) {
        this.albuns = albuns;
    }

    public List<MusicaModel> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<MusicaModel> musicas) {
        this.musicas = musicas;
    }

    public List<MusicaDTO> getMusicasDTO() {
        return musicasDTO;
    }

    public void setMusicasDTO(List<MusicaDTO> musicasDTO) {
        this.musicasDTO = musicasDTO;
    }
}
