package com.familyfund.backend.dto;

import com.familyfund.backend.modelo.Goal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoalResponse {
    private Long id;
    private String name;
    private Double amount;
    private Boolean achieved;
    private String month;
    private Long categoryId;
    private String categoryName;

    // Constructor
    public GoalResponse(Goal goal) {
        this.id = goal.getId();
        this.name = goal.getName();
        this.amount = goal.getAmount();
        this.achieved = goal.getAchieved();
        this.month = goal.getMonth();
        this.categoryId = goal.getCategory().getId();
        this.categoryName = goal.getCategory().getName();
    }
}
