package com.familyfund.backend.dto;

public record MaxiGoalResponse(
        Long id,
        String name,
        Double targetAmount,
        Double actualSave,
        Boolean achieved
) {}

