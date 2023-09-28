package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;

@Data
public class UpdateBudgetRequest {
    private double weeklyBudget;
    private double monthlyBudget;
}
