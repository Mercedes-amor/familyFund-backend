package com.familyfund.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.security.JwtUtils;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.UsuarioService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/families")
public class MainController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    FamilyService familyService;
    @Autowired
    UsuarioService usuarioService;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    //*CREAR FAMILIA */
    @PostMapping("/newfamily")
    public ResponseEntity<?> createFamily(@RequestBody Family family, @RequestParam Long userId) {
    if (family.getName() == null || family.getName().trim().isEmpty()) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "El nombre es obligatorio");
        return ResponseEntity.badRequest().body(error);
    }

    Usuario usuario = usuarioService.findById(userId);
    if (usuario == null) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Usuario no encontrado");
        return ResponseEntity.badRequest().body(error);
    }

    // Asociar el usuario a la familia
    family.setUsuarios(new ArrayList<>());
    family.getUsuarios().add(usuario);
    usuario.setFamily(family);  // mantener el lado inverso de la relación

    familyService.save(family);  // cascada guardará también la relación
    usuarioService.save(usuario); // opcional si no se usa cascade desde usuario

    return ResponseEntity.ok(family);
    }

}