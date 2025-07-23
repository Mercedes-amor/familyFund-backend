package com.familyfund.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data //Tenemos constructor por defecto, getters/setters, toString(), equals() y hashCode()
@AllArgsConstructor
public class SignupDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String nombre;

    @NotBlank
    @Size(min = 3, max = 20)
    private String apellido;

    private Integer edad;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;

    private String rol;



}
