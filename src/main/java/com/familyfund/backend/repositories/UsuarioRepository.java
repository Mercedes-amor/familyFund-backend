package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.familyfund.backend.modelo.Usuario;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNombre(String nombre);

    Usuario findByEmail(String nombre);

    Optional<Usuario> findById(Long id);
    
    // Usuario findByEmail(String email);

    Boolean existsByNombre(String nombre);

    Boolean existsByEmail(String email);

}
