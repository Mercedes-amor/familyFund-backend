package com.familyfund.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Usuario")
@Data //Tenemos constructor por defecto, getters/setters, toString(), equals() y hashCode()
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido1;

    @Column(length = 50)
    private String apellido2;

    private Integer edad;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String tlf;
}
