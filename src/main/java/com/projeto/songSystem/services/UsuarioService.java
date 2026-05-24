package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.UsuarioDTO;
import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setAtivo(true);
        usuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return converterParaDTO(usuario);
    }

    public UsuarioDTO buscarPorId(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public UsuarioDTO buscarPorUsername(String username) {
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDTO(usuario);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDataAtualizacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return converterParaDTO(usuario);
    }

    @Transactional
    public void desativarUsuario(Long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setAtivo(false);
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    private UsuarioDTO converterParaDTO(UsuarioModel usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setAtivo(usuario.getAtivo());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());
        return dto;
    }

    public UsuarioModel autenticar(String usuarioEmail, String senha) {

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(usuarioEmail);

        if (usuarioOpt.isEmpty()) {
            return null;
        }

        UsuarioModel usuario = usuarioOpt.get();

        if (Objects.equals(senha, usuario.getSenha())) {
            usuario.setUsername(usuario.getUsername());
            usuario.setEmail(usuario.getEmail());
            usuario.setSenha(usuario.getSenha());
            usuario.setNome(usuario.getNome());
            usuario.setAtivo(usuario.getAtivo());
            usuario.setDataCadastro(usuario.getDataCadastro());
            usuario.setDataAtualizacao(usuario.getDataAtualizacao());
            usuarioRepository.save(usuario);
            return usuario;
        } else {
            return null;
        }
    }

}