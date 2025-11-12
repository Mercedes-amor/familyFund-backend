package com.familyfund.backend.dto;

import java.util.List;

public record MaxiGoalResponse(
        Long id,
        String name,
        Double targetAmount,
        Double actualSave,
        Boolean achieved,
        List<MaxiGoalSavingResponse> savings
) {}

