package com.familyfund.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.MaxiGoalRepository;

@Service
public class MaxiGoalServiceImpl implements MaxiGoalService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    MaxiGoalRepository maxiGoalRepository;

    @Autowired
    FamilyRepository familyRepository;

    // AÑADIR CANTIDAD AHORRADA
    @Transactional
    public MaxiGoal addSaving(Long maxiGoalId, Double amount) {

        MaxiGoal maxiGoal = maxiGoalRepository.findById(maxiGoalId)
                .orElseThrow(() -> new RuntimeException("MaxiGoal not found"));

        maxiGoal.setActualSave(maxiGoal.getActualSave() + amount);

        // Comprobamos si se alcanzó el objetivo, en tal caso creamos nuevo MaxiGoal
        if (maxiGoal.getActualSave() >= maxiGoal.getTargetAmount()) {
            maxiGoal.setAchieved(true);
            maxiGoalRepository.save(maxiGoal);

            MaxiGoal newGoal = new MaxiGoal();
            newGoal.setName("Nuevo objetivo"); // luego el usuario lo cambia si quiere
            newGoal.setTargetAmount(1000.0);
            newGoal.setActualSave(0.0);
            newGoal.setAchieved(false);
            newGoal.setFamily(maxiGoal.getFamily());

            return maxiGoalRepository.save(newGoal);
        }

        return maxiGoalRepository.save(maxiGoal);
    }

    // OBTENER TODOS LOS MAXIGOAL POR FAMILIA
    public List<MaxiGoal> getAllGoalsByFamily(Long familyId) {
        return maxiGoalRepository.findByFamilyId(familyId);
    }

    // OBTENER POR ID
    public MaxiGoal getGoal(Long id) {
        return maxiGoalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaxiGoal not found"));
    }

    // CREAR NUEVO MAXIGOAL
    public MaxiGoal create(Long familyId, MaxiGoal maxiGoal) {

        // Bloqueamos que se cree un nuevo MaxiGoal si la familia ya tiene uno activo
        if (maxiGoalRepository.existsByFamilyIdAndAchievedFalse(familyId)) {
            throw new IllegalStateException("Ya existe un maxiGoal activo para esta familia");
        }

        // Asociamos el MaxiGoal a la familia
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new RuntimeException("Family not found"));

        maxiGoal.setFamily(family);
        maxiGoal.setAchieved(false);
        maxiGoal.setTargetAmount(1000.0);
        maxiGoal.setActualSave(0.0);

        return maxiGoalRepository.save(maxiGoal);
    }

}
