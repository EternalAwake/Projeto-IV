package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.BandaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BandaRepository extends JpaRepository<BandaModel, Long>, JpaSpecificationExecutor<BandaModel> {

    List<BandaModel> findByBandaDestaqueTrue();

    @Query("SELECT DISTINCT b FROM BandaModel b " +
            "LEFT JOIN FETCH b.albuns a " +
            "LEFT JOIN FETCH b.musicas m " +
            "WHERE b.bandaId = :id")
    Optional<BandaModel> findByIdWithAlbunsAndMusicas(@Param("id") Long id);

}