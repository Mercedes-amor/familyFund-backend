package com.familyfund.backend.dto;

import com.familyfund.backend.modelo.Rol;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseAdmin {
    private Long id;
    private String nombre;
    private String apellido;
    private int edad;
    private String email;
    private Rol rol;
    private Long familyId;
}