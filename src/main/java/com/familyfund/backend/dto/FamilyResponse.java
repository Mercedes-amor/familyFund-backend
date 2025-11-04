package com.familyfund.backend.dto;

import java.util.List;

public record FamilyResponse(
        Long id,
        String name,
        List<CategoryResponse> categories,
        MaxiGoalResponse maxiGoal
) {}
