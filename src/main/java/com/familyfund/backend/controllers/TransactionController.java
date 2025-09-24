package com.familyfund.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.TransactionRequest;
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.security.JwtUtils;
import com.familyfund.backend.services.CategoryService;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.TransactionService;
import com.familyfund.backend.services.UsuarioService;

//Para manejar CORS
// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

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
    CategoryService categoryService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    // CREAR TRANSACCIÓN Y AÑADIRLO A LA CATEGORÍA
    @PostMapping("/new/{categoryId}")
    public ResponseEntity<?> createTransaction(
            @PathVariable Long categoryId,
            @RequestBody TransactionRequest request) {

        System.out.println("POST /new/" + categoryId + " recibido. Request: " + request);

        try {
            TransactionResponse transaction = transactionService.createTransaction(categoryId, request);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "details", e.getMessage()));
        }
    }

    // public ResponseEntity<Transaction> createTransaction(
    // @PathVariable Long categoryId,
    // @RequestBody TransactionRequest request) {

    // try {
    // //El usuario se obtiene en el servicio por AuthenticationMannager
    // Transaction transaction = transactionService.createTransaction(categoryId,
    // request);
    // return ResponseEntity.ok(transaction);
    // } catch (RuntimeException e) {
    // // Puedes personalizar la respuesta si quieres
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    // }
    // }

    // EDITAR TRANSACCIÓN

    // BORRAR TRANSACCIÓN

    // OBTENER LAS TRANSACCIONES DEL MES ACTUAL
    // @GetMapping("/{familyId}/transactions")
    // public ResponseEntity<?> getTransactionsByFamilyAndMonth(
    // @PathVariable Long familyId,
    // @RequestParam int year,
    // @RequestParam int month) {

    // List<Transaction> transactions =
    // transactionService.findByFamilyAndMonth(familyId, year, month);
    // return ResponseEntity.ok(transactions);
    // }

}