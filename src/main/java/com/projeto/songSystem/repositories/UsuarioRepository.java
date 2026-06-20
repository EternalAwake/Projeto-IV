package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.UsuarioModel;
import com.projeto.songSystem.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByUsername(String username);

    Optional<UsuarioModel> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UsuarioModel> findAllByOrderByDataCadastroDesc();

    List<UsuarioModel> findByRole(Role role);
}
