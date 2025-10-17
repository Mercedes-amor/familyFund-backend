package com.familyfund.backend.services;

import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.UsuarioDto;
import com.familyfund.backend.modelo.Usuario;
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

    // Crear y guardar nuevo Usuario
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getPhotoUrl() == null) {
            usuario.setPhotoUrl("https://res.cloudinary.com/dz2owkkwa/image/upload/v1760687036/Familyfund/Dise%C3%B1o_sin_t%C3%ADtulo-removebg-preview_vqqzhb.png");
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


    //Convertir Usuario a UsuarioDTO
    public UsuarioDto toDto(Usuario usuario) {
    if (usuario == null) return null;
    UsuarioDto dto = new UsuarioDto();
    dto.setId(usuario.getId());
    dto.setNombre(usuario.getNombre());
    dto.setEmail(usuario.getEmail());
    dto.setFamily(usuario.getFamily());
    dto.setPhotoUrl(usuario.getPhotoUrl());
    return dto;
}

}
