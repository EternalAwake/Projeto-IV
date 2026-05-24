package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TecnicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TecnicaRepository extends JpaRepository<TecnicaModel, Long> {

    List<TecnicaModel> findAllByOrderByNomeAsc();

    List<TecnicaModel> findByCategoriaOrderByNomeAsc(String categoria);

    List<TecnicaModel> findByNivelOrderByNomeAsc(String nivel);

    List<TecnicaModel> findByNomeContainingIgnoreCase(String nome);
}