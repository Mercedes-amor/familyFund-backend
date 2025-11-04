package com.familyfund.backend.modelo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaxiGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull(message = "Target amount cannot be null")
    private Double targetAmount;// objetivo

    @NotNull(message = "Saved amount cannot be null")
    private Double actualSave; // dinero acumulado

    private Boolean achieved; // se actualiza desde servicio

    @NotNull(message = "Month cannot be null")
    private LocalDate achievedDate; // almacenar como YYYY-MM-01

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    @JsonBackReference
    private Family family;

}
