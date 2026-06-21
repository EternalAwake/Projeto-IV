package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.SetlistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetlistRepository extends JpaRepository<SetlistModel, Long> {
    List<SetlistModel> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT s FROM SetlistModel s
        WHERE s.usuario.id = :usuarioId
        AND (:busca IS NULL OR LOWER(s.nome) LIKE CONCAT('%', :busca, '%'))
        AND (:qtdMin IS NULL OR SIZE(s.itens) >= :qtdMin)
        AND (:qtdMax IS NULL OR SIZE(s.itens) <= :qtdMax)
        ORDER BY
            CASE WHEN :ordem = 'nome' THEN s.nome END ASC,
            CASE WHEN :ordem = 'data' THEN s.dataCriacao END DESC,
            CASE WHEN :ordem = 'musicas' THEN SIZE(s.itens) END DESC
        """)
    List<SetlistModel> filtrarSetlists(
            @Param("usuarioId") Long usuarioId,
            @Param("busca") String busca,
            @Param("qtdMin") Integer qtdMin,
            @Param("qtdMax") Integer qtdMax,
            @Param("ordem") String ordem
    );
}