package com.familyfund.backend.controllers;

import java.util.List;

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
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Transaction;
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
    @GetMapping("/categories/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.findByCategoryId(id);
        List<TransactionResponse> response = transactions.stream()
                .map(tx -> new TransactionResponse(tx.getId(), tx.getName(), tx.getType(), tx.getDate(), tx.getAmount(), tx.getCategory().getId()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // EDITAR CATEGORÍA
    @PutMapping("edit/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails) {

        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.badRequest().body("Categoría no encontrada");
        }

        category.setName(categoryDetails.getName());
        Category updated = categoryService.save(category);
        return ResponseEntity.ok(updated);
    }

    // BORRAR CATEGORÍA
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.badRequest().body("Categoría no encontrada");
        }

        categoryService.deleteById(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}
