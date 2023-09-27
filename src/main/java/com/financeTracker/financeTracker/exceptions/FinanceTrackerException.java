package com.financeTracker.financeTracker.exceptions;

import com.financeTracker.financeTracker.data.enums.Status;
import lombok.Data;


public class FinanceTrackerException extends Exception{
    public FinanceTrackerException() {
    }

    public FinanceTrackerException(String message) {
        super(message);
    }
}
