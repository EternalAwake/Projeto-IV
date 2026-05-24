package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TeoriaGeralModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeoriaGeralRepository extends JpaRepository<TeoriaGeralModel, Long> {

    // Buscar por categoria
    List<TeoriaGeralModel> findByCategoriaOrderByOrdemExibicaoAsc(String categoria);

    // Buscar apenas os ativos
    List<TeoriaGeralModel> findByAtivoTrueOrderByOrdemExibicaoAsc();

    // Buscar por título (contém)
    List<TeoriaGeralModel> findByTituloContainingIgnoreCase(String titulo);

    // Buscar por categoria e ativo
    List<TeoriaGeralModel> findByCategoriaAndAtivoTrueOrderByOrdemExibicaoAsc(String categoria);

    // CORREÇÃO AQUI: Use o nome da ENTIDADE (TeoriaGeralModel) não da tabela
    @Query("SELECT DISTINCT t.categoria FROM TeoriaGeralModel t WHERE t.ativo = true")
    List<String> findAllCategorias();

    // CORREÇÃO AQUI: Use o nome da ENTIDADE (TeoriaGeralModel) não da tabela
    @Query("SELECT t FROM TeoriaGeralModel t WHERE t.ativo = true AND (LOWER(t.titulo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TeoriaGeralModel> searchByKeyword(@Param("keyword") String keyword);

    // Buscar últimos 5 adicionados
    List<TeoriaGeralModel> findTop5ByOrderByDataCriacaoDesc();

    // Verificar se título já existe (para evitar duplicação)
    boolean existsByTitulo(String titulo);

    // Buscar por URL do recurso
    Optional<TeoriaGeralModel> findByUrlRecurso(String urlRecurso);

    Page<TeoriaGeralModel> findByAtivoTrue(Pageable pageable);

}