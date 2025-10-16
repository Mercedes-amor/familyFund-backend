package com.familyfund.backend.dto;

import com.familyfund.backend.modelo.Family;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class JwtResponseDto {
  private String accessToken;
  private String tokenType;     // "Bearer"
  private Long id;
  private String nombre;
  private String email;
  private Family family;
  private String photoUrl;
  private String rol;
  
}

