package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.AlbumModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {

    @EntityGraph(attributePaths = {"musicas", "banda"})
    List<AlbumModel> findByAlbumDestaqueTrue();

    List<AlbumModel> findByBandaBandaId(Long bandaId);

    /**
     * Lista todos os álbuns já carregando 'musicas' (para exibir a contagem
     * album.musicas.size() na tela) e 'banda' (exibida no card). Evita
     * LazyInitializationException com open-in-view desativado.
     */
    @EntityGraph(attributePaths = {"musicas", "banda"})
    @Query("SELECT DISTINCT a FROM AlbumModel a")
    List<AlbumModel> findAllWithMusicasAndBanda();

    @Query("SELECT DISTINCT a FROM AlbumModel a " +
            "LEFT JOIN FETCH a.banda " +
            "LEFT JOIN FETCH a.musicas " +
            "WHERE a.albumId = :id")
    Optional<AlbumModel> findByIdWithMusicasAndBanda(@Param("id") Long id);

}
