package com.financeTracker.financeTracker.exceptions;

public class UserAlreadyExistException extends FinanceTrackerException{
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException() {
    }
}
