package com.familyfund.backend.dto;

import java.time.LocalDate;

import com.familyfund.backend.modelo.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotBlank(message = "Movement name cannot be empty")
    private String name;

    @NotNull(message = "Type cannot be null")
    private TransactionType type;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;
}


