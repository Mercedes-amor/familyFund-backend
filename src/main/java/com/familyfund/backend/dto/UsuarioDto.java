package com.familyfund.backend.dto;

import com.familyfund.backend.modelo.Family;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter 
@AllArgsConstructor
@Data
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String email;
    private Family family;
}

