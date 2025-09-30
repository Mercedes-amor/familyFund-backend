package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.modelo.Goal;

public interface GoalService {
    public List<Goal> getAllGoalsByFamily(Long familyId);

    public List<Goal> getGoalsByMonth(Long familyId, String month);

    public Goal createGoal(Goal goal);

    public Goal updateGoal(Long id, Goal updatedGoal);

    public void deleteGoal(Long id);

    public void evaluateGoals(Long familyId, String month);
}
