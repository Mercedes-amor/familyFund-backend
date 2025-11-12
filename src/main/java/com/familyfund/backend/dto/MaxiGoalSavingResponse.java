package com.familyfund.backend.dto;

import java.time.LocalDate;

public record MaxiGoalSavingResponse(
        Long id,
        Double amount,
        LocalDate createAt
) {}


