package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.BandaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandaRepository extends JpaRepository<BandaModel, Long> {

    /**
     * Carrega uma coleção por consulta.
     *
     * Não junte "albuns" e "musicas" na mesma consulta: ambas são List (bags)
     * e o Hibernate lança MultipleBagFetchException ao tentar buscá-las juntas.
     * O BandaService executa as duas consultas dentro da mesma transação; assim,
     * o primeiro nível de cache do Hibernate completa as mesmas entidades.
     */
    @Query("SELECT DISTINCT b FROM BandaModel b LEFT JOIN FETCH b.albuns ORDER BY b.bandaNome")
    List<BandaModel> findAllWithAlbuns();

    @Query("SELECT DISTINCT b FROM BandaModel b LEFT JOIN FETCH b.musicas ORDER BY b.bandaNome")
    List<BandaModel> findAllWithMusicas();

    @Query("SELECT DISTINCT b FROM BandaModel b " +
            "LEFT JOIN FETCH b.albuns " +
            "WHERE b.bandaDestaque = true " +
            "ORDER BY b.bandaNome")
    List<BandaModel> findDestaquesWithAlbuns();

    @Query("SELECT DISTINCT b FROM BandaModel b " +
            "LEFT JOIN FETCH b.musicas " +
            "WHERE b.bandaDestaque = true " +
            "ORDER BY b.bandaNome")
    List<BandaModel> findDestaquesWithMusicas();

    /**
     * Consulta leve para selects de formulários, onde só ID e nome são usados.
     */
    List<BandaModel> findAllByOrderByBandaNomeAsc();

}
