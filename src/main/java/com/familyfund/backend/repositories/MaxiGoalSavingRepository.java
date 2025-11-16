package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.familyfund.backend.modelo.MaxiGoalSaving;

public interface MaxiGoalSavingRepository extends JpaRepository<MaxiGoalSaving, Long> {

    List<MaxiGoalSaving> findByMaxiGoalId(Long maxiGoalId);
    List<MaxiGoalSaving> findByMaxiGoal_Family_Id(Long familyId);

    @Query("SELECT s FROM MaxiGoalSaving s JOIN s.maxiGoal m WHERE m.family.id = :familyId ORDER BY s.createdAt DESC")
    List<MaxiGoalSaving> findAllByFamilyIdOrderByCreatedAtDesc(@Param("familyId") Long familyId);


}
