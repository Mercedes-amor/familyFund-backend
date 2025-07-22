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
    private String userName;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    private Integer edad;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

}
