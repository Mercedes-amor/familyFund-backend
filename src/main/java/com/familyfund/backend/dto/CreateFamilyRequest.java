package com.familyfund.backend.dto;

import lombok.Data;

@Data
public class CreateFamilyRequest {
    private String name;
    private Double limit;
}
