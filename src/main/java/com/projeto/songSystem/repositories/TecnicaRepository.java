package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TecnicaModel;
import com.projeto.songSystem.models.enums.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TecnicaRepository extends JpaRepository<TecnicaModel, Long> {

    List<TecnicaModel> findAllByOrderByNomeAsc();

    List<TecnicaModel> findByCategoriaOrderByNomeAsc(String categoria);

    // Corrigido: campo nivel é do tipo Nivel (enum), não String
    List<TecnicaModel> findByNivelOrderByNomeAsc(Nivel nivel);

    List<TecnicaModel> findByNomeContainingIgnoreCase(String nome);
}