package com.familyfund.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinFamilyRequest {
    private Long userId;
    private String familyCode;
}

