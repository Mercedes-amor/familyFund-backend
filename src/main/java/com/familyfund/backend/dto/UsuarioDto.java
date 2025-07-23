package com.familyfund.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter 
@AllArgsConstructor
@Data
public class UsuarioDto {
    private Integer id;
    private String nombre;
    private String email;
}

