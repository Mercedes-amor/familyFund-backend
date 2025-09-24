package com.familyfund.backend.services;

import org.springframework.stereotype.Service;

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

    //Crear y guardar nuevo Usuario
    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    //Mostrar todos los usuarios
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    //Buscar por id
    @Override
    public Usuario findById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null);
    }

     //Buscar por email
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Método que devuelve todos los usuarios de una familia
    public List<Usuario> findByFamilyId(Long familyId) {
        return usuarioRepository.findByFamily_Id(familyId);
    }
    
    //Borrar por id
    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

   
}
