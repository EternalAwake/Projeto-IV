package com.projeto.songSystem.dto;

public class FiltroCustomizadoDTO {
    private String campo;
    private String operador;
    private String valor;

    public FiltroCustomizadoDTO() {}

    public FiltroCustomizadoDTO(String campo, String operador, String valor) {
        this.campo = campo;
        this.operador = operador;
        this.valor = valor;
    }

    public String getCampo() { return campo; }
    public void setCampo(String campo) { this.campo = campo; }
    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}