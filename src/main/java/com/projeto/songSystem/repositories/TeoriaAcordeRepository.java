package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.TeoriaAcordeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeoriaAcordeRepository extends JpaRepository<TeoriaAcordeModel, Long> {
    List<TeoriaAcordeModel> findByTipoOrderByNomeAsc(String tipo);
    List<TeoriaAcordeModel> findAllByOrderByNomeAsc();
}