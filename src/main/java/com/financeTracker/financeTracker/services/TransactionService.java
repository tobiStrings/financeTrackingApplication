package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.AddTransactionRequest;
import com.financeTracker.financeTracker.data.dtos.AddTransactionResponse;
import com.financeTracker.financeTracker.data.model.Transaction;

public interface TransactionService {
    AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest);
    Transaction saveTransaction(Transaction transaction);
}
