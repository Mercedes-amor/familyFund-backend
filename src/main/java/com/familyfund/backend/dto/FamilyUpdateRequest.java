package com.familyfund.backend.dto;
import lombok.Data;

@Data
public class FamilyUpdateRequest {
    private String name;

    // Constructor vacío
    public FamilyUpdateRequest() {}

    // Constructor con parámetros
    public FamilyUpdateRequest(String name) {
        this.name = name;
    }

}

