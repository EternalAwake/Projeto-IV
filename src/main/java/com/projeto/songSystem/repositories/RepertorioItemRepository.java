package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.RepertorioItemModel;
import com.projeto.songSystem.models.enums.Dificuldade;
import com.projeto.songSystem.models.enums.StatusRepertorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RepertorioItemRepository extends JpaRepository<RepertorioItemModel, Long> {

    List<RepertorioItemModel> findByUsuarioId(Long usuarioId);

    @Query("SELECT r FROM RepertorioItemModel r WHERE r.usuario.id = :usuarioId AND r.musica.musicaId = :musicaId")
    Optional<RepertorioItemModel> findByUsuarioIdAndMusicaId(@Param("usuarioId") Long usuarioId, @Param("musicaId") Long musicaId);

    List<RepertorioItemModel> findByUsuarioIdAndStatus(Long usuarioId, StatusRepertorio status);

    @Query("SELECT COUNT(r) FROM RepertorioItemModel r WHERE r.usuario.id = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(r) FROM RepertorioItemModel r WHERE r.usuario.id = :usuarioId AND r.status = :status")
    Long countByUsuarioIdAndStatus(@Param("usuarioId") Long usuarioId, @Param("status") StatusRepertorio status);

    @Query("""
        SELECT r FROM RepertorioItemModel r
        JOIN r.musica m
        WHERE (:status IS NULL OR r.status = :status)
        AND (:dificuldade IS NULL OR r.dificuldade = :dificuldade)
        AND (:musicaTom IS NULL OR m.musicaTom = :musicaTom)
        AND (:busca IS NULL OR LOWER(m.musicaNome) LIKE CONCAT('%', :busca, '%'))
        """)
    List<RepertorioItemModel> filtrarItemRepertorio(
            @Param("status") StatusRepertorio status,
            @Param("dificuldade") Dificuldade dificuldade,
            @Param("musicaTom") String musicaTom,
            @Param("busca") String busca
    );
}