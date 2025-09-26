package com.familyfund.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.CategoryRequest;
import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.services.CategoryService;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.TransactionService;

//Para manejar CORS
// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    FamilyService familyService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    // NUEVA CATEGORÍA DE UNA FAMILIA
    @PostMapping("/newCategory/{familyId}")
    // <?> Cuerpor de cualquier tipo
    public ResponseEntity<?> createCategory(
            // Obtenemos el id de la familia por pathVariable
            @PathVariable Long familyId,
            // Creamos instancia de CategoryRequest con los datos que llegan del frontend
            @RequestBody CategoryRequest request) {
        try {
            Category saved = categoryService.createCategory(familyId, request);
            return ResponseEntity.ok(saved);
            // Capturamos el error, ej: Familia no encontrada
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // OBTENER LAS CATEGORÍAS DE UNA FAMILIA
    @GetMapping("/list/{familyId}")
    public ResponseEntity<?> getCategoriesByFamily(@PathVariable Long familyId) {
        List<Category> categories = categoryService.findByFamilyId(familyId);
        return ResponseEntity.ok(categories);
    }

    // OBTENER LAS TRANSACCIONES DE UNA CATEGORÍA
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.findByCategoryId(id);
        List<TransactionResponse> response = transactions.stream()
                .map(tx -> new TransactionResponse(tx.getId(), tx.getName(), tx.getType(), tx.getDate(), tx.getAmount(),
                        tx.getCategory().getId()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // EDITAR CATEGORÍA
    @PutMapping("edit/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newName = body.get("name");
        if (newName == null || newName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        category.setName(newName);
        Category saved = categoryRepository.save(category);

        return ResponseEntity.ok(new CategoryResponse(saved.getId(), saved.getName()));
    }

    

    // BORRAR CATEGORÍA Y TODAS LAS TRANSACCIONES
    // DELETE /api/categories/{id}
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Primero borramos todas las transacciones asociadas
        transactionRepository.deleteAllByCategoryId(category.getId());

        // Ahora borramos la categoría del repositorio
        categoryRepository.delete(category);

        return ResponseEntity.ok(Map.of("message", "Categoría borrada correctamente"));
    }
}