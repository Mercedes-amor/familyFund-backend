package com.familyfund.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.familyfund.backend.modelo.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNombre(String nombre);

    Usuario findByEmail(String nombre);

    Optional<Usuario> findById(Long id);
    
    // Usuario findByEmail(String email);

    Boolean existsByNombre(String nombre);

    Boolean existsByEmail(String email);

    // Devuelve todos los usuarios asociados a una familia
    List<Usuario> findByFamily_Id(Long familyId);


}
