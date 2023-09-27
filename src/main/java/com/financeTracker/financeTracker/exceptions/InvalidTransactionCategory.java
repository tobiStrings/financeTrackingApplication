package com.financeTracker.financeTracker.exceptions;

public class InvalidTransactionCategory extends TransactionException{
    public InvalidTransactionCategory() {
        super("Invalid transaction category");
    }

    public InvalidTransactionCategory(String message) {
        super(message);
    }
}
