package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;

public class DeleteTransactionResponse extends BaseResponse{
    public DeleteTransactionResponse(Status status, String message) {
        super(status, message);
    }
}
