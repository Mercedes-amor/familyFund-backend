package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.familyfund.backend.modelo.Usuario;
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


    // Obtención inflacción España
    @GetMapping("worldbank/pib-per-capita")
    public ResponseEntity<String> getPibPerCapita() {
        String url = "https://api.worldbank.org/v2/country/ES/indicator/NY.GDP.PCAP.CD?format=json";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(result);
    }
}
