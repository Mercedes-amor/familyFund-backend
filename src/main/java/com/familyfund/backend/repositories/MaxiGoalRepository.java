package com.familyfund.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.familyfund.backend.modelo.MaxiGoal;

public interface MaxiGoalRepository extends JpaRepository<MaxiGoal, Long> {

    // Obtener litado de maxiGoals de una familia
    List<MaxiGoal> findByFamilyId(Long familyId);

    // Comprobar si existe un maxiGoal activo en la familia
    boolean existsByFamilyIdAndAchievedFalse(Long familyId);

    // Obtener el MaxiGoal activo de una familia
    Optional<MaxiGoal> findByFamilyIdAndAchievedFalse(Long familyId);

    // Obtener todos los maxigoals activos de las familias
    List<MaxiGoal> findAllByAchievedFalse();

    // Obtener todos los id de maxigoals activos de las familias
    @Query("SELECT g.family.id FROM MaxiGoal g WHERE g.achieved = false")
    List<Long> findAllActiveFamilyIds();

}
