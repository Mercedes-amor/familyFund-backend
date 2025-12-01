package com.familyfund.backend.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Goal name cannot be empty")
    private String name;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private boolean evaluated; //si se ha evaluado
    private Boolean achieved; // si se ha logrado el objetivo
    private String month; // mes objetivo en formato YYYY-MM

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    @JsonBackReference
    private Family family;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;
}

