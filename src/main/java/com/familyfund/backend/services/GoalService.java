package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.GoalRequest;
import com.familyfund.backend.dto.GoalResponse;
import com.familyfund.backend.modelo.Goal;

public interface GoalService {
    public List<Goal> getAllGoalsByFamily(Long familyId);

    public List<Goal> getGoalsByMonth(Long familyId, String month);

    public Goal createGoal(Goal goal);

    public Goal updateGoal(Long id, Goal updatedGoal);

    public Goal updateGoalFromRequest(Long goalId, GoalRequest request);

    public void deleteGoal(Long id);

    public void evaluateGoal(Goal goal);

    public void evaluateAllGoals();

    public GoalResponse createGoalFromDTO(GoalRequest dto);

    public void evaluateAllFamiliesForMonth(String month);
}
