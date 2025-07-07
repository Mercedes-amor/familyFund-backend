package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.domain.Usuario;
import com.familyfund.backend.repositories.UsuarioRepository;

@RestController
@RequestMapping("/api")

public class TestController {
    
    private final UsuarioRepository usuarioRepository;

    public TestController(UsuarioRepository usuarioRepository) {
            this.usuarioRepository = usuarioRepository;
        }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }


    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

}
