package com.familyfund.backend.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "Usuario")
@Data // Tenemos constructor por defecto, getters/setters, toString(), equals() y
      // hashCode()
@AllArgsConstructor
public class Usuario {

    public Usuario() {
        // constructor vacío obligatorio para JPA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false, length = 50)
    // private String userName;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = true, length = 100)
    private Integer edad;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Nos aseguramos de indicar a JPA que el enum persiste como String,
                                 // en la base de datos se guarda como Varchar
    @Column(nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonBackReference // <- Evita recursión infinta, no serializa hacia atrás
    private Family family;

}
