package com.familyfund.backend.services;

import java.util.List;

import com.familyfund.backend.dto.UpdateMaxiGoalRequest;
import com.familyfund.backend.modelo.MaxiGoal;

public interface MaxiGoalService {

    public MaxiGoal addSaving(Long maxiGoalId, Double amount);

    public List<MaxiGoal> getAllGoalsByFamily(Long familyId);

    public MaxiGoal getGoal(Long id);

    public MaxiGoal create(Long familyId, MaxiGoal maxiGoal);

    public MaxiGoal updateMaxiGoal(Long id, UpdateMaxiGoalRequest dto);
}
