package com.familyfund.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.dto.GoalRequest;
import com.familyfund.backend.dto.GoalResponse;
import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Goal;
import com.familyfund.backend.repositories.CategoryRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;

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

    // Crear un Goal a partir de un DTO
    public GoalResponse createGoalFromDTO(GoalRequest dto) {
        // Buscar familia
        Family family = familyRepository.findById(dto.getFamilyId())
                .orElseThrow(() -> new RuntimeException("Family not found"));

        // Buscar categoría
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Crear Goal
        Goal goal = Goal.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .achieved(dto.getAchieved() != null ? dto.getAchieved() : false)
                .month(dto.getMonth())
                .family(family)
                .category(category)
                .build();

        Goal savedGoal = goalRepository.save(goal);

        // Devolver DTO de respuesta
        GoalResponse response = new GoalResponse();
        response.setId(savedGoal.getId());
        response.setName(savedGoal.getName());
        response.setAmount(savedGoal.getAmount());
        response.setAchieved(savedGoal.getAchieved());
        response.setMonth(savedGoal.getMonth());
        response.setCategoryName(savedGoal.getCategory().getName());

        return response;

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

    // Actualizar desde DTO
    public Goal updateGoalFromRequest(Long goalId, GoalRequest request) {
        return goalRepository.findById(goalId)
                .map(goal -> {
                    goal.setName(request.getName());
                    goal.setAmount(request.getAmount());
                    goal.setMonth(request.getMonth());
                    goal.setAchieved(request.getAchieved());

                    // Asociar categoría
                    Category category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    goal.setCategory(category);

                    // Asociar familia
                    Family family = familyRepository.findById(request.getFamilyId())
                            .orElseThrow(() -> new RuntimeException("Family not found"));
                    goal.setFamily(family);

                    return goalRepository.save(goal);
                })
                .orElseThrow(() -> new RuntimeException("Goal not found with id " + goalId));
    }

    // Borrar objetivo
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    // ---- VERIFICAR OBJETIVOS CUMPLIDOS ----//

    // Evaluar un goal concreto en su mes
    public void evaluateGoal(Goal goal) {
        String month = goal.getMonth(); // formato YYYY-MM de su propio mes
        double spent = categoryService.getTotalSpentInMonth(goal.getCategory().getId(), month);
        goal.setAchieved((goal.getCategory().getLimit() - spent) >= goal.getAmount());
    }

    // Evaluar todos los goals de una familia en un mes concreto
    public void evaluateGoals(Long familyId, String month) {
        List<Goal> goalsThisMonth = goalRepository.findByFamilyId(familyId)
                .stream()
                .filter(g -> month.equals(g.getMonth()))
                .collect(Collectors.toList());

        for (Goal g : goalsThisMonth) {
            evaluateGoal(g);
            goalRepository.save(g);
        }
    }

    // Evaluar todos los goals de todas las familias de meses pasados y del actual
    public void evaluateAllGoals() {
        List<Goal> allGoals = goalRepository.findAll();

        for (Goal g : allGoals) {
            evaluateGoal(g);
            goalRepository.save(g);
        }
    }

    // Al cierre de cada mes con Scheduler
    // Evaluar todas las familias automáticamente
    public void evaluateAllFamiliesForMonth(String month) {
        List<Family> families = familyRepository.findAll();
        for (Family family : families) {
            evaluateGoals(family.getId(), month); // Método que calcula todos los objetivos de una familia y mes
        }
    }
}
