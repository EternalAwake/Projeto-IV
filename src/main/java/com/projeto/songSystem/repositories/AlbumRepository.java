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

}
