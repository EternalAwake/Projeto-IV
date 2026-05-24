package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.SetlistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SetlistRepository extends JpaRepository<SetlistModel, Long> {
    List<SetlistModel> findByUsuarioId(Long usuarioId);
}