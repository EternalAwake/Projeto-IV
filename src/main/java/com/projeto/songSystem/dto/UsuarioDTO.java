package com.projeto.songSystem.dto;

import com.projeto.songSystem.models.enums.Role;
import java.time.LocalDateTime;

public class UsuarioDTO {

    private Long id;
    private String username;
    private String email;
    private String nome;
    private String senha;
    private Boolean ativo;
    private Role role;
    private String perguntaSecreta;
    // respostaSecreta nunca vai no DTO — só é lida/gravada pelo service
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public UsuarioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPerguntaSecreta() { return perguntaSecreta; }
    public void setPerguntaSecreta(String perguntaSecreta) { this.perguntaSecreta = perguntaSecreta; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }
}
