package com.familyfund.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.MaxiGoalSaving;

public interface MaxiGoalSavingRepository extends JpaRepository<MaxiGoalSaving, Long> {

    List<MaxiGoalSaving> findByMaxiGoalId(Long maxiGoalId);

}
