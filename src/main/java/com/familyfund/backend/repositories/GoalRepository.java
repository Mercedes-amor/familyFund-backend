package com.familyfund.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familyfund.backend.modelo.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
