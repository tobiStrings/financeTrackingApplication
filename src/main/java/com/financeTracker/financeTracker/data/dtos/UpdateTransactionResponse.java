package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Transaction;
import lombok.Data;

@Data
public class UpdateTransactionResponse extends BaseResponse{
    private Transaction transaction;

    public UpdateTransactionResponse(Status status, String message, Transaction transaction) {
        super(status, message);
        this.transaction = transaction;
    }

    public UpdateTransactionResponse(Status status, String message) {
        super(status, message);
    }
}
