package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.MusicaModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicaRepository extends JpaRepository<MusicaModel, Long> {

    /**
     * Lista todas as músicas já carregando 'banda' e 'album'.
     * Necessário porque musicas.html exibe musica.banda.bandaNome e musica.album.albumNome
     * — relações @ManyToOne que são LAZY por padrão. Com open-in-view=false a sessão
     * já está fechada quando o Thymeleaf renderiza, causando LazyInitializationException.
     */
    @EntityGraph(attributePaths = {"banda", "album"})
    @Query("SELECT m FROM MusicaModel m")
    List<MusicaModel> findAllWithBandaAndAlbum();

    /**
     * Músicas em destaque já com banda e album carregados (exibidos em biblioteca.html).
     */
    @EntityGraph(attributePaths = {"banda", "album"})
    List<MusicaModel> findByMusicaDestaqueTrue();

    @Query("SELECT m FROM MusicaModel m LEFT JOIN FETCH m.banda LEFT JOIN FETCH m.album WHERE m.musicaId = :id")
    Optional<MusicaModel> findByIdWithBandaAndAlbum(@Param("id") Long id);

}
