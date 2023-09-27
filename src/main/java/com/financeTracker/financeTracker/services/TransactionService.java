package com.financeTracker.financeTracker.services;

import com.financeTracker.financeTracker.data.dtos.*;
import com.financeTracker.financeTracker.data.model.Transaction;
import com.financeTracker.financeTracker.exceptions.TransactionNotFoundException;

public interface TransactionService {
    AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest);
    Transaction saveTransaction(Transaction transaction);

    FetchSingleTransactionResponse fetchSingleTransaction(String uuid);
    FetchTransactionsResponse fetchAllTransactions();
    FetchTransactionsResponse fetchTransactionsByTransactionCategory(String category);

    UpdateTransactionResponse updateTransaction(UpdateTransactionRequest request,String uuid);

    Transaction findTransactionByUuid(String uuid) throws TransactionNotFoundException;

    DeleteTransactionResponse deleteTransactionByUUID(String uuid);
}
