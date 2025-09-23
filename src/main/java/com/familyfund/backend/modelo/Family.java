package com.familyfund.backend.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Family name cannot be empty")
    private String name;

    @NotBlank(message = "Code cannot be empty")
    private String code; // para invitar usuarios a unirse a la familia

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // <- Evita recursión infinta, no serializa hacia atrás
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Category> categories;

    
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;
}
