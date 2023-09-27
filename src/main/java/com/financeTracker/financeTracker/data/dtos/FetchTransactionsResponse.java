package com.financeTracker.financeTracker.data.dtos;

import com.financeTracker.financeTracker.data.enums.Status;
import com.financeTracker.financeTracker.data.model.Transaction;
import lombok.Data;

import java.util.List;
@Data
public class FetchTransactionsResponse extends BaseResponse{
    private List<Transaction> transactions;

    public FetchTransactionsResponse(Status status, String message, List<Transaction> transactions) {
        super(status, message);
        this.transactions = transactions;
    }

    public FetchTransactionsResponse(Status status, String message) {
        super(status, message);
    }
}
