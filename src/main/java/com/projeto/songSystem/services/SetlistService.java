package com.projeto.songSystem.services;

import com.projeto.songSystem.dto.SetlistDTO;
import com.projeto.songSystem.dto.SetlistItemDTO;
import com.projeto.songSystem.models.*;
import com.projeto.songSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetlistService {

    @Autowired
    private SetlistRepository setlistRepository;

    @Autowired
    private SetlistItemRepository setlistItemRepository;

    @Autowired
    private RepertorioItemRepository repertorioItemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public SetlistDTO criarSetlist(String nome, String descricao, Long usuarioId) {

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        SetlistModel setlist = new SetlistModel();
        setlist.setNome(nome);
        setlist.setDescricao(descricao);
        setlist.setUsuario(usuario);
        setlist.setDataCriacao(LocalDateTime.now());

        SetlistModel saved = setlistRepository.save(setlist);

        return converterParaDTO(saved);
    }

    @Transactional
    public SetlistDTO adicionarMusicaAoSetlist(Long setlistId, Long repertorioItemId, Integer ordem) {
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado"));

        RepertorioItemModel repertorioItem = repertorioItemRepository.findById(repertorioItemId)
                .orElseThrow(() -> new RuntimeException("Item do repertório não encontrado"));

        // Verificar se já existe na ordem
        if (ordem == null) {
            ordem = setlist.getItens().size() + 1;
        }

        SetlistItemModel item = new SetlistItemModel();
        item.setSetlist(setlist);
        item.setRepertorioItem(repertorioItem);
        item.setOrdem(ordem);

        setlistItemRepository.save(item);
        setlist.setDataAtualizacao(LocalDateTime.now());
        setlistRepository.save(setlist);

        return converterParaDTO(setlist);
    }

    @Transactional
    public void removerMusicaDoSetlist(Long setlistId, Long itemId) {
        SetlistItemModel item = setlistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
        setlistItemRepository.delete(item);

        // Reordenar os itens
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado"));
        List<SetlistItemModel> itens = setlistItemRepository.findBySetlistIdOrderByOrdemAsc(setlistId);
        for (int i = 0; i < itens.size(); i++) {
            itens.get(i).setOrdem(i + 1);
            setlistItemRepository.save(itens.get(i));
        }

        setlist.setDataAtualizacao(LocalDateTime.now());
        setlistRepository.save(setlist);
    }

    @Transactional
    public void reordenarSetlist(Long setlistId, List<Long> itemIds) {
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado"));

        for (int i = 0; i < itemIds.size(); i++) {
            SetlistItemModel item = setlistItemRepository.findById(itemIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            item.setOrdem(i + 1);
            setlistItemRepository.save(item);
        }

        setlist.setDataAtualizacao(LocalDateTime.now());
        setlistRepository.save(setlist);
    }

    @Transactional
    public void excluirSetlist(Long setlistId) {
        setlistItemRepository.deleteBySetlistId(setlistId);
        setlistRepository.deleteById(setlistId);
    }

    public List<SetlistDTO> listarSetlistsPorUsuario(Long usuarioId) {
        List<SetlistModel> setlists = setlistRepository.findByUsuarioId(usuarioId);

        return setlists.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public SetlistDTO buscarSetlistPorId(Long setlistId) {
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado com ID: " + setlistId));


        return converterParaDTO(setlist);
    }

    public List<RepertorioItemModel> listarMusicasDisponiveisParaSetlist(Long usuarioId, Long setlistId) {
        // Buscar músicas do repertório que não estão neste setlist
        List<RepertorioItemModel> todasDoRepertorio = repertorioItemRepository.findByUsuarioId(usuarioId);
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado"));

        Set<Long> idsNoSetlist = setlist.getItens().stream()
                .map(item -> item.getRepertorioItem().getId())
                .collect(Collectors.toSet());

        return todasDoRepertorio.stream()
                .filter(item -> !idsNoSetlist.contains(item.getId()))
                .collect(Collectors.toList());
    }


    /**
     * Retorna o ID do setlist ao qual pertence um SetlistItem, verificando que
     * o setlist pertence ao usuário informado (proteção de ownership).
     */
    public Long buscarSetlistIdPorItem(Long itemId, Long usuarioId) {
        SetlistItemModel item = setlistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado com ID: " + itemId));

        SetlistModel setlist = item.getSetlist();
        if (!setlist.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Acesso negado: este item não pertence ao seu setlist.");
        }
        return setlist.getId();
    }

    private SetlistDTO converterParaDTO(SetlistModel setlist) {
        SetlistDTO dto = new SetlistDTO();
        dto.setId(setlist.getId());
        dto.setNome(setlist.getNome());
        dto.setDescricao(setlist.getDescricao());

        if (setlist.getUsuario() != null) {
            dto.setUsuarioId(setlist.getUsuario().getId());
            dto.setUsuarioNome(setlist.getUsuario().getNome());
            dto.setUsuarioUsername(setlist.getUsuario().getUsername());
        }

        dto.setDataCriacao(setlist.getDataCriacao());
        dto.setDataAtualizacao(setlist.getDataAtualizacao());

        List<SetlistItemDTO> itensDTO = setlist.getItens().stream()
                .map(this::converterItemParaDTO)
                .collect(Collectors.toList());
        dto.setItens(itensDTO);
        dto.setTotalMusicas(itensDTO.size());

        return dto;
    }

    private SetlistItemDTO converterItemParaDTO(SetlistItemModel item) {
        SetlistItemDTO dto = new SetlistItemDTO();
        dto.setId(item.getId());
        dto.setRepertorioItemId(item.getRepertorioItem().getId());
        dto.setMusicaId(item.getRepertorioItem().getMusica().getMusicaId());
        dto.setMusicaNome(item.getRepertorioItem().getMusica().getMusicaNome());
        dto.setBandaNome(item.getRepertorioItem().getMusica().getBanda() != null ?
                item.getRepertorioItem().getMusica().getBanda().getBandaNome() : "Artista desconhecido");
        dto.setMusicaTom(item.getRepertorioItem().getMusica().getMusicaTom());
        dto.setOrdem(item.getOrdem());
        return dto;
    }
}