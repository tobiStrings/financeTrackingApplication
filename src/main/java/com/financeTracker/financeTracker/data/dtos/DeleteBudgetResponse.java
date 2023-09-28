package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;

public class DeleteBudgetResponse extends BaseResponse{
    public DeleteBudgetResponse(Status status, String message) {
        super(status, message);
    }
}
