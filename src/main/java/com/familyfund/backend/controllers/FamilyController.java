package com.familyfund.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.dto.JoinFamilyRequest;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.services.CategoryService;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.UsuarioService;

@RestController
@RequestMapping("/api/families")
public class FamilyController {

    @Autowired
    FamilyService familyService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    CategoryService categoryService;

    // *CREAR FAMILIA*
    @PostMapping("/newfamily")
    public ResponseEntity<?> createFamily(@RequestBody Family family, @RequestParam Long userId) {
        if (family.getName() == null || family.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El nombre es obligatorio"));
        }

        Usuario usuario = usuarioService.findById(userId);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuario no encontrado"));
        }

        family.setUsuarios(List.of(usuario));
        usuario.setFamily(family);

        familyService.save(family);
        usuarioService.save(usuario);

        // Devolvemos DTO plano
        return ResponseEntity.ok(new FamilyResponse(family.getId(), family.getName(), List.of()));
    }

    // AÑADIR USUARIO A UNA FAMILIA
    @PostMapping("/join")
    public ResponseEntity<?> joinFamily(@RequestBody JoinFamilyRequest request) {
        Usuario usuario = usuarioService.findById(request.getUserId());
        Family family = familyService.findById(request.getFamilyId());

        if (usuario == null || family == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuario o familia no encontrada"));
        }

        usuario.setFamily(family);
        usuarioService.save(usuario);

        return ResponseEntity.ok(new MemberResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail()));
    }

    // OBTENER UNA FAMILIA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyResponse> getFamilyById(@PathVariable Long id) {
        Optional<Family> familyOpt = familyService.findOptionalById(id);
        if (familyOpt.isEmpty()) return ResponseEntity.notFound().build();

        Family family = familyOpt.get();
        // Transformar categorías a DTO
        List<CategoryResponse> categories = family.getCategories() != null ?
                family.getCategories().stream()
                    .map(c -> new CategoryResponse(c.getId(), c.getName()))
                    .collect(Collectors.toList())
                : List.of();

        FamilyResponse response = new FamilyResponse(family.getId(), family.getName(), categories);
        return ResponseEntity.ok(response);
    }

    // OBTENER LOS MIEMBROS DE UNA FAMILIA
    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberResponse>> getFamilyMembers(@PathVariable Long id) {
        List<Usuario> usuarios = usuarioService.findByFamilyId(id);
        List<MemberResponse> members = usuarios.stream()
                .map(u -> new MemberResponse(u.getId(), u.getNombre(), u.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    // OBTENER LAS CATEGORÍAS DE UNA FAMILIA
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryResponse>> getFamilyCategories(@PathVariable Long id) {
        List<Category> categories = categoryService.findByFamilyId(id);
        List<CategoryResponse> res = categories.stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }
}
