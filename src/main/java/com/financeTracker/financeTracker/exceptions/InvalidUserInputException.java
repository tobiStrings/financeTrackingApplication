package com.financeTracker.financeTracker.exceptions;

public class InvalidUserInputException extends FinanceTrackerException{
    public InvalidUserInputException(String message) {
        super(message);
    }

    public InvalidUserInputException() {
    }
}
