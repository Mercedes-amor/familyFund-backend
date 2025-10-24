package com.familyfund.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.FamilyUpdateRequest;
import com.familyfund.backend.dto.UsuarioResponseAdmin;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.services.FamilyService;

//Rutas de gestión aplicación, protegidas para Admin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private FamilyService familyService;
    // @Autowired
    // private CategoryService categoryService;
    // @Autowired
    // private TransactionService transactionService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FamilyRepository familyRepository;
    // @Autowired
    // private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // ===================== USUARIOS =====================

    // OBTENER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public List<UsuarioResponseAdmin> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseAdmin(
                        u.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getEdad(),
                        u.getEmail(),
                        u.getRol(),
                        u.getPhotoUrl(),
                        u.getFamily() != null ? u.getFamily().getId() : null))
                .collect(Collectors.toList());
    }

    // OBTENER UN USUARIO POR ID
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // EDITAR UN USUARIO
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseAdmin> editarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioResponseAdmin dto) {

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(dto.getNombre());
                    usuario.setApellido(dto.getApellido());
                    usuario.setEdad(dto.getEdad());
                    usuario.setEmail(dto.getEmail());
                    usuario.setRol(dto.getRol());

                    if (dto.getFamilyId() != null) {
                        Family family = familyRepository.findById(dto.getFamilyId())
                                .orElse(null);
                        usuario.setFamily(family);
                    }

                    Usuario guardado = usuarioRepository.save(usuario);

                    UsuarioResponseAdmin response = new UsuarioResponseAdmin(
                            guardado.getId(),
                            guardado.getNombre(),
                            guardado.getApellido(),
                            guardado.getEdad() != null ? guardado.getEdad() : 0,
                            guardado.getEmail(),
                            guardado.getRol(),
                            guardado.getPhotoUrl(),
                            guardado.getFamily() != null ? guardado.getFamily().getId() : null);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // BORRAR USUARIO
    @DeleteMapping("/usuarios/{id}")
    @Transactional
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = optUsuario.get();

        // Ponemos user_id a null en sus transacciones
        transactionRepository.setUsuarioNull(usuario);

        // Borramos usuario
        usuarioRepository.delete(usuario);

        return ResponseEntity.ok("Usuario borrado correctamente");
    }

    // ===================== FAMILIAS =====================

    // OBTENER TODAS LAS FAMILIAS
    @GetMapping("/familias")
    public List<Family> listarFamilias() {
        return familyRepository.findAll();
    }

    // OBTENER UNA FAMILIA POR ID
    @GetMapping("/familias/{id}")
    public ResponseEntity<Family> obtenerFamilia(@PathVariable Long id) {
        return familyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // EDITAR UNA FAMILIA
    @PutMapping("/familias/{id}")
    public ResponseEntity<Family> editarFamilia(@PathVariable Long id,
            @RequestBody FamilyUpdateRequest updateRequest) {
        return familyRepository.findById(id)
                .map(familia -> {
                    familia.setName(updateRequest.getName());
                    Family guardada = familyRepository.save(familia);
                    return ResponseEntity.ok(guardada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // BORRAR FAMILIA
    @DeleteMapping("/familias/{id}")
    public ResponseEntity<Void> borrarFamilia(@PathVariable Long id) {
        if (familyRepository.existsById(id)) {
            familyService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
