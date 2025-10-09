package com.familyfund.backend.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
// Renombramos la tabla ya que Transaction es una palabra reservada en SQL
// Server y nos daba error en las consultas
@Table(name = "user_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movement name cannot be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    // Se genera automáticamente a partir de date
    private Integer month;
    private int year;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;



    @PrePersist // Debe calcularse antes de guardar nueva Transaction
    @PreUpdate // Debe calcularse antes de guardar un update de Transaction
    private void calculateMonthAndYear() {
        if (this.date != null) {
            this.month = this.date.getMonthValue();
            this.year = this.date.getYear();
        }
    }

}
