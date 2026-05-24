package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.TecnicaDTO;
import com.projeto.songSystem.models.TecnicaModel;
import com.projeto.songSystem.models.enums.Nivel;
import com.projeto.songSystem.repositories.TecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TecnicaService {

    @Autowired
    private TecnicaRepository tecnicaRepository;

    public List<TecnicaDTO> listarTodasTecnicas() {
        return tecnicaRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TecnicaDTO> listarTecnicasPorCategoria(String categoria) {
        return tecnicaRepository.findByCategoriaOrderByNomeAsc(categoria)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<TecnicaDTO> listarTecnicasPorNivel(String nivel) {
        return tecnicaRepository.findByNivelOrderByNomeAsc(nivel)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<String> listarCategorias() {
        return List.of("Mão Esquerda", "Mão Direita", "Percussão e Efeitos", "Timbre e Expressão", "Avançadas");
    }

    public List<String> listarNiveis() {
        return List.of("INICIANTE", "INTERMEDIARIO", "AVANCADO");
    }

    @Transactional
    public void salvarTecnica(TecnicaDTO dto) {
        TecnicaModel tecnica = new TecnicaModel();
        tecnica.setNome(dto.getNome());
        tecnica.setCategoria(dto.getCategoria());
        tecnica.setDescricao(dto.getDescricao());
        tecnica.setComoFazer(dto.getComoFazer());
        tecnica.setDicas(dto.getDicas());
        tecnica.setImagem(dto.getImagem());
        tecnica.setVideoUrl(dto.getVideoUrl());
        tecnica.setNivel(dto.getNivel());
        tecnica.setExercicios(dto.getExercicios());
        tecnica.setMusicasExemplo(dto.getMusicasExemplo());
        tecnica.setCriadoEm(LocalDateTime.now());
        tecnicaRepository.save(tecnica);
    }

    @Transactional
    public void atualizarTecnica(TecnicaDTO dto) {
        TecnicaModel tecnica = tecnicaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Técnica não encontrada"));
        tecnica.setNome(dto.getNome());
        tecnica.setCategoria(dto.getCategoria());
        tecnica.setDescricao(dto.getDescricao());
        tecnica.setComoFazer(dto.getComoFazer());
        tecnica.setDicas(dto.getDicas());
        tecnica.setImagem(dto.getImagem());
        tecnica.setVideoUrl(dto.getVideoUrl());
        tecnica.setNivel(dto.getNivel());
        tecnica.setExercicios(dto.getExercicios());
        tecnica.setMusicasExemplo(dto.getMusicasExemplo());
        tecnica.setAtualizadoEm(LocalDateTime.now());
        tecnicaRepository.save(tecnica);
    }

    public TecnicaDTO buscarTecnicaPorId(Long id) {
        TecnicaModel tecnica = tecnicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnica não encontrada com ID: " + id));
        return converterParaDTO(tecnica);
    }

    @Transactional
    public void excluirTecnica(Long id) {
        if (!tecnicaRepository.existsById(id)) {
            throw new RuntimeException("Técnica não encontrada com ID: " + id);
        }
        tecnicaRepository.deleteById(id);
    }

    public List<TecnicaDTO> filtrarTecnicas(String categoria, String nivel, String busca) {
        List<TecnicaModel> tecnicas = tecnicaRepository.findAll();
        return tecnicas.stream()
                .filter(t -> categoria == null || categoria.isEmpty() || t.getCategoria().equals(categoria))
                .filter(t -> nivel == null || nivel.isEmpty() || t.getNivel().name().equals(nivel))
                .filter(t -> busca == null || busca.isEmpty() ||
                        (t.getNome() != null && t.getNome().toLowerCase().contains(busca.toLowerCase())))
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private TecnicaDTO converterParaDTO(TecnicaModel model) {
        TecnicaDTO dto = new TecnicaDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setCategoria(model.getCategoria());
        dto.setDescricao(model.getDescricao());
        dto.setComoFazer(model.getComoFazer());
        dto.setDicas(model.getDicas());
        dto.setImagem(model.getImagem());
        dto.setVideoUrl(model.getVideoUrl());
        dto.setNivel(model.getNivel());
        dto.setExercicios(model.getExercicios());
        dto.setMusicasExemplo(model.getMusicasExemplo());
        dto.setCriadoEm(model.getCriadoEm());
        dto.setAtualizadoEm(model.getAtualizadoEm());
        return dto;
    }
}