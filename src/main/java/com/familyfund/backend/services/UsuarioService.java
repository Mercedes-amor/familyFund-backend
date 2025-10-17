package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.UsuarioDto;
import com.familyfund.backend.modelo.Usuario;

public interface UsuarioService {
    Usuario save(Usuario usuario); // Crear o actualizar

    List<Usuario> findAll(); // Listar todos los usuarios

    Usuario findById(Long id); // Buscar por ID

    void deleteById(Long id); // Eliminar por ID

    void borrarUsuario(Long usuarioId); // Eliminar usuario desvinculando antes sus transacciones

    Usuario findByEmail(String email); // Buscar por email (opcional)

    List<Usuario> findByFamilyId(Long familyId);

    UsuarioDto toDto(Usuario usuario); // Convertir Usuario a DTO
}
