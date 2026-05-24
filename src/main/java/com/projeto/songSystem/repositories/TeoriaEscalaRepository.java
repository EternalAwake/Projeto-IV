package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TeoriaEscalaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeoriaEscalaRepository extends JpaRepository<TeoriaEscalaModel, Long> {
    List<TeoriaEscalaModel> findByTipoOrderByNomeAsc(String tipo);
    List<TeoriaEscalaModel> findAllByOrderByNomeAsc();
}