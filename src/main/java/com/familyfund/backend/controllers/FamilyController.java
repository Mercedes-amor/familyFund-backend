package com.familyfund.backend.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.CategoryResponse;
import com.familyfund.backend.dto.CreateFamilyRequest;
import com.familyfund.backend.dto.FamilyResponse;
import com.familyfund.backend.dto.JoinFamilyRequest;
import com.familyfund.backend.dto.MaxiGoalSavingResponse;
import com.familyfund.backend.dto.MemberResponse;
import com.familyfund.backend.dto.TransactionResponse;
import com.familyfund.backend.dto.UpdateMaxiGoalRequest;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.TransactionType;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.CategoryRepository;
import com.familyfund.backend.repositories.TransactionRepository;
import com.familyfund.backend.security.UserDetailsImpl;
import com.familyfund.backend.services.CategoryService;
import com.familyfund.backend.services.FamilyService;
import com.familyfund.backend.services.MaxiGoalService;
import com.familyfund.backend.services.TransactionService;
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
    @Autowired
    MaxiGoalService maxiGoalService;

    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CategoryRepository categoryRepository;

    // *CREAR FAMILIA*
    @PostMapping("/newfamily")
    public ResponseEntity<FamilyResponse> createFamily(
            @RequestBody CreateFamilyRequest request,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Family createdFamily = familyService.createFamily(
                request.getName(),
                userDetails.getId());

        // Usamos el mapper existente para incluir categorías y maxiGoal
        FamilyResponse response = familyService.getFamilyById(createdFamily.getId());

        return ResponseEntity.ok(response);
    }

    // UNIRSE A UNA FAMILIA
    @PostMapping("/join")
    public ResponseEntity<FamilyResponse> joinFamily(@RequestBody JoinFamilyRequest request) {
        Usuario usuario = usuarioService.findById(request.getUserId());
        Family family = familyService.findById(request.getFamilyId());

        if (usuario == null || family == null) {
            return ResponseEntity.badRequest().build();
        }

        // Ligamos usuario a la familia
        usuario.setFamily(family);
        usuarioService.save(usuario);

        // Transformamos la familia a DTO
        FamilyResponse response = familyService.getFamilyById(family.getId());
        return ResponseEntity.ok(response);
    }

    // OBTENER UNA FAMILIA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyResponse> getFamilyById(@PathVariable Long id) {

        Optional<Family> familyOpt = familyService.findOptionalById(id);
        if (familyOpt.isEmpty())
            return ResponseEntity.notFound().build();

        FamilyResponse response = familyService.getFamilyById(id);

        return ResponseEntity.ok(response);
    }

    // OBTENER LOS MIEMBROS DE UNA FAMILIA
    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberResponse>> getFamilyMembers(@PathVariable Long id) {
        List<Usuario> usuarios = usuarioService.findByFamilyId(id);
        List<MemberResponse> members = usuarios.stream()
                .map(u -> new MemberResponse(u.getId(), u.getNombre(), u.getEmail(), u.getPhotoUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    // OBTENER LAS CATEGORÍAS ACTIVAS DE UNA FAMILIA
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryResponse>> getFamilyCategories(@PathVariable Long id) {
        List<Category> categories = categoryRepository.findByFamily_IdAndDeletedFalse(id);

        // Obtenemos el mes y año actual
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        // Calculamos la suma de ingresos solo del mes actual
        double totalIngresosMes = transactionRepository.sumByFamilyAndTypeAndMonth(
                id, TransactionType.INCOME, currentYear, currentMonth);

        // Actualizamos el límite dinámico para la categoría "INGRESOS"
        categories.forEach(cat -> {
            if ("INGRESOS".equalsIgnoreCase(cat.getName())) {
                cat.setLimit(totalIngresosMes);
            }
        });

        // Transformamos cada categoría a DTO
        List<CategoryResponse> res = categories.stream()
                .map(categoryService::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    // HISTÓRICO- OBTENER CATEGORÍAS DE UNA FAMILIA, INCLUIDAS BORRADAS
    @GetMapping("/{id}/categories/history")
    public ResponseEntity<List<CategoryResponse>> getCategoriesHistory(@PathVariable Long id) {

        List<Category> categories = categoryRepository.findAllIncludingDeletedWithTransactions(id);

        List<CategoryResponse> res = categories.stream()
                .map(categoryService::toResponse) // tu mapper debe incluir transacciones
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    // OBTENER LAS TRANSACCIONES DE UNA FAMILIA
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> findByFamily(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.findByFamilyId(id);

        List<TransactionResponse> res = transactions.stream()
                .map(c -> new TransactionResponse(c.getId(), c.getName(), c.getType(), c.getDate(), c.getAmount(),
                        c.getCategory().getId(), c.getUsuario()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    // MAXIGOALS - AHORRO
    // Crear nuevo MaxiGoal
    @PostMapping("/{familyId}/maxigoal")
    public ResponseEntity<MaxiGoal> createMaxiGoal(
            @PathVariable Long familyId,
            @RequestBody MaxiGoal maxiGoal) {
        return ResponseEntity.ok(maxiGoalService.create(familyId, maxiGoal));
    }

    // Actualizar un MaxiGoal existente
    @PutMapping("maxigoal/{id}")
    public ResponseEntity<MaxiGoal> updateMaxiGoal(
            @PathVariable Long id,
            @RequestBody UpdateMaxiGoalRequest dto) {

        MaxiGoal saved = maxiGoalService.updateMaxiGoal(id, dto);
        return ResponseEntity.ok(saved);
    }

    // ---SAVINGS DE MAXIGOAL--

    // Obtener Savings de MaxiGoal
    @GetMapping("/maxigoal/{maxiGoalId}/savings")
    public ResponseEntity<List<MaxiGoalSavingResponse>> getAllSavings(
            @PathVariable Long maxiGoalId) {

        try {
            List<MaxiGoalSaving> savings = maxiGoalService.getAllSavings(maxiGoalId);

            // Convertir a Response
            List<MaxiGoalSavingResponse> response = savings.stream()
                    .map(maxiGoalService::toResponse)
                    .toList();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Añadir saving por usuario
    @PostMapping("maxigoal/{id}/add-saving")
    public ResponseEntity<?> addSaving(
            @PathVariable Long id,
            @RequestParam Double amount,
            Authentication authentication) {

        maxiGoalService.addSaving(id, amount);
        return ResponseEntity.ok("Saving added");
    }

    // Añadir saving automático (de Sistema)
    @PostMapping("/maxigoal/{maxiGoalId}/system-saving")
    public ResponseEntity<MaxiGoalSavingResponse> addSystemSaving(
            @PathVariable Long maxiGoalId,
            @RequestBody Map<String, Object> body) {

        double amount = ((Number) body.get("amount")).doubleValue();

        try {
            MaxiGoalSaving saving = maxiGoalService.createSystemSaving(maxiGoalId, amount);
            MaxiGoalSavingResponse response = maxiGoalService.toResponse(saving);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
