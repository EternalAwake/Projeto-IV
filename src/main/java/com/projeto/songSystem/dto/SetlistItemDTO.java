package com.projeto.songSystem.dto;

public class SetlistItemDTO {

    private Long id;
    private Long repertorioItemId;
    private Long musicaId;
    private String musicaNome;
    private String bandaNome;
    private String musicaTom;
    private Integer ordem;

    // Construtor padrão
    public SetlistItemDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRepertorioItemId() { return repertorioItemId; }
    public void setRepertorioItemId(Long repertorioItemId) { this.repertorioItemId = repertorioItemId; }

    public Long getMusicaId() { return musicaId; }
    public void setMusicaId(Long musicaId) { this.musicaId = musicaId; }

    public String getMusicaNome() { return musicaNome; }
    public void setMusicaNome(String musicaNome) { this.musicaNome = musicaNome; }

    public String getBandaNome() { return bandaNome; }
    public void setBandaNome(String bandaNome) { this.bandaNome = bandaNome; }

    public String getMusicaTom() { return musicaTom; }
    public void setMusicaTom(String musicaTom) { this.musicaTom = musicaTom; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}