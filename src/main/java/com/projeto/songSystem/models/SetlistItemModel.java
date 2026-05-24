package com.projeto.songSystem.models;

import jakarta.persistence.*;

@Entity
@Table(name = "setlist_item")
public class SetlistItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "setlist_id", nullable = false)
    private SetlistModel setlist;

    @ManyToOne
    @JoinColumn(name = "repertorio_item_id", nullable = false)
    private RepertorioItemModel repertorioItem;

    @Column(nullable = false)
    private Integer ordem;

    // Construtor padrão
    public SetlistItemModel() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SetlistModel getSetlist() {
        return setlist;
    }

    public void setSetlist(SetlistModel setlist) {
        this.setlist = setlist;
    }

    public RepertorioItemModel getRepertorioItem() {
        return repertorioItem;
    }

    public void setRepertorioItem(RepertorioItemModel repertorioItem) {
        this.repertorioItem = repertorioItem;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}