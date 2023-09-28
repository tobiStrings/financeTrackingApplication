package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Budget;
import lombok.Data;

import java.util.List;

@Data
public class FetchBudgetsResponse extends BaseResponse{
    private List<Budget> budgets;

    public FetchBudgetsResponse(Status status, String message) {
        super(status, message);
    }

    public FetchBudgetsResponse(Status status, String message, List<Budget> budgets) {
        super(status, message);
        this.budgets = budgets;
    }
}
