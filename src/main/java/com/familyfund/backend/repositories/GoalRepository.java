package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long> {
        boolean existsByFamilyIdAndNameIgnoreCase(Long familyId, String name);

        // Obtener todos los objetivos (Goal) de una familia
        List<Goal> findByFamilyId(Long familyId);

}
