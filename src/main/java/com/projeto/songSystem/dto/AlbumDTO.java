package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.BandaModel;
import com.projeto.songSystem.models.MusicaModel;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class AlbumDTO {

    private Long albumId;
    private String albumNome;
    private BandaModel banda;  // Para o select
    private BandaDTO bandaDTO;
    private Long bandaId;  // NOVO CAMPO - apenas o ID
    private Integer albumAnoLancamento;
    private Boolean albumDestaque;
    private Integer albumNumeroFaixas;
    private String albumDuracao;
    private String albumObservacoes;
    private String albumImagem;  // Caminho da imagem
    private MultipartFile albumAvatar;  // Para upload
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private List<MusicaDTO> musicasDto;
    private List<MusicaModel> musicas;

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

    public BandaModel getBanda() {
        return banda;
    }

    public void setBanda(BandaModel banda) {
        this.banda = banda;
    }

    public BandaDTO getBandaDTO() {
        return bandaDTO;
    }

    public void setBandaDTO(BandaDTO bandaDTO) {
        this.bandaDTO = bandaDTO;
    }

    public Long getBandaId() {
        return bandaId;
    }

    public void setBandaId(Long bandaId) {
        this.bandaId = bandaId;
    }

    public Integer getAlbumAnoLancamento() {
        return albumAnoLancamento;
    }

    public void setAlbumAnoLancamento(Integer albumAnoLancamento) {
        this.albumAnoLancamento = albumAnoLancamento;
    }

    public Boolean getAlbumDestaque() {
        return albumDestaque;
    }

    public void setAlbumDestaque(Boolean albumDestaque) {
        this.albumDestaque = albumDestaque;
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

    public MultipartFile getAlbumAvatar() {
        return albumAvatar;
    }

    public void setAlbumAvatar(MultipartFile albumAvatar) {
        this.albumAvatar = albumAvatar;
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

    public List<MusicaModel> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<MusicaModel> musicas) {
        this.musicas = musicas;
    }

    public List<MusicaDTO> getMusicasDto() {
        return musicasDto;
    }

    public void setMusicasDto(List<MusicaDTO> musicasDto) {
        this.musicasDto = musicasDto;
    }

    public AlbumDTO() {}

}
