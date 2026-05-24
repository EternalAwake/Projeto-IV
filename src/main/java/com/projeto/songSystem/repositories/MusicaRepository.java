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

}
