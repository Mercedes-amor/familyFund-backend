package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.modelo.Goal;
import com.familyfund.backend.services.GoalService;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    // Listar todos los objetivos
    @GetMapping("/family/{familyId}")
    public List<Goal> getGoalsByFamily(@PathVariable Long familyId) {
        return goalService.getAllGoalsByFamily(familyId);
    }

    // Listar objetivos de un mes específico (YYYY-MM) de una familia
    @GetMapping("/family/{familyId}/month/{month}")
    public List<Goal> getGoalsByMonth(
            @PathVariable Long familyId,
            @PathVariable String month) {
        return goalService.getGoalsByMonth(familyId, month);
    }

    // Crear nuevo objetivo
    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    // Actualizar objetivo existente
    @PutMapping("/{goalId}")
    public Goal updateGoal(@PathVariable Long goalId, @RequestBody Goal goal) {
        return goalService.updateGoal(goalId, goal);
    }

    // Borrar objetivo
    @DeleteMapping("/{goalId}")
    public void deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
    }
}
