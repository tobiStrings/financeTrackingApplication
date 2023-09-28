package com.financeTracker.financeTracker.data.dtos;

import lombok.Data;

@Data
public class AddBudgetRequest {
    private String budgetCategory;
    private double monthlyBudget;
    private double weeklyBudget;
}
