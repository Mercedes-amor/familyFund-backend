package com.familyfund.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter 
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private Double limit;
    private Double totalSpent;   // suma de transacciones de la categoria
    private Double remaining;    // limit - totalSpent
    private Double percentage;   // totalSpent / limit * 100
    private boolean deleted;
}


