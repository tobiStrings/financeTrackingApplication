package com.financeTracker.financeTracker.exceptions;

import com.financeTracker.financeTracker.data.enums.Status;
import lombok.Data;


public class TransactionException extends FinanceTrackerException{
    public TransactionException() {
    }

    public TransactionException(String message) {
        super(message);

    }
}
