package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.familyfund.backend.dto.GoalRequest;
import com.familyfund.backend.dto.GoalResponse;
import com.familyfund.backend.dto.GoalUpdateRequest;
import com.familyfund.backend.modelo.Goal;
import com.familyfund.backend.repositories.GoalRepository;
import com.familyfund.backend.services.GoalService;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalRepository goalRepository;

    // @Autowired
    // private CategoryRepository categoryRepository;

    // @Autowired
    // private FamilyRepository familyRepository;

    // Listar todos los objetivos de una familia
    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<GoalResponse>> getGoalsByFamily(@PathVariable Long familyId) {
        List<Goal> goals = goalService.getAllGoalsByFamily(familyId);
        List<GoalResponse> dtoList = goals.stream()
                .map(GoalResponse::new)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    // Listar objetivos de un mes específico (YYYY-MM) de una familia
    @GetMapping("/family/{familyId}/month/{month}")
    public ResponseEntity<List<GoalResponse>> getGoalsByMonth(
            @PathVariable Long familyId,
            @PathVariable String month) {
        List<Goal> goals = goalService.getGoalsByMonth(familyId, month);
        List<GoalResponse> dtoList = goals.stream()
                .map(GoalResponse::new)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    // Crear nuevo objetivo
    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalRequest dto) {
        GoalResponse created = goalService.createGoalFromDTO(dto);
        return ResponseEntity.ok(created);
    }

    // Actualizar objetivo existente
    @PutMapping("/{goalId}")
    public ResponseEntity<Goal> updateGoal(@PathVariable("goalId") Long goalId,
            @RequestBody GoalUpdateRequest dto) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        goal.setName(dto.getName());
        goal.setAmount(dto.getAmount());

        goalRepository.save(goal);
        return ResponseEntity.ok(goal);
    }

    // Borrar objetivo
    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }
}
