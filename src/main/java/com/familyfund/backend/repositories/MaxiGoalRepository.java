package com.familyfund.backend.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.MaxiGoal;

public interface MaxiGoalRepository extends JpaRepository<MaxiGoal, Long> {
        
    List<MaxiGoal> findByFamilyId(Long familyId);

    //Comprobar si existe un maxiGoal activo en la familia
    boolean existsByFamilyIdAndAchievedFalse(Long familyId);


}
