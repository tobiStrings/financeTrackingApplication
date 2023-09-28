package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Budget;
import lombok.Data;

@Data
public class FetchSingleBudgetResponse extends BaseResponse{
    private Budget budget;

    public FetchSingleBudgetResponse(Status status, String message) {
        super(status, message);
    }

    public FetchSingleBudgetResponse(Status status, String message, Budget budget) {
        super(status, message);
        this.budget = budget;
    }
}
