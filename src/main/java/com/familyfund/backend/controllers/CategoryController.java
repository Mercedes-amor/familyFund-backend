package com.familyfund.backend.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    // OBTENER DTO DE UNA CATEGORÍA
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        CategoryResponse response = categoryService.toResponse(category);
        return ResponseEntity.ok(response);
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
            @RequestBody Map<String, Object> body) {
        // Extraemos el nuevo nombre de la categoría del body
        String newName = (String) body.get("name");

        // Extraemos el nuevo límite (puede no venir, de ahí Object)
        Object limitObj = body.get("limit");

        // Validamos que el nombre exista y no esté en blanco
        if (newName == null || newName.isBlank()) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }

        Double newLimit = null;
        if (limitObj != null) { // Si nos envían un límite, lo convertimos a Double
            try {
                newLimit = Double.parseDouble(limitObj.toString());

                // Validamos que el límite sea positivo
                if (newLimit < 0)
                    return ResponseEntity.badRequest().body(null);
            } catch (NumberFormatException e) {
                // Si no se puede convertir a número, devolvemos 400
                return ResponseEntity.badRequest().body(null);
            }
        }

        // Buscamos la categoría en base a su id
        Category category = categoryService.findById(id);
        if (category == null) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        // Actualizamos los campos de la categoría
        category.setName(newName);
        category.setLimit(newLimit);

        // Guardamos la categoría actualizada
        Category saved = categoryService.save(category);

        // Devolvemos la categoría transformada a DTO con totalSpent, remaining y
        // percentage

        return ResponseEntity.ok(categoryService.toResponse(saved));
    }

    // SOFT-DELETED DE CATEGORÍA
    // En realidad es un UPDATE de CATEGORIA dónde deleted = true;
    @DeleteMapping("delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Solo transacciones del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate inicioSiguienteMes = inicioMes.plusMonths(1);

        transactionRepository.deleteByCategoryIdAndDateBetween(id, inicioMes, inicioSiguienteMes);

        // Soft delete de la categoría
        categoryRepository.delete(category);

        return ResponseEntity.ok(Map.of("message", "Categoría borrada correctamente"));
    }

}