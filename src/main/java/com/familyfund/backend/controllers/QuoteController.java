package com.familyfund.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController                      
@RequestMapping("/api/quotes")        
public class QuoteController {

    @GetMapping("/random")            
    public ResponseEntity<String> getRandomQuote() {
        // RestTemplate es un cliente HTTP que permite hacer llamadas a APIs externas
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://zenquotes.io/api/random";

        try {
            // Hacemos una petición GET a la API externa
            // getForObject devuelve la respuesta en el tipo indicado (String)
            String response = restTemplate.getForObject(url, String.class);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // devolvemos un JSON con un mensaje de error y status 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"No se pudo obtener la cita\"}");
        }
    }
}

