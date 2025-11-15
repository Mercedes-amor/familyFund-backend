package com.familyfund.backend.dto;

import java.time.LocalDate;

public record MaxiGoalSavingResponse(
        Long id,
        Double amount,
        LocalDate createAt,
        Long usuarioId, // null si es un saving de sistema
        String usuarioNombre,
        String photoUrl,
        boolean system

) {
}
