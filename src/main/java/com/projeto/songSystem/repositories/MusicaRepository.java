package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.MusicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicaRepository extends JpaRepository<MusicaModel, Long> {

    List<MusicaModel> findByMusicaDestaqueTrue();

    @Query("SELECT m FROM MusicaModel m LEFT JOIN FETCH m.banda LEFT JOIN FETCH m.album WHERE m.musicaId = :id")
    Optional<MusicaModel> findByIdWithBandaAndAlbum(@Param("id") Long id);

    @Query("""
        SELECT m FROM MusicaModel m
        WHERE (:bandaId IS NULL OR m.banda.bandaId = :bandaId)
        AND (:tom IS NULL OR m.musicaTom = :tom)
        AND (:busca IS NULL OR LOWER(m.musicaNome) LIKE CONCAT('%', :busca, '%'))
        """)
    List<MusicaModel> filtrarMusicas(
            @Param("bandaId") Long bandaId,
            @Param("tom") String tom,
            @Param("busca") String busca
    );

}
