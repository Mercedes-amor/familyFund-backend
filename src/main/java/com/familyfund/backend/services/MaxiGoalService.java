package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.MaxiGoalSavingResponse;
import com.familyfund.backend.dto.UpdateMaxiGoalRequest;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;

public interface MaxiGoalService {

    public MaxiGoal addSaving(Long maxiGoalId, Double amount);

    public MaxiGoalSaving createSystemSaving(Long maxiGoalId, double amount);

    public List<MaxiGoal> getAllGoalsByFamily(Long familyId);

    public MaxiGoal getGoal(Long id);

    public List<MaxiGoalSaving> getAllSavings(Long id);

    public List<MaxiGoalSaving> getAllSavingsbyFamily(Long familyId);

    public MaxiGoalSavingResponse toResponse(MaxiGoalSaving saving);

    public MaxiGoal create(Long familyId, MaxiGoal maxiGoal);

    public MaxiGoal updateMaxiGoal(Long id, UpdateMaxiGoalRequest dto);

    public void addRemainingToMaxiGoal();
}
