package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Budget;
import lombok.Data;

@Data
public class AddBudgetResponse extends BaseResponse{
    private Budget budget;

    public AddBudgetResponse(Status status, String message) {
        super(status, message);
    }

    public AddBudgetResponse(Status status, String message, Budget budget) {
        super(status, message);
        this.budget = budget;
    }
}
