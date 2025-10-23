package com.familyfund.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.dto.UsuarioDto;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Autowired
    TransactionRepository transactionRepository;

    // Crear y guardar nuevo Usuario
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getPhotoUrl() == null) {
            usuario.setPhotoUrl(
                    "https://res.cloudinary.com/dz2owkkwa/image/upload/v1761215068/Familyfund/ProfileDefault_kyrpza.png");
        }
        return usuarioRepository.save(usuario);
    }

    // Mostrar todos los usuarios
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar por id
    @Override
    public Usuario findById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

    // Buscar por email
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Método que devuelve todos los usuarios de una familia
    public List<Usuario> findByFamilyId(Long familyId) {
        return usuarioRepository.findByFamily_Id(familyId);
    }

    // Borrar por id
    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void borrarUsuario(Long usuarioId) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Poner user_id a null en todas sus transacciones
        transactionRepository.setUsuarioNull(usuario);

        // Borrar usuario
        usuarioRepository.delete(usuario);
    }

    // Convertir Usuario a UsuarioDTO
    public UsuarioDto toDto(Usuario usuario) {
        if (usuario == null)
            return null;
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setFamily(usuario.getFamily());
        dto.setPhotoUrl(usuario.getPhotoUrl());
        return dto;
    }

}
