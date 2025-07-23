package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.familyfund.backend.modelo.Usuario;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByNombre(String nombre);

    Usuario findByEmail(String nombre);

    Optional<Usuario> findById(Integer id);

    Boolean existsByNombre(String nombre);

    Boolean existsByEmail(String email);

}
