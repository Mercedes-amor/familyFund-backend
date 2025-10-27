package com.familyfund.backend.dto;

import java.time.LocalDate;
import com.familyfund.backend.modelo.TransactionType;
import com.familyfund.backend.modelo.Usuario;

public record TransactionResponse(
    Long id,
    String name,
    TransactionType type,
    LocalDate date,
    Double amount,
    Long categoryId,
    Usuario user
) {}

