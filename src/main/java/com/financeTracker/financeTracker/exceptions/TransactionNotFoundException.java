package com.financeTracker.financeTracker.exceptions;

public class TransactionNotFoundException extends Exception{
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
