package com.projeto.songSystem.models.enums;

public enum StatusRepertorio {
    ESTUDANDO("Estudando"),
    APRENDIDA("Aprendida"),
    DOMINADA("Dominada");

    private final String descricao;

    StatusRepertorio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}