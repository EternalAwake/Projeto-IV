package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.TeoriaGeralDTO;
import com.projeto.songSystem.dto.TeoriaGeralResponseDTO;
import com.projeto.songSystem.models.TeoriaGeralModel;
import com.projeto.songSystem.repositories.TeoriaGeralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeoriaGeralService {

    @Autowired
    private TeoriaGeralRepository teoriaRepository;

    // Converter Entity para ResponseDTO
    private TeoriaGeralResponseDTO convertToResponseDTO(TeoriaGeralModel teoria) {
        TeoriaGeralResponseDTO dto = new TeoriaGeralResponseDTO();
        dto.setId(teoria.getId());
        dto.setTitulo(teoria.getTitulo());
        dto.setDescricao(teoria.getDescricao());
        dto.setCategoria(teoria.getCategoria());
        dto.setUrlImagem(teoria.getUrlImagem());
        dto.setIcone(teoria.getIcone());
        dto.setCorIcone(teoria.getCorIcone());
        dto.setTipoBadge(teoria.getTipoBadge());
        dto.setUrlRecurso(teoria.getUrlRecurso());
        dto.setOrdemExibicao(teoria.getOrdemExibicao());
        dto.setDataCriacao(teoria.getDataCriacao());
        dto.setDataAtualizacao(teoria.getDataAtualizacao());
        return dto;
    }

    // Converter DTO para Entity
    private TeoriaGeralModel convertToEntity(TeoriaGeralDTO dto) {
        TeoriaGeralModel teoria = new TeoriaGeralModel();
        teoria.setTitulo(dto.getTitulo());
        teoria.setConteudo(dto.getConteudo());
        teoria.setDescricao(dto.getDescricao());
        teoria.setCategoria(dto.getCategoria());
        teoria.setUrlImagem(dto.getUrlImagem());
        teoria.setIcone(dto.getIcone());
        teoria.setCorIcone(dto.getCorIcone());
        teoria.setTipoBadge(dto.getTipoBadge());
        teoria.setUrlRecurso(dto.getUrlRecurso());
        teoria.setOrdemExibicao(dto.getOrdemExibicao() != null ? dto.getOrdemExibicao() : 0);
        teoria.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return teoria;
    }

    // Atualizar Entity existente com DTO
    private void updateEntityFromDTO(TeoriaGeralModel existing, TeoriaGeralDTO dto) {
        existing.setTitulo(dto.getTitulo());
        existing.setConteudo(dto.getConteudo());
        existing.setDescricao(dto.getDescricao());
        existing.setCategoria(dto.getCategoria());
        existing.setUrlImagem(dto.getUrlImagem());
        existing.setIcone(dto.getIcone());
        existing.setCorIcone(dto.getCorIcone());
        existing.setTipoBadge(dto.getTipoBadge());
        existing.setUrlRecurso(dto.getUrlRecurso());
        existing.setOrdemExibicao(dto.getOrdemExibicao() != null ? dto.getOrdemExibicao() : 0);
        existing.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
    }

    // Criar nova teoria
    @Transactional
    public TeoriaGeralResponseDTO criarTeoria(TeoriaGeralDTO dto) {
        // Verificar se título já existe
        if (teoriaRepository.existsByTitulo(dto.getTitulo())) {
            throw new RuntimeException("Já existe uma teoria com este título");
        }

        TeoriaGeralModel teoria = convertToEntity(dto);
        TeoriaGeralModel saved = teoriaRepository.save(teoria);
        return convertToResponseDTO(saved);
    }

    // Buscar todas as teorias ativas
    public List<TeoriaGeralResponseDTO> buscarTodasAtivas() {
        return teoriaRepository.findByAtivoTrueOrderByOrdemExibicaoAsc()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar todas as teorias (incluindo inativas)
    public List<TeoriaGeralResponseDTO> buscarTodas() {
        return teoriaRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public TeoriaGeralResponseDTO buscarPorId(Long id) {
        TeoriaGeralModel teoria = teoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoria não encontrada com ID: " + id));
        return convertToResponseDTO(teoria);
    }

    // Buscar teoria completa (com conteúdo) para edição
    public TeoriaGeralDTO buscarParaEdicao(Long id) {
        TeoriaGeralModel teoria = teoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoria não encontrada com ID: " + id));

        TeoriaGeralDTO dto = new TeoriaGeralDTO();
        dto.setId(teoria.getId());
        dto.setTitulo(teoria.getTitulo());
        dto.setConteudo(teoria.getConteudo());
        dto.setDescricao(teoria.getDescricao());
        dto.setCategoria(teoria.getCategoria());
        dto.setUrlImagem(teoria.getUrlImagem());
        dto.setIcone(teoria.getIcone());
        dto.setCorIcone(teoria.getCorIcone());
        dto.setTipoBadge(teoria.getTipoBadge());
        dto.setUrlRecurso(teoria.getUrlRecurso());
        dto.setOrdemExibicao(teoria.getOrdemExibicao());
        dto.setAtivo(teoria.getAtivo());
        return dto;
    }

    // Atualizar teoria
    @Transactional
    public TeoriaGeralResponseDTO atualizarTeoria(Long id, TeoriaGeralDTO dto) {
        TeoriaGeralModel existing = teoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoria não encontrada com ID: " + id));

        // Verificar se o novo título já existe em outra teoria
        if (!existing.getTitulo().equals(dto.getTitulo()) &&
                teoriaRepository.existsByTitulo(dto.getTitulo())) {
            throw new RuntimeException("Já existe uma teoria com este título");
        }

        updateEntityFromDTO(existing, dto);
        TeoriaGeralModel updated = teoriaRepository.save(existing);
        return convertToResponseDTO(updated);
    }

    // Excluir teoria (soft delete)
    @Transactional
    public void excluirTeoria(Long id) {
        TeoriaGeralModel teoria = teoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoria não encontrada com ID: " + id));
        teoria.setAtivo(false);
        teoriaRepository.save(teoria);
    }

    // Excluir permanentemente
    @Transactional
    public void excluirPermanentemente(Long id) {
        if (!teoriaRepository.existsById(id)) {
            throw new RuntimeException("Teoria não encontrada com ID: " + id);
        }
        teoriaRepository.deleteById(id);
    }

    // Buscar por categoria
    public List<TeoriaGeralResponseDTO> buscarPorCategoria(String categoria) {
        return teoriaRepository.findByCategoriaAndAtivoTrueOrderByOrdemExibicaoAsc(categoria)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar todas as categorias disponíveis
    public List<String> buscarTodasCategorias() {
        return teoriaRepository.findAllCategorias();
    }

    // Buscar por palavra-chave
    public List<TeoriaGeralResponseDTO> buscarPorPalavraChave(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return buscarTodasAtivas();
        }
        return teoriaRepository.searchByKeyword(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar últimas adicionadas
    public List<TeoriaGeralResponseDTO> buscarUltimasAdicionadas() {
        return teoriaRepository.findTop5ByOrderByDataCriacaoDesc()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Alternar status ativo/inativo
    @Transactional
    public void alternarStatus(Long id) {
        TeoriaGeralModel teoria = teoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoria não encontrada com ID: " + id));
        teoria.setAtivo(!teoria.getAtivo());
        teoriaRepository.save(teoria);
    }

    // Adicione este método no TeoriaGeralService
    public Page<TeoriaGeralResponseDTO> buscarPaginado(Pageable pageable) {
        Page<TeoriaGeralModel> page = teoriaRepository.findByAtivoTrue(pageable);
        return page.map(this::convertToResponseDTO);
    }
}