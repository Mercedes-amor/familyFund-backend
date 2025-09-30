package com.familyfund.backend.dto;

import lombok.Data;

@Data
public class GoalRequest {
    private String name;
    private Double amount;
    private Long familyId;
    private Long categoryId;
    private String month;
    private Boolean achieved;
}
