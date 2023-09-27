package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Transaction;
import lombok.Data;

@Data
public class FetchSingleTransactionResponse extends BaseResponse{
    private Transaction transaction;

    public FetchSingleTransactionResponse(Status status, String message) {
        super(status, message);
    }

    public FetchSingleTransactionResponse(Status status, String message, Transaction transaction) {
        super(status, message);
        this.transaction = transaction;
    }

}
