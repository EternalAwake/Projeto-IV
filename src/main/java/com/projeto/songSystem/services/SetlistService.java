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
        System.out.println("=== CRIANDO SETLIST ===");
        System.out.println("Usuário ID: " + usuarioId);

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        SetlistModel setlist = new SetlistModel();
        setlist.setNome(nome);
        setlist.setDescricao(descricao);
        setlist.setUsuario(usuario);
        setlist.setDataCriacao(LocalDateTime.now());

        SetlistModel saved = setlistRepository.save(setlist);
        System.out.println("Setlist criado com ID: " + saved.getId());

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
        System.out.println("=== LISTANDO SETLISTS DO USUÁRIO: " + usuarioId);
        List<SetlistModel> setlists = setlistRepository.findByUsuarioId(usuarioId);
        System.out.println("Setlists encontrados: " + setlists.size());

        return setlists.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public SetlistDTO buscarSetlistPorId(Long setlistId) {
        SetlistModel setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new RuntimeException("Setlist não encontrado com ID: " + setlistId));

        System.out.println("Buscando setlist: " + setlist.getNome());
        System.out.println("Quantidade de itens: " + setlist.getItens().size());

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

    public List<SetlistDTO> filtrarSetlists(Long usuarioId, String busca, String quantidade, String ordem) {
        String buscaSanitizada = (busca == null || busca.isEmpty()) ? null : busca.trim().toLowerCase();

        Integer qtdMin = null;
        Integer qtdMax = null;

        if (quantidade != null && !quantidade.isEmpty()) {
            switch (quantidade) {
                case "0" -> { qtdMin = 0; qtdMax = 0; }
                case "1-5" -> { qtdMin = 1; qtdMax = 5; }
                case "6-10" -> { qtdMin = 6; qtdMax = 10; }
                case "11+" -> { qtdMin = 11; qtdMax = null; }
                default -> {}
            }
        }

        String ordemSanitizada = validarOrdem(ordem);

        List<SetlistModel> setlists = setlistRepository.filtrarSetlists(
                usuarioId, buscaSanitizada, qtdMin, qtdMax, ordemSanitizada
        );

        return setlists.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private String validarOrdem(String ordem) {
        if (ordem == null) return "nome";
        return switch (ordem) {
            case "nome", "data", "musicas" -> ordem;
            default -> "nome";
        };
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