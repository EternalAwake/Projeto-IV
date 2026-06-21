package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {

    List<AlbumModel> findByAlbumDestaqueTrue();

    List<AlbumModel> findByBandaBandaId(Long bandaId);

    @Query("SELECT DISTINCT a FROM AlbumModel a " +
            "LEFT JOIN FETCH a.banda " +
            "LEFT JOIN FETCH a.musicas " +
            "WHERE a.albumId = :id")
    Optional<AlbumModel> findByIdWithMusicasAndBanda(@Param("id") Long id);

    @Query("""
        SELECT a FROM AlbumModel a
        WHERE (:bandaId IS NULL OR a.banda.bandaId = :bandaId)
        AND (:ano IS NULL OR a.albumAnoLancamento = :ano)
        AND (:busca IS NULL
             OR LOWER(a.albumNome) LIKE CONCAT('%', :busca, '%')
             OR LOWER(a.banda.bandaNome) LIKE CONCAT('%', :busca, '%'))
        """)
    List<AlbumModel> filtrarAlbuns(
            @Param("bandaId") Long bandaId,
            @Param("ano") Integer ano,
            @Param("busca") String busca
    );

    @Query("""
        SELECT DISTINCT a.albumAnoLancamento FROM AlbumModel a
        WHERE a.albumAnoLancamento IS NOT NULL
        AND (:bandaId IS NULL OR a.banda.bandaId = :bandaId)
        ORDER BY a.albumAnoLancamento DESC
        """)
    List<Integer> obterAnosDisponiveis(@Param("bandaId") Long bandaId);

}
