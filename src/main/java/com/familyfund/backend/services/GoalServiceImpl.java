package com.familyfund.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.modelo.Goal;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.GoalRepository;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    FamilyRepository familyRepository;

    // Obtener todos los objetivos de una familia
    public List<Goal> getAllGoalsByFamily(Long familyId) {
        return goalRepository.findByFamilyId(familyId);
    }

    // Obtener objetivos de un mes específico (YYYY-MM) y una familia
    public List<Goal> getGoalsByMonth(Long familyId, String month) {
    return goalRepository.findByFamilyId(familyId)
            .stream()
            .filter(g -> month.equals(g.getMonth()))
            .collect(Collectors.toList());
}

    // Crear nuevo objetivo
    public Goal createGoal(Goal goal) {
        // Asegurarse de que achieved sea false al crear
        goal.setAchieved(false);
        return goalRepository.save(goal);
    }

    // Actualizar objetivo existente
    public Goal updateGoal(Long goalId, Goal updatedGoal) {
        return goalRepository.findById(goalId)
                .map(goal -> {
                    goal.setName(updatedGoal.getName());
                    goal.setAmount(updatedGoal.getAmount());
                    goal.setCategory(updatedGoal.getCategory());
                    goal.setMonth(updatedGoal.getMonth());
                    goal.setFamily(updatedGoal.getFamily());
                    goal.setAchieved(updatedGoal.getAchieved());
                    return goalRepository.save(goal);
                })
                .orElseThrow(() -> new RuntimeException("Goal not found with id " + goalId));
    }

    // Borrar objetivo
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    // Marcar objetivos conseguidos al final del mes
    public void evaluateGoals(Long familyId, String month) {
        List<Goal> goalsThisMonth = goalRepository.findByFamilyId(familyId)
                .stream()
                .filter(g -> month.equals(g.getMonth()))
                .collect(Collectors.toList());

        for (Goal g : goalsThisMonth) {
            double spent = categoryService.getTotalSpentInMonth(g.getCategory().getId(), month);
            g.setAchieved((g.getCategory().getLimit() - spent) >= g.getAmount());
            goalRepository.save(g);
        }

    }
}
