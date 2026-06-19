package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.RepertorioEstatisticasDTO;
import com.projeto.songSystem.dto.RepertorioItemDTO;
import com.projeto.songSystem.models.MusicaModel;
import com.projeto.songSystem.models.RepertorioItemModel;
import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import com.projeto.songSystem.repositories.MusicaRepository;
import com.projeto.songSystem.repositories.RepertorioItemRepository;
import com.projeto.songSystem.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepertorioService {

    @Autowired
    private RepertorioItemRepository repertorioItemRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Adiciona uma música ao repertório do usuário
     */
    @Transactional
    public RepertorioItemDTO adicionarAoRepertorio(Long usuarioId, Long musicaId, Dificuldade dificuldade) {
        // Verificar se a música já está no repertório
        if (repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId).isPresent()) {
            throw new RuntimeException("Esta música já está no seu repertório");
        }

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        MusicaModel musica = musicaRepository.findById(musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada"));

        RepertorioItemModel item = new RepertorioItemModel();
        item.setUsuario(usuario);
        item.setMusica(musica);
        item.setStatus(StatusRepertorio.ESTUDANDO);
        item.setDificuldade(dificuldade != null ? dificuldade : Dificuldade.MEDIO);
        item.setContadorPraticas(0);
        item.setDataAdicionado(LocalDateTime.now());

        RepertorioItemModel saved = repertorioItemRepository.save(item);
        return converterParaDTO(saved);
    }

    /**
     * Lista todas as músicas do repertório do usuário
     */
    public List<RepertorioItemDTO> listarRepertorio(Long usuarioId) {
        List<RepertorioItemModel> itens = repertorioItemRepository.findByUsuarioId(usuarioId);
        return itens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista músicas do repertório filtradas por status
     */
    public List<RepertorioItemDTO> listarPorStatus(Long usuarioId, StatusRepertorio status) {
        List<RepertorioItemModel> itens = repertorioItemRepository.findByUsuarioIdAndStatus(usuarioId, status);
        return itens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um item específico do repertório
     */
    public RepertorioItemDTO buscarItem(Long usuarioId, Long musicaId) {
        RepertorioItemModel item = repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada no repertório"));
        return converterParaDTO(item);
    }

    /**
     * Atualiza o status de uma música no repertório
     */
    @Transactional
    public RepertorioItemDTO atualizarStatus(Long usuarioId, Long musicaId, StatusRepertorio status) {
        RepertorioItemModel item = repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada no repertório"));

        item.setStatus(status);
        item.setDataAtualizacao(LocalDateTime.now());

        return converterParaDTO(repertorioItemRepository.save(item));
    }

    /**
     * Atualiza a dificuldade de uma música no repertório
     */
    @Transactional
    public RepertorioItemDTO atualizarDificuldade(Long usuarioId, Long musicaId, Dificuldade dificuldade) {
        RepertorioItemModel item = repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada no repertório"));

        item.setDificuldade(dificuldade);
        item.setDataAtualizacao(LocalDateTime.now());

        return converterParaDTO(repertorioItemRepository.save(item));
    }

    /**
     * Registra uma prática da música
     */
    @Transactional
    public RepertorioItemDTO registrarPratica(Long usuarioId, Long musicaId) {
        RepertorioItemModel item = repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada no repertório"));

        item.setUltimaPratica(LocalDate.now());
        item.setContadorPraticas(item.getContadorPraticas() + 1);
        item.setDataAtualizacao(LocalDateTime.now());

        return converterParaDTO(repertorioItemRepository.save(item));
    }

    /**
     * Remove uma música do repertório
     */
    @Transactional
    public void removerDoRepertorio(Long usuarioId, Long musicaId) {
        RepertorioItemModel item = repertorioItemRepository.findByUsuarioIdAndMusicaId(usuarioId, musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada no repertório"));
        repertorioItemRepository.delete(item);
    }

    /**
     * Obtém estatísticas do repertório
     */
    public RepertorioEstatisticasDTO obterEstatisticas(Long usuarioId) {
        Long total = repertorioItemRepository.countByUsuarioId(usuarioId);
        Long estudando = repertorioItemRepository.countByUsuarioIdAndStatus(usuarioId, StatusRepertorio.ESTUDANDO);
        Long aprendida = repertorioItemRepository.countByUsuarioIdAndStatus(usuarioId, StatusRepertorio.APRENDIDA);
        Long dominada = repertorioItemRepository.countByUsuarioIdAndStatus(usuarioId, StatusRepertorio.DOMINADA);

        // Músicas praticadas hoje
        LocalDate hoje = LocalDate.now();
        List<RepertorioItemModel> itens = repertorioItemRepository.findByUsuarioId(usuarioId);
        long praticadasHoje = itens.stream()
                .filter(item -> item.getUltimaPratica() != null && item.getUltimaPratica().equals(hoje))
                .count();

        RepertorioEstatisticasDTO estatisticas = new RepertorioEstatisticasDTO();
        estatisticas.setTotal(total);
        estatisticas.setEstudando(estudando);
        estatisticas.setAprendida(aprendida);
        estatisticas.setDominada(dominada);
        estatisticas.setPraticadasHoje(praticadasHoje);

        return estatisticas;
    }

    public List<RepertorioItemDTO> filtrarItensRepertorio(StatusRepertorio status, Dificuldade dificuldade, String musicaTom, String busca) {
        String musicaTomSanitizado = (musicaTom == null || musicaTom.isEmpty()) ? null : musicaTom.trim();
        String buscaSanitizada = (busca == null || busca.isEmpty()) ? null : busca.trim().toLowerCase();

        return repertorioItemRepository.filtrarItemRepertorio(status, dificuldade, musicaTomSanitizado, buscaSanitizada)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Model para DTO
     */
    private RepertorioItemDTO converterParaDTO(RepertorioItemModel item) {
        RepertorioItemDTO dto = new RepertorioItemDTO();
        dto.setId(item.getId());
        dto.setMusicaId(item.getMusica().getMusicaId());
        dto.setMusicaNome(item.getMusica().getMusicaNome());
        dto.setMusicaTom(item.getMusica().getMusicaTom());
        dto.setMusicaDuracao(item.getMusica().getMusicaDuracao());

        if (item.getMusica().getBanda() != null) {
            dto.setBandaNome(item.getMusica().getBanda().getBandaNome());
        }

        dto.setStatus(item.getStatus());
        dto.setDificuldade(item.getDificuldade());
        dto.setUltimaPratica(item.getUltimaPratica());
        dto.setContadorPraticas(item.getContadorPraticas());
        dto.setObservacoes(item.getObservacoes());
        dto.setDataAdicionado(item.getDataAdicionado());

        return dto;
    }
}