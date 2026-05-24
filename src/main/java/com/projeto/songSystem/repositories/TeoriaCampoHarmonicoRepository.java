package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TeoriaCampoHarmonicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeoriaCampoHarmonicoRepository extends JpaRepository<TeoriaCampoHarmonicoModel, Long> {

    // Listar todos ordenados por tom e grau
    List<TeoriaCampoHarmonicoModel> findAllByOrderByTomAscGrauAsc();

    // Buscar todos os campos de um tom específico
    List<TeoriaCampoHarmonicoModel> findByTomOrderByGrauAsc(String tom);

    // Verificar duplicação - usado no cadastro
    List<TeoriaCampoHarmonicoModel> findByTomAndGrau(String tom, Integer grau);

    // Verificar duplicação ignorando um ID - usado na edição
    @Query("SELECT c FROM TeoriaCampoHarmonicoModel c WHERE c.tom = :tom AND c.grau = :grau AND c.id != :id")
    List<TeoriaCampoHarmonicoModel> findByTomAndGrauAndIdNot(@Param("tom") String tom,
                                                             @Param("grau") Integer grau,
                                                             @Param("id") Long id);

    // Buscar por função (Tônica, Subdominante, Dominante)
    List<TeoriaCampoHarmonicoModel> findByFuncao(String funcao);

    // Buscar por tipo (Maior, Menor, Diminuto, Aumentado)
    List<TeoriaCampoHarmonicoModel> findByTipo(String tipo);

    // Buscar por tom e função
    List<TeoriaCampoHarmonicoModel> findByTomAndFuncao(String tom, String funcao);

    // Buscar um campo específico por tom e grau (retorna Optional)
    Optional<TeoriaCampoHarmonicoModel> findOneByTomAndGrau(String tom, Integer grau);
}