package com.familyfund.backend.modelo;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
// @SQLDelete(...): cuando llamemos a categoryRepository.delete(category), no se
// eliminará físicamente
// Se ejecutará un UPDATE estableciendo deleted = true, en SQL 1 = TRUE/ 0 = FALSE.
@SQLDelete(sql = "UPDATE category SET deleted = 1 WHERE id = ?")
// Las consultas automáticas como findAll no tendrán en cuenta las categorías
// deleted=true;
@SQLRestriction("deleted = 0")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name cannot be empty")
    private String name;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double limit;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) // quitamos orphanRemoval al pasar a soft-delete
    private List<Transaction> transactions;

    // Para el soft-delete
    @Builder.Default
    private boolean deleted = false;

}
