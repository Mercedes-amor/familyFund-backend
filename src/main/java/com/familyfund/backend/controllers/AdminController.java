package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.FamilyUpdateRequest;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.services.CategoryService;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.TransactionService;

//Rutas de gestión aplicación, protegidas para Admin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // @Autowired
    // private FamilyService familyService;
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
    // @Autowired
    // private TransactionRepository transactionRepository;

    // ===================== USUARIOS =====================

    // OBTENER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
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
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setEmail(usuarioActualizado.getEmail());
                    usuario.setPassword(usuarioActualizado.getPassword());
                    usuario.setRol(usuarioActualizado.getRol());
                    usuario.setFamily(usuarioActualizado.getFamily());
                    Usuario guardado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(guardado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // BORRAR USUARIO
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
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
        return familyRepository.findById(id)
                .map(familia -> {
                    familyRepository.delete(familia);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
