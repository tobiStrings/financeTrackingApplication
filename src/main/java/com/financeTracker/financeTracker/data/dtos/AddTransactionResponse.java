package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.enums.TransactionCategory;
import com.financeTracker.financeTracker.data.model.Transaction;
import lombok.Data;

@Data
public class AddTransactionResponse extends BaseResponse{
    private Transaction transaction;
    public AddTransactionResponse(Status status, String message, Transaction transaction) {
        super(status, message);
        this.transaction = transaction;
    }

    public AddTransactionResponse(Status status) {
        super(status);
    }
}
